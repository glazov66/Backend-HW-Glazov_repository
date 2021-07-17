package ru.glazov66.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.glazov66.Endpoints;
import ru.glazov66.dto.PostImageResponse;
import ru.glazov66.dto.PostVideoResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@DisplayName("Позитивные тесты (видео)")
public class VideoTests extends BaseTest {

    public final String PATH_TO_VIDEO_2MB = "src/test/resources/Video_mp4-1.8_Mb-23_sec.mp4";
    public final String PATH_TO_VIDEO_BY_URL = "https://st4.depositphotos.com/8469332/21956/v/600/depositphotos_219569478-stock-video-retro-flip-clock-showing-days.mp4";

    String uploadedVideoId;

    MultiPartSpecification video2MbMultiPartSpec;
    MultiPartSpecification videoByUrlMultiPartSpec;

    static RequestSpecification requestSpecWithAuthAndMultipart2MbVideo;
    static RequestSpecification requestSpecWithAuthAndMultipartByUrlVideo;

    static ResponseSpecification positiveresponseVideoSpecification;

    @BeforeEach
    void beforeTest() {


        video2MbMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_VIDEO_2MB))
                .controlName("video")
                .build();

        videoByUrlMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_VIDEO_BY_URL))
                .controlName("video")
                .build();

        requestSpecWithAuthAndMultipart2MbVideo = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(video2MbMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipartByUrlVideo = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(videoByUrlMultiPartSpec)
                .build();


        positiveresponseVideoSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", equalTo(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.link", is(notNullValue()))
                .expectContentType(ContentType.JSON)
                .build();
    }

    @DisplayName("Загрузка видеофайла")
    @Test
    void upload2MbVideoTest() {
        uploadedVideoId = given(requestSpecWithAuthAndMultipart2MbVideo, positiveresponseVideoSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostVideoResponse.class)
                .getData().getDeletehash();
    }


    @DisplayName("Видео по URL")
    @Test
    void uploadByUrlVideoTest() {
        uploadedVideoId = given(requestSpecWithAuthAndMultipartByUrlVideo, positiveresponseVideoSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostVideoResponse.class)
                .getData().getDeletehash();

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
