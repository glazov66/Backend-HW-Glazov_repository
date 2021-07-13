package ru.glazov66.tests;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.glazov66.Endpoints;
import ru.glazov66.dto.PostImageResponse;


import java.io.File;
import java.io.IOException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@DisplayName("Загрузка изображений")
public class ImageTests extends BaseTest {

    private final String PATH_TO_BASE64_ENCODING_IMAGE = "src/test/resources/JPEG-Image_40_Kb.jpg";
    private final String PATH_TO_7MB_IMAGE = "src/test/resources/JPEG-Image_7_Mb.jpg";
    private final String PATH_TO_10MB_IMAGE = "src/test/resources/JPEG-Image_10-Mb.jpg";
    private final String PATH_TO_LITTLE_IMAGE = "src/test/resources/PNG_Image_1_Kb.png";
    private final String PATH_TO_PNG_IMAGE = "src/test/resources/PNG-Image_Middle.png";
    private final String PATH_TO_BMP_IMAGE = "src/test/resources/BMP-Image_Middle.bmp";
    private final String PATH_TO_GIF_IMAGE = "src/test/resources/GIF-Image.gif";
    private final String PATH_TO_IMAGE_BY_URL = "https://avatars.mds.yandex.net/get-zen_doc/50335/pub_5d4c012fe854a900aea3b547_5d4c042097b5d400aeb8c0a5/scale_1200";

    static String encodedFile;
    String uploadedImageId;

    MultiPartSpecification image7MbMultiPartSpec;
    MultiPartSpecification image10MbMultiPartSpec;
    MultiPartSpecification imageLittleMultiPartSpec;
    MultiPartSpecification imagePngMultiPartSpec;
    MultiPartSpecification imageBmpMultiPartSpec;
    MultiPartSpecification imageGifMultiPartSpec;
    MultiPartSpecification base64MultiPartSpec;
    MultiPartSpecification imageByUrlMultiPartSpec;

    static RequestSpecification requestSpecWithAuthAndMultipart7MbImage;
    static RequestSpecification requestSpecWithAuthAndMultipart10MbImage;
    static RequestSpecification requestSpecWithAuthAndMultipartLittleImage;
    static RequestSpecification requestSpecWithAuthAndMultipartPngImage;
    static RequestSpecification requestSpecWithAuthAndMultipartBmpImage;
    static RequestSpecification requestSpecWithAuthAndMultipartGifImage;
    static RequestSpecification requestSpecWithAuthAndMultipartByUrlImage;
    static RequestSpecification requestSpecificationWithAuthWithBase64;

    static ResponseSpecification positiveresponseSpecification;

    @BeforeEach
    void beforeTest() {

        positiveresponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", equalTo(true))
                .expectBody("data.id", is(notNullValue()))
                .expectBody("data.link", is(notNullValue()))
                .expectContentType(ContentType.JSON)
                .build();


        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);
        base64MultiPartSpec = new MultiPartSpecBuilder(encodedFile)
                .controlName("image")
                .build();

        image7MbMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_7MB_IMAGE))
                .controlName("image")
                .build();

        image10MbMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_10MB_IMAGE))
                .controlName("image")
                .build();

        imageLittleMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_LITTLE_IMAGE))
                .controlName("image")
                .build();

        imagePngMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_PNG_IMAGE))
                .controlName("image")
                .build();

        imageBmpMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_BMP_IMAGE))
                .controlName("image")
                .build();

        imageGifMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_GIF_IMAGE))
                .controlName("image")
                .build();

        imageByUrlMultiPartSpec = new MultiPartSpecBuilder(PATH_TO_IMAGE_BY_URL)
                .controlName("image")
                .build();

        requestSpecWithAuthAndMultipart7MbImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "jpeg")
                .addMultiPart(image7MbMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipart10MbImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "jpeg")
                .addMultiPart(image10MbMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipartLittleImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "jpeg")
                .addMultiPart(imageLittleMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipartPngImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "png")
                .addMultiPart(imagePngMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipartBmpImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "bmp")
                .addMultiPart(imageBmpMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipartGifImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("title", "Picture")
                .addFormParam("type", "gif")
                .addMultiPart(imageGifMultiPartSpec)
                .build();

        requestSpecWithAuthAndMultipartByUrlImage = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addFormParam("type", "png")
                .addMultiPart(imageByUrlMultiPartSpec)
                .build();

        requestSpecificationWithAuthWithBase64 = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(base64MultiPartSpec)
                .build();

    }

    @DisplayName("Файл изображения допустимого размера")
    @Test
    void upload7MbImageTest() {
        uploadedImageId = given(requestSpecWithAuthAndMultipart7MbImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();

    }

    @DisplayName("Файл изображения граничного размера")
    @Test
    void upload10MbImageTest() {
        uploadedImageId = given(requestSpecWithAuthAndMultipart10MbImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Файл изображения маленький")
    @Test
    void uploadLittleImageTest() {
        uploadedImageId = given(requestSpecWithAuthAndMultipartLittleImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Файл изображения в формате PNG")
    @Test
    void uploadPngImageTest() {
        uploadedImageId = given(requestSpecWithAuthAndMultipartPngImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Файл изображения в формате BMP")
    @Test
    void uploadBmpImageTest() {
        given(requestSpecWithAuthAndMultipartBmpImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @DisplayName("Файл изображения в формате GIF")
    @Test
    void uploadGifImageTest() {
        uploadedImageId = given(requestSpecWithAuthAndMultipartGifImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();


    }

    @DisplayName("Файл изображения в формате Base64")
    @Test
    void uploadBase64ImageTest() {
        uploadedImageId = given(requestSpecificationWithAuthWithBase64, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE_BY_URL)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();

    }

    @DisplayName("Загрузка изображения по URL")
    @Test
    void uploadByUrlImageTest() {
        uploadedImageId = given(requestSpecWithAuthAndMultipartByUrlImage, positiveresponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE_BY_URL)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();
    }

    @AfterEach
    void tearDown() {
        given()
                .headers("Authorization", token)
                .when()
                .delete(Endpoints.GET_DELETEHASH, username, uploadedImageId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    private byte[] getFileContent() {
        byte[] byteArray = new byte[0];
        try {
            byteArray = FileUtils.readFileToByteArray(new File(PATH_TO_BASE64_ENCODING_IMAGE));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

}
