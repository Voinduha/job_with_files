package ru.ya.danvu;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class FileZipTest {

    @DisplayName("Работа с zip файлом")
    @Test
    void zipFile() throws Exception {
        ZipFile zip = new ZipFile("src/test/resources/files/file.zip");

        ZipEntry csvEntry = zip.getEntry("example.csv");
        try (InputStream csvStream = zip.getInputStream(csvEntry)) {
            CSVReader reader = new CSVReader(new InputStreamReader(csvStream));
            List<String[]> list = reader.readAll();
            assertThat(list).hasSize(4).contains(
                    new String[]{"Type", "Logo"},
                    new String[]{"Sedan", "VW"},
                    new String[]{"Cupe", "Reno"},
                    new String[]{"Jeep", "Wrangler"});
        }

        ZipEntry XlsEntry = zip.getEntry("AdGuard tests.xlsx");
        try(InputStream xlsStream = zip.getInputStream(XlsEntry)) {
            XLS parsed = new XLS(xlsStream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue())
                    .isEqualTo("Without adding site to the whitelist, " +
                            "the page does not load ads in a middle of the page and in the right corner");
        }

        ZipEntry PdfEntry = zip.getEntry("Презентация.pdf");
        try(InputStream pdfStream = zip.getInputStream(PdfEntry)) {
            PDF parsed = new PDF(pdfStream);
            assertThat(parsed.text).contains("Доставка грузов по России");
        }
    }
}
