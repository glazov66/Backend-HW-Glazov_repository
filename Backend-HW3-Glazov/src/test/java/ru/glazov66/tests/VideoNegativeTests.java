package ru.glazov66.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@DisplayName("Негативные тесты (видео)")
public class VideoNegativeTests extends BaseTest {

    @DisplayName("Текст вместо видеофайла")
    @Test
    void uploadVideoFileTest() {
        given()
                .headers("Authorization", token)
                .multiPart("video", new File("src/test/resources/pom.xml.docx"))
                .expect()
                .body("success", equalTo(false))
                .body("status", greaterThanOrEqualTo(400))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek();

    }

    @DisplayName("Изображение вместо Видео")
    @Test
    void uploadByUrlVideoTest() {
        given()
                .headers("Authorization", token)
                .multiPart("video", "src/test/resources/JPEG-Image_7_Mb.jpg")
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
