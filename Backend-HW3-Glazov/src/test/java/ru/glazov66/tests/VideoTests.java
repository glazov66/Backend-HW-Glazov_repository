package ru.glazov66.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("Позитивные тесты (видео)")
public class VideoTests extends BaseTest {

    String uploadedVideoId;

    @DisplayName("Загрузка видеофайла")
    @Test
    void uploadVideoFileTest() {
        uploadedVideoId = given()
                .headers("Authorization", token)
                .multiPart("video", new File("src/test/resources/Video_mp4-1.8_Mb-23_sec.mp4"))
                .expect()
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

    }

    @DisplayName("Видео по URL")
    @Test
    void uploadByUrlVideoTest() {
        uploadedVideoId = given()
                .headers("Authorization", token)
                .log()
                .all()
                .multiPart("video", "https://st4.depositphotos.com/8469332/21956/v/600/depositphotos_219569478-stock-video-retro-flip-clock-showing-days.mp4")
                .expect()
                .body("success", equalTo(true))
                .body("status", equalTo(200))
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/video/{deleteHash}", "testprogmath", uploadedVideoId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

}
