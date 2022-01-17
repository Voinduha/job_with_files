package ru.ya.danvu;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFiles {

    private ClassLoader classLoader = getClass().getClassLoader();

    @Test
    void downloadTest() throws Exception {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();
        try (InputStream is = new FileInputStream(downloadedFile)) {
            assertThat(new String(is.readAllBytes(), StandardCharsets.UTF_8))
                    .contains("Continuous Integration Builds");
        }
    }

    @Test
    void uploadFile() {
        open("https://the-internet.herokuapp.com/upload");
        $("input[type='file']").uploadFromClasspath("text.txt");
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(Condition.text("text.txt"));
    }
}
