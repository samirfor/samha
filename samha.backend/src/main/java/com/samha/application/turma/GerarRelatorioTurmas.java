
package com.samha.application.turma;

import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.domain.Aula;
import com.samha.domain.Servidor;
import com.samha.domain.Servidor_;
import com.samha.domain.Turma;
import com.samha.domain.Usuario_;
import com.samha.domain.dto.AulaDto;
import com.samha.domain.dto.RelatorioDto;
import com.samha.persistence.generics.IGenericRepository;
import com.samha.service.EmailService;
import com.samha.service.HorarioService;
import com.samha.util.JasperHelper;
import com.samha.util.Zipper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Async
public class GerarRelatorioTurmas extends UseCase<Map<String, Object>> {

    private RelatorioDto relatorioDto;

    @Inject
    public GerarRelatorioTurmas(RelatorioDto relatorioDto) {
        this.relatorioDto = relatorioDto;
    }

    @Inject
    private IGenericRepository genericRepository;

    @Inject
    private HorarioService horarioService;

    @Inject
    private EmailService emailService;

    @Override
    protected Map<String, Object> execute() throws Exception {
        ObterAulasTurmaRelatorio obterAulasTurmaRelatorio = new ObterAulasTurmaRelatorio(relatorioDto, genericRepository);
        List<Turma> turmas = obterAulasTurmaRelatorio.execute().stream().sorted(Comparator.comparing(Turma::getNome)).collect(Collectors.toList());
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
            Map<String, String> parametros = gerarHashTurma(turma);
            parametros.putAll(this.preencherAulas(turma));
            parametros.putAll(horarioService.getGenericReportLabels(turma.getTurno(), 2));
            Map<String, Object> arquivo = new HashMap<>();
            String nomeExport = nomeTurma + "-" + relatorioDto.getAno() + "-" + relatorioDto.getSemestre() + ".pdf";
            arquivo.put("nome", nomeExport);
            byte[] report = JasperHelper.generateReport(parametros, relatorioDto);
            arquivo.put("bytes", report);
            reports.add(arquivo);

            if (relatorioDto.getEnviarEmail()) {
                if(servidor != null && servidor.getEmail() != null && !turma.getProfessoresEmails().isEmpty()) {
                    emailService.enviarEmail(
                            servidor.getEmail(),
                            servidor.getMatricula(),
                            turma.getProfessoresEmails(),
                            relatorioDto.getSenha(),
                            emailService.montarMensagem(servidor, relatorioDto.getAno(), relatorioDto.getSemestre()),
                            "Horários de turma " + relatorioDto.getAno() + "/" + relatorioDto.getSemestre(),
                            report,
                            nomeExport);
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


    private Map<String, String> preencherAulas(Turma turma) {
        Map<String, String> disciplinas = new HashMap<>();
        Map<String, String> hashMapAulas = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 16; j++) {
                final int line = i;
                final int column = j;
                Optional<AulaDto> aulaMatriz = turma.getAulas().stream().filter(a -> a.getDia() == line && a.getNumero() == column).findFirst();
                if (aulaMatriz.isPresent()) {
                    Aula aula = genericRepository.get(Aula.class, aulaMatriz.get().getId());
                    String professor = aula.getAlocacao().getProfessor1().obterNomeAbreviado() + this.getProfessor2String(aula);
                    String key = aula.getDia() + String.valueOf(aula.getNumero());
                    String sigla = aula.getAlocacao().getDisciplina().getSigla();
                    disciplinas.put(sigla, aula.getAlocacao().getDisciplina().getNome());
                    hashMapAulas.put(key, professor + "\n" + sigla);
                } else {
                    String key = line + String.valueOf(column);
                    String professor = "";
                    String sigla = "";
                    hashMapAulas.put(key, professor + "\n" + sigla);
                }
            }
        }

        int rodapesFaltantes = 20 - disciplinas.size();

        int k = 1;
        for (var key : disciplinas.keySet()) {
            hashMapAulas.put("rodape" + k, key + ": " + disciplinas.get(key));
            k++;
        }

        for (int i = k; i < rodapesFaltantes + k; i++) {
            hashMapAulas.put("rodape" + i, "");
        }

        return hashMapAulas;
    }

    private String getProfessor2String(Aula aula) {
        if (aula.getAlocacao().getProfessor2() != null) return aula.getAlocacao().getProfessor2().obterNomeAbreviado();
        return "";
    }

    public Map<String, String> gerarHashTurma(Turma turma){

        Map<String, String> hash = new HashMap();

        String anoSemestre = relatorioDto.getAno() + "/" + relatorioDto.getSemestre();
        hash.put("nome", turma.getNome() + " - " + ObterPeriodoAtualTurma.getPeriodoAtual(turma));
        hash.put("aulas", anoSemestre);
        hash.put("setor", turma.getMatriz().getCurso().getNome());

        return hash;
    }
}
