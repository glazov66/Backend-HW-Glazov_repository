package ru.glazov66.tests;

import org.apache.commons.io.FileUtils;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

    static String encodedFile;
    String uploadedImageId;

    @BeforeEach
    void beforeTest() {
        byte[] byteArray = getFileContent();
        encodedFile = Base64.getEncoder().encodeToString(byteArray);

    }

    @DisplayName("Файл изображения допустимого размера")
    @Test
    void upload7MbImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_7MB_IMAGE))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/jpeg"))
                .body("data.link", is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

    }

    @DisplayName("Файл изображения граничного размера")
    @Test
    void upload10MbImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_10MB_IMAGE))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/jpeg"))
                .body("data.link",is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @DisplayName("Файл изображения маленький")
    @Test
    void uploadLittleImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_LITTLE_IMAGE))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/png"))
                .body("data.link",is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @DisplayName("Файл изображения в формате PNG")
    @Test
    void uploadPngImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_PNG_IMAGE))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/png"))
                .body("data.link",is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @DisplayName("Файл изображения в формате BMP")
    @Test
    void uploadBmpImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_BMP_IMAGE))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/png"))
                .body("data.link",is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");
    }

    @DisplayName("Файл изображения в формате GIF")
    @Test
    void uploadGifImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", new File(PATH_TO_GIF_IMAGE))
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/gif"))
                .body("data.link", is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/upload")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");


    }

    @DisplayName("Файл изображения в формате Base64")
    @Test
    void uploadBase64ImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", encodedFile)
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.type", equalTo("image/jpeg"))
                .body("data.link",is(notNullValue()))
                .when()
                .post("https://api.imgur.com/3/image")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.deletehash");

    }

    @DisplayName("Загрузка изображения по URL")
    @Test
    void uploadByUrlImageTest() {
        uploadedImageId = given()
                .headers("Authorization", token)
                .multiPart("image", "https://avatars.mds.yandex.net/get-zen_doc/50335/pub_5d4c012fe854a900aea3b547_5d4c042097b5d400aeb8c0a5/scale_1200")
                .expect()
                .body("success", is(true))
                .body("data.id", is(notNullValue()))
                .body("data.link",is(notNullValue()))
                .statusCode(200)
                .when()
                .post("https://api.imgur.com/3/image")
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
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedImageId)
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
