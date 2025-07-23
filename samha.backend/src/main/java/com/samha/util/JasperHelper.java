package com.samha.util;

import com.samha.commons.BusinessException;
import com.samha.domain.Aula;
import com.samha.domain.dto.RelatorioDto;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class JasperHelper {
    public static byte[] generateReport(Map parametros, RelatorioDto relatorioDto) {
        List relatorio = new ArrayList();
        relatorio.add(new Aula());
        // Carregue o arquivo de modelo do relatório
        try {
            String logo = "images/logo_ifes.png";
            parametros.put("logo", logo);

            String caminhoArquivo = "relatorio/" + relatorioDto.getNomeRelatorio() + ".jrxml";
            InputStream arquivo = Thread.currentThread().getContextClassLoader().getResourceAsStream(caminhoArquivo);
            JasperReport jasperReport = JasperCompileManager.compileReport(arquivo);
            JRBeanCollectionDataSource dados = new JRBeanCollectionDataSource(relatorio);

            JasperPrint print = JasperFillManager.fillReport(jasperReport, parametros, dados);
            return JasperExportManager.exportReportToPdf(print);
        } catch (JRException e) {
            throw new BusinessException("Falha ao gerar relatório", e);
        }
    }
}
