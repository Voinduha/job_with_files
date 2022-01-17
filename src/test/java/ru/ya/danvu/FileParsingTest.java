package ru.ya.danvu;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class FileParsingTest {

    private ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void parsePdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File pdfDownload = $(byText("PDF download")).download();
        PDF parsed = new PDF(pdfDownload);
        assertThat(parsed.title).contains("JUnit 5 User Guide");
    }

    @Test
    void parseXlsTest() throws Exception {
        try (InputStream stream = classLoader.getResourceAsStream("AdGuard tests.xlsx")) {
            XLS parsed = new XLS(stream);
            assertThat(parsed.excel.getSheetAt(0).getRow(1).getCell(2).getStringCellValue())
                    .isEqualTo("Without adding site to the whitelist, " +
                            "the page does not load ads in a middle of the page and in the right corner");
        }
    }

    @Test
    void parseCsvFile() throws Exception {
        try (InputStream stream = classLoader.getResourceAsStream("example.csv")) {
            CSVReader reader = new CSVReader(new InputStreamReader(stream));
            List<String[]> list = reader.readAll();
            assertThat(list).hasSize(4).contains(
                    new String[]{"Type", "Logo"},
                    new String[]{"Sedan", "VW"},
                    new String[]{"Cupe", "Reno"},
                    new String[]{"Jeep", "Wrangler"});
        }
    }
}



