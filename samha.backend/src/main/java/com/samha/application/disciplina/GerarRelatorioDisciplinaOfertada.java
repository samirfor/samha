package com.samha.application.disciplina;

import java.util.Map;
import java.util.HashMap;

import javax.sql.DataSource;

import java.io.InputStream;
import java.sql.Connection;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperExportManager;

import com.samha.commons.BusinessException;
import com.samha.commons.UseCase;
import com.samha.domain.dto.RelatorioDisciplinaOfertadaDto;


@Async
public class GerarRelatorioDisciplinaOfertada extends UseCase<Map<String, Object>> {
 
    private RelatorioDisciplinaOfertadaDto relDto;

    @Autowired
    private DataSource dataSource;

    @Inject
    public GerarRelatorioDisciplinaOfertada(RelatorioDisciplinaOfertadaDto relatorioDisciplinaOfertadaDto) {
        this.relDto = relatorioDisciplinaOfertadaDto;
    }

    @Override
    protected Map<String, Object> execute() throws Exception {
        Map<String, Object> parametros = new HashMap<>();

        putParametros(parametros, relDto);
        
        try {
            Connection conexao = dataSource.getConnection();

            String caminhoArquivo = "relatorio/relatorioDisciplinaOfertada.jasper";
            InputStream reportStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(caminhoArquivo);

            JasperReport report = (JasperReport) JRLoader.loadObject(reportStream);
            /*
            String caminhoArquivo = "relatorio/relatorioDisciplinaOfertada.jrxml";
            InputStream arquivo = Thread.currentThread().getContextClassLoader().getResourceAsStream(caminhoArquivo);
            JasperReport report = JasperCompileManager.compileReport(arquivo);
            */
            JasperPrint print = JasperFillManager.fillReport(report, parametros, conexao);
            byte[] dadosRel =  JasperExportManager.exportReportToPdf(print);

            Map<String, Object> result = new HashMap<>();
            result.put("bytes", dadosRel);

            String nomeExport = "Relatório de Disciplinas Ofertadas - " + parametros.get("nomeCurso") + " - " + parametros.get("ano") +"/"+ parametros.get("semestre") + ".pdf";
            result.put("nomeArquivo", nomeExport);
        
            return result;        
        }catch (JRException e) {
            throw new BusinessException("Falha ao gerar relatório", e);
        }
    }

    private void putParametros(Map<String, Object> parametros, RelatorioDisciplinaOfertadaDto relDto){
        parametros.put("cursoId", relDto.getCursoId());
        parametros.put("ano", relDto.getAno());
        parametros.put("semestre", relDto.getSemestre());
        parametros.put("nomeCurso", relDto.getNomeCurso());
        parametros.put("logo", "images/logo_ifes_2.png");
    }
}



