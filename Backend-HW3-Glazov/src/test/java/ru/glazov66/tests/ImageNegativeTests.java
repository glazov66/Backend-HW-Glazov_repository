package ru.glazov66.tests;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@DisplayName("Негативные тесты (изображения)")
public class ImageNegativeTests extends BaseTest {

    @DisplayName("Видео вместо изображения")
    @Test
    void uploadVideoFileInsteadImageNegativeTest() {
        given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/Video_mp4-20_Mb-95_sec.mp4"))
                .expect()
                .body("success", equalTo(false))
                .body("status", greaterThanOrEqualTo(400))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();

    }

    @DisplayName("Текст вместо изображения")
    @Test
    void uploadTextFileInsteadImageNegativeTest() {
        given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/pom.xml.docx"))
                .expect()
                .body("success", equalTo(false))
                .body("status", greaterThanOrEqualTo(400))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();

    }

    @DisplayName("Нет файла изображения")
    @Test
    void uploadNoFileImageNegativeTest() {
        given()
                .headers("Authorization", token)
                .multiPart("image", "null")
                .expect()
                .body("success", equalTo(false))
                .body("status", greaterThanOrEqualTo(400))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();

    }

    @DisplayName("Видеофайл больше допустимой длительности")
    @Test
    void fileExceedsMaxDurationVideoNegativeTest() {
        given()
                .headers("Authorization", token)
                .multiPart("video", new File("src/test/resources/Video_mp4-20_Mb-95_sec.mp4"))
                .expect()
                .body("success", equalTo(false))
                .body("status", greaterThanOrEqualTo(400))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();

    }


}
