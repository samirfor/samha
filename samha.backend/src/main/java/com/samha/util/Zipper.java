package com.samha.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {
    public static byte[] createZipFile(List<Map<String, Object>> results) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ZipOutputStream zos = new ZipOutputStream(baos)) {

            List<String> usedEntrys = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                Map<String, Object> doc = results.get(i);
                byte[] fileBytes = (byte[]) doc.get("bytes");
                String fileName = doc.get("nome") + ".pdf";
                if (usedEntrys.contains(fileName)) {
                    fileName += "_" + i;
                }else usedEntrys.add(fileName);

                ZipEntry zipEntry = new ZipEntry(fileName);
                zos.putNextEntry(zipEntry);
                zos.write(fileBytes);
                zos.closeEntry();
            }

            zos.finish();
            zos.close();

            byte[] zipBytes = baos.toByteArray();

            System.out.println("Arquivos zipados salvos com sucesso.");

            return zipBytes;
        } catch (IOException e) {
            throw new RuntimeException("Erro ao compactar e salvar os arquivos: " + e.getMessage());
        }
    }
}
