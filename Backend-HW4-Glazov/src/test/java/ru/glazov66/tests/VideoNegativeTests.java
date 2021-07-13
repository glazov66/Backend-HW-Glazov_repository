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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@DisplayName("Негативные тесты (видео)")
public class VideoNegativeTests extends BaseTest {

    private final String PATH_TO_TEXT_FILE = "src/test/resources/pom.xml.docx";
    private final String PATH_TO_IMAGE_FILE = "src/test/resources/JPEG-Image_7_Mb.jpg";
    private final String PATH_TO_VIDEO_FILE = "src/test/resources/Video_mp4-20_Mb-95_sec.mp4";

    static ResponseSpecification negativeVideoResponseSpecification;

    MultiPartSpecification textInsteadVideoMultiPartSpec;
    MultiPartSpecification imageInsteadVideoMultiPartSpec;
    MultiPartSpecification maxDurationNegativeMultiPartSpec;

    static RequestSpecification requestSpecUploadTextInsteadVideo;
    static RequestSpecification requestSpecUploadImageInsteadVideo;
    static RequestSpecification requestSpecMaxDurationVideoNegativeTest;

    @BeforeEach
    void beforeTest() {
        negativeVideoResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", greaterThanOrEqualTo(400))
                .expectBody("success", equalTo(false))
                .build();

        requestSpecUploadTextInsteadVideo = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(textInsteadVideoMultiPartSpec)
                .build();

        textInsteadVideoMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_TEXT_FILE))
                .controlName("image")
                .build();

        requestSpecUploadImageInsteadVideo = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(imageInsteadVideoMultiPartSpec)
                .build();

        imageInsteadVideoMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_IMAGE_FILE))
                .controlName("image")
                .build();

        requestSpecMaxDurationVideoNegativeTest = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(maxDurationNegativeMultiPartSpec)
                .build();

        maxDurationNegativeMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_VIDEO_FILE))
                .controlName("image")
                .build();
    }

    @DisplayName("Текст вместо видеофайла")
    @Test
    void uploadTextInsteadVideoTest() {
        given(requestSpecUploadTextInsteadVideo, negativeVideoResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek();

    }

    @DisplayName("Изображение вместо Видео")
    @Test
    void uploadImageInsteadVideoTest() {
        given(requestSpecUploadImageInsteadVideo, negativeVideoResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek();

    }

    @DisplayName("Видеофайл больше допустимой длительности")
    @Test
    void fileExceedsMaxDurationVideoNegativeTest() {
        given(requestSpecMaxDurationVideoNegativeTest, negativeVideoResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek();

    }

}
