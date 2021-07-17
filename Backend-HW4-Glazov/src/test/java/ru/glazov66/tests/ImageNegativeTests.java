package ru.glazov66.tests;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.glazov66.Endpoints;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Негативные тесты (изображения)")
public class ImageNegativeTests extends BaseTest {

    MultiPartSpecification videoInsteadImageMultiPartSpec;
    MultiPartSpecification textInsteadImageMultiPartSpec;
    MultiPartSpecification noFileNegativeMultiPartSpec;


    static ResponseSpecification negativeResponseSpecification;

    static RequestSpecification requestSpecUploadVideoFileInsteadImage;
    static RequestSpecification requestSpecUploadTextFileInsteadImage;
    static RequestSpecification requestSpecNoFileNegativeTest;



    @BeforeEach
    void beforeTest() {
        negativeResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", greaterThanOrEqualTo(400))
                .expectBody("success", equalTo(false))
                .build();


        requestSpecUploadVideoFileInsteadImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(videoInsteadImageMultiPartSpec)
                .build();

        videoInsteadImageMultiPartSpec = new MultiPartSpecBuilder(new File("src/test/resources/Video_mp4-20_Mb-95_sec.mp4"))
                .controlName("image")
                .build();

        requestSpecUploadTextFileInsteadImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(textInsteadImageMultiPartSpec)
                .build();

        textInsteadImageMultiPartSpec = new MultiPartSpecBuilder(new File("src/test/resources/pom.xml.docx"))
                .controlName("image")
                .build();

        requestSpecNoFileNegativeTest = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(noFileNegativeMultiPartSpec)
                .build();

        noFileNegativeMultiPartSpec = new MultiPartSpecBuilder(new File("null"))
                .controlName("image")
                .build();



    }

    @DisplayName("Видео вместо изображения")
    @Test
    void uploadVideoFileInsteadImageNegativeTest() {
        given(requestSpecUploadVideoFileInsteadImage, negativeResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek();

    }

    @DisplayName("Текст вместо изображения")
    @Test
    void uploadTextFileInsteadImageNegativeTest() {
        given(requestSpecUploadTextFileInsteadImage, negativeResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek();
    }

    @DisplayName("Нет файла изображения")
    @Test
    void uploadNoFileImageNegativeTest() {
        given(requestSpecNoFileNegativeTest, negativeResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek();

    }




}
