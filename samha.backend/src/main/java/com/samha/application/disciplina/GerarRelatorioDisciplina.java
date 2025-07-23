package com.samha.application.disciplina;

import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.domain.Aula;
import com.samha.domain.Aula_;
import com.samha.domain.Disciplina;
import com.samha.domain.Oferta_;
import com.samha.domain.Servidor;
import com.samha.domain.Servidor_;
import com.samha.domain.Turma;
import com.samha.domain.Turma_;
import com.samha.domain.Usuario_;
import com.samha.domain.dto.RelatorioDto;
import com.samha.persistence.generics.IGenericRepository;
import com.samha.persistence.generics.IQueryHelper;
import com.samha.service.EmailService;
import com.samha.util.JasperHelper;
import com.samha.util.Zipper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import javax.persistence.criteria.Predicate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Async
public class GerarRelatorioDisciplina extends UseCase<Map<String, Object>> {

    private RelatorioDto relatorioDto;

    @Inject
    public GerarRelatorioDisciplina(RelatorioDto relatorioDto) {
        this.relatorioDto = relatorioDto;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Inject
    private EmailService emailService;

    @Override
    protected Map<String, Object> execute() throws Exception {
        List<Turma> turmas = genericRepository.find(Turma.class, q -> q.where(
                q.equal(q.get(Turma_.ativa), true)
        ));
        List<Map<String, Object>> reports = new ArrayList<>();
        Servidor servidor = genericRepository.findSingle(Servidor.class, q -> q.where(
                q.equal(q.get(Servidor_.usuario).get(Usuario_.login), SecurityContextHolder.getContext().getAuthentication().getName())
        ));
        for (var turma : turmas) {
            String nomeTurma = turma.getNome();
            //zip entry error com turmas de mesmo nome.
            List<Turma> turmasComMesmoNome = turmas.stream().filter(t -> t.getNome().equals(turma.getNome())).collect(Collectors.toList());
            if (turmasComMesmoNome.size() > 1) {
                nomeTurma += LocalTime.now().getNano();
            }
            String mensagem = "\n\t\t\t\t\t\t" + nomeTurma + " - " + relatorioDto.getAno() + "/" + relatorioDto.getSemestre() + "\n\n\n";

            List<Aula> aulasTurma = genericRepository.find(Aula.class, q -> q.where(
                    q.equal(q.get(Aula_.oferta).get(Oferta_.turma), turma),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.ano), relatorioDto.getAno()),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.semestre), relatorioDto.getSemestre()),
                    getFiltroOfertaPublicaPredicate(q)
            ));
            mensagem = mensagem + obterSiglaDisciplina(aulasTurma) + "\n";

            turma.setProfessoresEmails(
                    aulasTurma.stream()
                            .map(Aula::getAlocacao)
                            .flatMap(alocacao -> {
                                Set<String> professoresEmails = new HashSet<>();
                                professoresEmails.add(alocacao.getProfessor1().getEmail());
                                if (alocacao.getProfessor2() != null) professoresEmails.add(alocacao.getProfessor2().getEmail());
                                return professoresEmails.stream();
                            })
                            .collect(Collectors.toSet())
            );

            String nomeExport = nomeTurma + "-Relatório de Disciplinas-" + relatorioDto.getAno() + "-" + relatorioDto.getSemestre() + ".pdf";

            List relatorio = new ArrayList();
            relatorio.add(new Aula());

            relatorioDto.setNomeRelatorio("relatorioDisciplinas");
            Map parametros =  new HashMap();
            parametros.put("mensagem", mensagem);

            Map<String, Object> result = new HashMap<>();
            result.put("nome", nomeExport);
            result.put("bytes", JasperHelper.generateReport(parametros, relatorioDto));
            reports.add(result);
            if (relatorioDto.getEnviarEmail()) {
                if(servidor != null && servidor.getEmail() != null && !turma.getProfessoresEmails().isEmpty()) {
                    emailService.enviarEmail(
                            servidor.getEmail(),
                            servidor.getMatricula(),
                            turma.getProfessoresEmails(),
                            relatorioDto.getSenha(),
                            emailService.montarMensagem(servidor, relatorioDto.getAno(), relatorioDto.getSemestre()),
                            "Relatório de disciplinas " + relatorioDto.getAno() + "/" + relatorioDto.getSemestre(),
                            (byte[]) result.get("bytes"),
                            nomeExport );
                }
                else if(servidor == null) throw new BusinessException("Falha no envio de Email: Não foi possível encontrar o servidor associado a este usuário");
                else if (servidor.getEmail() == null) throw new BusinessException("Não é possível enviar e-mail para usuários sem e-mail cadastrado.");
            }
        }

        Map<String, Object> result = new HashMap<>();
        if (reports.size() > 1) {
            result.put("bytes", Zipper.createZipFile(reports));
            result.put("nomeArquivo", "relatorio_turmas.zip");
        } else {
            result.put("bytes", reports.get(0).get("bytes"));
            result.put("nomeArquivo", reports.get(0).get("nome"));
        }

        return result;
    }

    private Predicate getFiltroOfertaPublicaPredicate(IQueryHelper<Aula, Aula> q) {
        boolean isAuthenticated = SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken;
        if (isAuthenticated) {
            return q.or(
                    q.equal(q.get(Aula_.oferta).get(Oferta_.publica), true),
                    q.equal(q.get(Aula_.oferta).get(Oferta_.publica), false)
            );
        } else {
            return q.equal(q.get(Aula_.oferta).get(Oferta_.publica), true);
        }
    }

    public String obterSiglaDisciplina(List<Aula> aulas){

        String mensagem = "";
        List<Disciplina> disciplinas = new ArrayList<>();

        for(Aula aula : aulas){
            if(!disciplinas.contains(aula.getAlocacao().getDisciplina())){

                mensagem = mensagem + "\t" + aula.getAlocacao().getDisciplina().getSigla() + " - " + aula.getAlocacao().getDisciplina().getNome() + ".\n";
                mensagem = mensagem + "\tProfessor da Disciplina: " + aula.getAlocacao().getProfessor1().getNome() + " (" + aula.getAlocacao().getProfessor1().obterNomeAbreviado() + ").\n";

                if(aula.getAlocacao().getDisciplina().getTipo().equalsIgnoreCase("ESPECIAL") && aula.getAlocacao().getProfessor2() != null){
                    mensagem = mensagem + "\tProfessor da Disciplina: " + aula.getAlocacao().getProfessor2().getNome() + " (" + aula.getAlocacao().getProfessor2().obterNomeAbreviado() + ").\n";
                }

                mensagem = mensagem + "\n";
                disciplinas.add(aula.getAlocacao().getDisciplina());
            }
        }

        return mensagem;
    }
}
