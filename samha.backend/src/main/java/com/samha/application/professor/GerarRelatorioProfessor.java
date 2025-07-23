package com.samha.application.professor;

import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.domain.Professor;
import com.samha.domain.Servidor;
import com.samha.domain.Servidor_;
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
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Async
public class GerarRelatorioProfessor extends UseCase<Map<String, Object>> {

    private RelatorioDto relatorioDto;

    @Inject
    public GerarRelatorioProfessor(RelatorioDto relatorioDto) {
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
        ObterAulasProfessores obterAulasProfessores = new ObterAulasProfessores(relatorioDto);
        obterAulasProfessores.setGenericRepository(genericRepository);
        List<Professor> professors = obterAulasProfessores.execute().stream().sorted(Comparator.comparing(Professor::getNome)).collect(Collectors.toList());
        List<Map<String, Object>> reports = new ArrayList<>();
        Servidor servidor = genericRepository.findSingle(Servidor.class, q -> q.where(
                q.equal(q.get(Servidor_.usuario).get(Usuario_.login), SecurityContextHolder.getContext().getAuthentication().getName())
        ));
        for (var prof : professors) {
            Map<String, String> parametros = gerarHashProfessor(prof);
            parametros.putAll(this.preencherAulas(prof));
            parametros.putAll(horarioService.getGenericReportLabels("NOTURNO", 1));
            Map<String, Object> arquivo = new HashMap<>();
            arquivo.put("nome", prof.getNome());
            byte[] report = JasperHelper.generateReport(parametros, relatorioDto);
            arquivo.put("bytes", report);
            reports.add(arquivo);

            if (relatorioDto.getEnviarEmail()) {
                if(servidor != null && servidor.getEmail() != null) {
                    emailService.enviarEmail(
                            servidor.getEmail(),
                            servidor.getMatricula(),
                            new HashSet<>(List.of(prof.getEmail())),
                            relatorioDto.getSenha(),
                            emailService.montarMensagem(servidor, relatorioDto.getAno(), relatorioDto.getSemestre()),
                            "Horários de aula " + relatorioDto.getAno() + "/" + relatorioDto.getSemestre(),
                            report,
                            prof.getNome() + ".pdf");
                }
                else if(servidor == null) throw new BusinessException("Falha no envio de Email: Não foi possível encontrar o servidor associado a este usuário");
                else if (servidor.getEmail() == null) throw new BusinessException("Não é possível enviar e-mail para usuários sem e-mail cadastrado.");

            }
        }

        Map<String, Object> result = new HashMap<>();
        if (reports.size() > 1) {
            result.put("bytes", Zipper.createZipFile(reports));
            result.put("nomeArquivo", "relatorio_professor.zip");
        } else {
            result.put("bytes", reports.get(0).get("bytes"));
            result.put("nomeArquivo", reports.get(0).get("nome") + ".pdf");
        }


        return result;
    }

    public Byte[] toObjectArray(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }

        Byte[] objectArray = new Byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++) {
            objectArray[i] = byteArray[i];
        }

        return objectArray;
    }

    private Map<String, String> preencherAulas(Professor prof) {
        Map<String, String> disciplinas = new HashMap<>();
        Map<String, String> hashMapAulas = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 16; j++) {
                final int line = i;
                final int column = j;
                Optional<AulaDto> aulaMatriz = prof.getAulas().stream().filter(a -> a.getDia() == line && a.getNumero() == column).findFirst();
                if (aulaMatriz.isPresent()) {
                    AulaDto aula = aulaMatriz.get();
                    String key = aula.getDia() + String.valueOf(aula.getNumero());
                    String turma = aula.getNomeTurma();
                    disciplinas.put(aula.getSiglaDisciplina(), aula.getNomeDisciplina());
                    hashMapAulas.put(key, turma + "\n" + aula.getSiglaDisciplina());
                } else {
                    String key = line + String.valueOf(column);
                    String turma = "";
                    String sigla = "";
                    hashMapAulas.put(key, turma + "\n" + sigla);
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

    public Map<String, String> gerarHashProfessor(Professor professor){

        Map<String, String> hash = new HashMap();

        String anoSemestre = relatorioDto.getAno() + "/" + relatorioDto.getSemestre();
        hash.put("nome", professor.getNome());
        hash.put("setor", professor.getCoordenadoria().getNome());
        hash.put("aulas", professor.getAulas().size() + " aulas semanais" + "\t" + anoSemestre);

        return hash;
    }
}
