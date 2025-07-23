package com.samha.application.aula;

import com.samha.commons.CorEnum;
import com.samha.commons.UseCase;
import com.samha.domain.Alocacao_;
import com.samha.domain.Aula;
import com.samha.domain.Aula_;
import com.samha.domain.Oferta_;
import com.samha.domain.Professor;
import com.samha.domain.Professor_;
import com.samha.domain.RestricaoProfessor;
import com.samha.domain.RestricaoProfessor_;
import com.samha.domain.Turma_;
import com.samha.domain.dto.Conflito;
import com.samha.domain.dto.Mensagem;
import com.samha.domain.dto.RestricaoRequest;
import com.samha.persistence.IAulaRepository;
import com.samha.persistence.generics.IGenericRepository;
import com.samha.service.HorarioService;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class ObterRestricoesAulas extends UseCase<List<Conflito>> {
    private RestricaoRequest restricaoRequest;

    private List<Conflito> conflitos = new ArrayList<>();

    public ObterRestricoesAulas(RestricaoRequest restricaoRequest) {
        this.restricaoRequest = restricaoRequest;
    }

    @Inject
    public ObterRestricoesAulas(RestricaoRequest restricaoRequest, IGenericRepository genericRepository, HorarioService horarioService, IAulaRepository aulaRepository) {
        //Essas atribuições são necessárias porque há lugares em que esta classe é instanciada manualmente. Nesses casos, o Spring NÃO "INJETA" NADA. Assim,
        //as variáveis devem ser atribuídas de onde foram "injetadas" pelo Spring
        this.restricaoRequest = restricaoRequest;
        this.genericRepository = genericRepository;
        this.horarioService = horarioService;
        this.aulaRepository = aulaRepository;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Inject
    private HorarioService horarioService;

    @Autowired
    private IAulaRepository aulaRepository;

    @Override
    protected List<Conflito> execute() throws Exception {
        List<Aula> aulas = restricaoRequest.getAulas().stream().sorted((a, b) -> {
            if (a.getNumero() > b.getNumero()) return 1;
            else if (a.getNumero() < b.getNumero()) return -1;
            else return 0;
        }).collect(Collectors.toList());
        for (Aula aula : aulas) {
            setConflitoProfessorTurma(aula, aula.getAlocacao().getProfessor1());
            setConflitoIntervaloMinimoTempoMaximoProfessor(aula, aula.getAlocacao().getProfessor1());
            setConflitoRestricaoProfessor(aula, aula.getAlocacao().getProfessor1());
            if (aula.getAlocacao().getDisciplina().getTipo().equalsIgnoreCase("especial") && aula.getAlocacao().getProfessor2() != null) {
                setConflitoProfessorTurma(aula, aula.getAlocacao().getProfessor2());
                setConflitoIntervaloMinimoTempoMaximoProfessor(aula, aula.getAlocacao().getProfessor2());
                setConflitoRestricaoProfessor(aula, aula.getAlocacao().getProfessor2());
            }

        }
        return conflitos.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    private void adicionarConflitoProfessor(Professor professor, Mensagem mensagem) {
        Optional<Conflito> conflitoOptional = conflitos.stream().filter(c -> c.getProfessor().getId().equals(professor.getId())).findFirst();
        Conflito conflito;
        if (conflitoOptional.isPresent()) {
            conflito = conflitoOptional.get();
        } else {
            conflito = new Conflito();
            conflitos.add(conflito);
        }
        conflito.setProfessor(professor);
        if (verificarMensagemExistente(mensagem, conflito))
            conflito.getMensagens().add(mensagem);
    }

    private void adicionarConflitoProfessor(Professor professor, List<Mensagem> mensagems) {
        mensagems.forEach(m -> adicionarConflitoProfessor(professor, m));
    }

    private void setConflitoRestricaoProfessor(Aula aula, Professor professor) {
        final String turno = getTurnoPorAula(aula.getTurno());
        List<RestricaoProfessor> restricoes = genericRepository.find(RestricaoProfessor.class, q -> q.where(
                q.equal(q.join(RestricaoProfessor_.professor).get(Professor_.id), professor.getId()),
                q.equal(q.get(RestricaoProfessor_.turno), turno),
                q.equal(q.get(RestricaoProfessor_.dia), aula.getDia())
        ));

        List<Mensagem> mensagensRestricao = new ArrayList<>();
        boolean resposta;
        for (RestricaoProfessor restricao : restricoes) {
            List<String> restricoesMensagens = new ArrayList<>();
            Mensagem mensagem = new Mensagem();
            mensagem.setTitulo("Restrição de horário");
            setTipoByPrioridadeRestricaoProfessor(restricao, mensagem);

            resposta = identificarNumeroAulaConflitante(restricao, aula.getNumero());

            if (resposta) {
                String dia = horarioService.obterStringDia(aula.getDia());
                restricoesMensagens.add(dia);
                restricoesMensagens.add(aula.getOferta().getTurma().getNome() + ": " + horarioService.getHorarioInicial(aula.getNumero()));
                restricoesMensagens.add("Descrição: " + restricao.getNome());
                if (restricao.getDescricao() != null) restricoesMensagens.add(restricao.getDescricao());
                mensagem.setRestricoes(restricoesMensagens);
                mensagensRestricao.add(mensagem);
            }
        }

        if(mensagensRestricao.size() > 0) {
            mensagensRestricao.get(0).getAulas().add(aula);
            adicionarConflitoProfessor(professor, mensagensRestricao);
        }
    }

    private void setTipoByPrioridadeRestricaoProfessor(RestricaoProfessor restricao, Mensagem mensagem) {
        switch (restricao.getPrioridade().toUpperCase()) {
            case "ALTA":
                mensagem.setCor(CorEnum.VERMELHO.getId());
                mensagem.setTipo(1);
                break;
            case "MÉDIA":
                mensagem.setCor(CorEnum.LARANJA.getId());
                mensagem.setTipo(2);
                break;
            default:
                mensagem.setCor(CorEnum.AZUL.getId());
                mensagem.setTipo(3);
        }
    }

    public boolean identificarNumeroAulaConflitante(RestricaoProfessor restricao, int numero){

        String t = restricao.getTurno();
        int turno = getNumeroTurno(t);

        switch(numero - turno){

            case 0: return restricao.isAula1();
            case 1: return restricao.isAula2();
            case 2: return restricao.isAula3();
            case 3: return restricao.isAula4();
            case 4: return restricao.isAula5();
            case 5: return restricao.isAula6();
        }
        return false;
    }

    private String getTurnoPorAula(int turno) {
        switch (turno) {
            case 0:
                return "MATUTINO";
            case 6:
                return "VESPERTINO";
            case 12:
                return "NOTURNO";
            default: return "NAO_MAPEADO";
        }
    }

    public int getNumeroTurno(String turno){

        switch(turno.toUpperCase()){
            case "MATUTINO": return 0;
            case "VESPERTINO": return 6;
            default: return 12;
        }
    }

    private void setConflitoIntervaloMinimoTempoMaximoProfessor(Aula aula, Professor professor) {
        List<Aula> aulasProfessor = restricaoRequest.getAulas().stream().filter(a -> {
            if (a.getAlocacao().getDisciplina().getTipo().equalsIgnoreCase("especial") && a.getAlocacao().getProfessor2() != null) {
                return a.getAlocacao().getProfessor1().getId().equals(professor.getId()) || a.getAlocacao().getProfessor2().getId().equals(professor.getId());
            } else {
                return a.getAlocacao().getProfessor1().getId().equals(professor.getId());
            }
        }).collect(Collectors.toList());

        //pega as aulas de outras turmas do professor
        aulasProfessor.addAll(getAulasOutrasTurmasProfessor(professor));

        aulasProfessor = new ArrayList<>(new HashSet<>(aulasProfessor));

        setConflitoIntervaloMinimoProfessor(aulasProfessor, aula, professor);
        setConflitoTempoMaximoProfessor(aulasProfessor.stream().filter(a -> a.getDia() == aula.getDia()).collect(Collectors.toList()), aula, professor);
    }

    private Collection<? extends Aula> getAulasOutrasTurmasProfessor(Professor professor) {
        return genericRepository.find(Aula.class, q -> q.where(
                        q.equal(q.join(Aula_.oferta).get(Oferta_.ano), restricaoRequest.getOferta().getAno()),
                        q.equal(q.join(Aula_.oferta).join(Oferta_.turma).get(Turma_.ativa), true),
                        q.equal(q.join(Aula_.oferta).get(Oferta_.semestre), restricaoRequest.getOferta().getSemestre()),
                        q.notEqual(q.join(Aula_.oferta).get(Oferta_.id), restricaoRequest.getOferta().getId()),
                        q.or(
                                q.equal(q.join(Aula_.alocacao).join(Alocacao_.professor1).get(Professor_.id), professor.getId()),
                                q.equal(q.join(Aula_.alocacao).join(Alocacao_.professor2).get(Professor_.id), professor.getId())
                        )
                ));
    }

    private void setConflitoIntervaloMinimoProfessor(List<Aula> aulasProfessor, Aula aula, Professor professor) {
        List<Aula> aulaDiaAnterior;
        List<Aula> aulaProxDia;

        if (aula.getDia() != 0) {
            aulaDiaAnterior = aulasProfessor.stream().filter(a -> a.getDia() == aula.getDia() - 1).sorted(Comparator.comparing(Aula::getNumero)).collect(Collectors.toList());
            aulaProxDia = aulasProfessor.stream().filter(a -> a.getDia() == aula.getDia()).sorted(Comparator.comparing(Aula::getNumero)).collect(Collectors.toList());
        } else {
            aulaDiaAnterior = aulasProfessor.stream().filter(a -> a.getDia() == aula.getDia()).sorted(Comparator.comparing(Aula::getNumero)).collect(Collectors.toList());
            aulaProxDia = aulasProfessor.stream().filter(a -> a.getDia() == aula.getDia() + 1).sorted(Comparator.comparing(Aula::getNumero)).collect(Collectors.toList());
        }

        if (!aulaDiaAnterior.isEmpty() && !aulaProxDia.isEmpty()) {
            Aula ultimaAula = aulaDiaAnterior.get(aulaDiaAnterior.size() - 1);
            Aula primeiraAula = aulaProxDia.get(0);
            if(aula.getDia() != 0){
                primeiraAula = aula;
            }
            double tempo = horarioService.obterQuantidadeHoras(primeiraAula, ultimaAula, HorarioService.INTERVALO_MINIMO);
            if(tempo< aula.getOferta().getIntervaloMinimo()){
                montarMensagemIntervaloMinimo(ultimaAula, primeiraAula, tempo, professor);
            }
        }
    }

    private void montarMensagemIntervaloMinimo(Aula ultima, Aula primeira, double tempo, Professor professor) {
        Conflito novoConflito;
        Optional<Conflito> conflitoRegistrado = conflitos.stream().filter(c -> c.getProfessor().getId().equals(professor.getId())).findFirst();
        if (conflitoRegistrado.isPresent()) {
            novoConflito = conflitoRegistrado.get();
        } else {
            novoConflito = new Conflito();
            novoConflito.setProfessor(professor);
            conflitos.add(novoConflito);
        }
        String diaAnterior = horarioService.obterStringDia(ultima.getDia());
        String diaAtual = horarioService.obterStringDia(primeira.getDia());

        String horarioFinal = horarioService.getHorarioFinal(ultima.getNumero());
        String horarioInicial = horarioService.getHorarioInicial(primeira.getNumero());

        Mensagem mensagem = new Mensagem();

        mensagem.setTitulo("Intervalo mínimo de descanso inferior ao permitido: " + horarioService.getTimeFromDouble(tempo).toString() + " horas.");
        mensagem.setCor(CorEnum.VERMELHO.getId());
        mensagem.getAulas().add(ultima);
        mensagem.getAulas().add(primeira);
        mensagem.setTipo(1);

        List<String> restricoes = new ArrayList<>();

        restricoes.add(ultima.getOferta().getTurma().getNome() + ": " + diaAnterior + " - " + horarioFinal + ".");
        restricoes.add(primeira.getOferta().getTurma().getNome() + ": "+ diaAtual + " - " + horarioInicial + ".");

        mensagem.setRestricoes(restricoes);
        if(verificarMensagemExistente(mensagem, novoConflito)) novoConflito.getMensagens().add(mensagem);
    }

    private boolean verificarMensagemExistente(Mensagem mensagem, Conflito novoConflito) {
        Optional<Mensagem> mensagemRegistrada = novoConflito.getMensagens().stream().filter(m -> m.getVerificarMensagem().equals(mensagem.getVerificarMensagem())).findFirst();

        return !mensagemRegistrada.isPresent();
    }

    private void setConflitoTempoMaximoProfessor(List<Aula> aulasProfessor, Aula aula, Professor professor) {
        if (!aulasProfessor.isEmpty()) {
            aulasProfessor = aulasProfessor.stream().sorted(Comparator.comparing(Aula::getNumero)).collect(Collectors.toList());

            if (aula.getTurno() == 0) {
                List<Aula> proximas = aulasProfessor.stream().filter(a -> a.getNumero() > aula.getNumero()).collect(Collectors.toList());
                for (int j = 0; j < proximas.size(); j++) {
                    Aula ultima = proximas.get(j);

                    double tempo = horarioService.obterQuantidadeHoras(aula, ultima, HorarioService.TEMPO_MAXIMO);

                    if (tempo > aula.getOferta().getTempoMaximoTrabalho())
                        montarMensagemTempoMaximo(ultima, aula, tempo, professor);
                }
            } else if (aula.getTurno() == 12) {
                List<Aula> anteriores = aulasProfessor.stream().filter(a -> a.getNumero() < aula.getNumero()).collect(Collectors.toList());
                for (int i = 0; i < anteriores.size(); i++) {
                    Aula primeira = anteriores.get(i);

                    double tempo = horarioService.obterQuantidadeHoras(primeira, aula, HorarioService.TEMPO_MAXIMO);

                    if (tempo> aula.getOferta().getTempoMaximoTrabalho())
                        montarMensagemTempoMaximo(aula, primeira, tempo, professor);
                }
            }

        }
    }

    public void montarMensagemTempoMaximo(Aula ultima, Aula primeira, double tempo, Professor professor) {
        Conflito novoConflito;
        Optional<Conflito> conflito = conflitos.stream().filter(c -> c.getProfessor().getId().equals(professor.getId())).findFirst();
        if(conflito.isPresent()) {
            novoConflito = conflito.get();
        } else {
            novoConflito = new Conflito();
            conflitos.add(novoConflito);
        }

        LocalTime hour = horarioService.getTimeFromDouble(tempo);

        novoConflito.setProfessor(professor);

        Mensagem mensagem = new Mensagem();
        String dia = horarioService.obterStringDia(ultima.getDia());
        mensagem.setTitulo("Tempo máximo de trabalho superior ao permitido: " + hour.toString() + " horas.");
        mensagem.setCor(CorEnum.VERMELHO.getId());
        mensagem.getAulas().add(ultima);
        mensagem.getAulas().add(primeira);
        mensagem.setTipo(1);
        List<String> restricoes = new ArrayList<>();
        restricoes.add(dia);
        restricoes.add(primeira.getOferta().getTurma().getNome() + ": " + horarioService.getHorarioInicial(primeira.getNumero()));
        restricoes.add(ultima.getOferta().getTurma().getNome() + ": " + horarioService.getHorarioFinal(ultima.getNumero()));

        mensagem.setRestricoes(restricoes);
        if(verificarMensagemExistente(mensagem, novoConflito)) novoConflito.getMensagens().add(mensagem);
    }

    /**
     * Método que retorna os conflitos de um professor dado uma determinada aula.
     * Um professor não pode estar ao mesmo tempo em duas turmas diferentes, logo, o filtro por oferta é feito abaixo
     *
     * @param aula
     * @return
     */
    private void setConflitoProfessorTurma(Aula aula, Professor professor) {

        // A CONSULTA ABAIXO RETORNA UMA CONSULTA ERRADA, POIS ELE FAZ UM JOIN COM A TABELA PROFESSORE DUAS VEZES E UMA DELA NÃO RETORNA 
        // DADO, NÃO RETORNANDO NENHUM DADO PARA A CONSULTA
        // List<Aula> aulasProfessor = genericRepository.find(Aula.class, q -> q.where(
        //         q.equal(q.get(Aula_.numero), aula.getNumero()),
        //         q.equal(q.get(Aula_.dia), aula.getDia()),
        //         q.equal(q.join(Aula_.alocacao).get(Alocacao_.ano), aula.getAlocacao().getAno()),
        //         q.equal(q.join(Aula_.alocacao).get(Alocacao_.semestre), aula.getAlocacao().getSemestre()),
        //         q.or(
        //                 q.equal(q.join(Aula_.alocacao).join(Alocacao_.professor1).get(Professor_.id), professor.getId()),
        //                 q.equal(q.join(Aula_.alocacao).join(Alocacao_.professor2).get(Professor_.id), professor.getId())
        //         ),
        //         q.notEqual(q.join(Aula_.oferta).get(Oferta_.id), aula.getOferta().getId()),
        //         q.equal(q.join(Aula_.oferta).get(Oferta_.turma).get(Turma_.ativa), true),
        //         q.equal(q.join(Aula_.oferta).get(Oferta_.ano), aula.getOferta().getAno()),
        //         q.equal(q.join(Aula_.oferta).get(Oferta_.semestre), aula.getOferta().getSemestre())
        //         //, q.equal(q.get(Aula_.id), 2) // adicionado para verificar o que é exeuctado no lado do SGBD
        // ));
        
        List<Aula> aulasProfessor = aulaRepository.buscaAulaMesmoHorario(aula.getOferta().getId(),  aula.getDia(), aula.getNumero(), aula.getOferta().getAno(), aula.getOferta().getSemestre(), professor.getId() );

        if (!aulasProfessor.isEmpty()) {
            Mensagem mensagem = new Mensagem();
            mensagem.setTitulo("Está em outra(s) turma(s) neste horário: ");
            mensagem.setCor(CorEnum.VERMELHO.getId());
            mensagem.setTipo(1);
            List<String> restricoes = new ArrayList<>();
            for (Aula restricao : aulasProfessor) {
                restricoes.add(restricao.getOferta().getTurma().getNome() + " - " + restricao.getAlocacao().getDisciplina().getNome() + ". ");
                restricoes.add(horarioService.getHorarioInicial(aula.getNumero()) + " a " + horarioService.getHorarioFinal(aula.getNumero()));
                mensagem.getAulas().add(aula);
            }
            mensagem.setRestricoes(restricoes);
            adicionarConflitoProfessor(professor, mensagem);
        }
    }
}
