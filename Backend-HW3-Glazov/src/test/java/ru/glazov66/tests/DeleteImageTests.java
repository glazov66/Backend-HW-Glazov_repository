package ru.glazov66.tests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты на удаление изображения")
public class DeleteImageTests extends BaseTest {

    String uploadedFileId;

    @DisplayName("Удаление своего изображения")
    @Test
    void deleteImageFileTest() {
        System.out.println("\nЗАГРУЖАЕМ ИЗОБРАЖЕНИЕ");
        uploadedFileId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/JPEG-Image_500_Kb.jpg"))
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

        System.out.println("\nУДАЛЯЕМ ТО ЧТО ЗАГРУЗИЛИ");
        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", "8ZWROP98x1AxQ1t")
                .prettyPeek()
                .then()
                .statusCode(200);
    }

    @DisplayName("Удаление изображения без авторизации (негативный)")
    @Test
    void deleteImageFileWithoutAuthTest() {
        System.out.println("\nЗАГРУЖАЕМ ИЗОБРАЖЕНИЕ");
        uploadedFileId = given()
                .headers("Authorization", token)
                .multiPart("image", new File("src/test/resources/JPEG-Image_40_Kb.jpg"))
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

        System.out.println("\nПЫТАЕМСЯ УДАЛИТЬ ТО ЧТО ЗАГРУЗИЛИ НЕ АВТОРИЗУЯСЬ");
        given()
                .expect()
                .body("success", equalTo(false))
                .body("status", equalTo(401))
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", uploadedFileId)
                .prettyPeek();

    }

    @DisplayName("Удаление изображения без ID (негативный)")
    @Test
    void deleteImageFileWithoutIdTest() {

        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", "")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error")
                .equals("An ID is required.");

    }

    @DisplayName("Удаление изображения с несуществующим ID (негативный)")
    @Test
    void deleteUnknownIdImageFileTest() {

        given()
                .headers("Authorization", token)
                .when()
                .delete("https://api.imgur.com/3/account/{username}/image/{deleteHash}", "testprogmath", "kjahsfddrtgsfdjnyg")
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error")
                .equals("Unauthorized");

    }

//    @DisplayName("Текст вместо изображения")
//    @Test
//    void uploadTextFileInsteadImageNegativeTest() {
//        given()
//                .headers("Authorization", token)
//                .multiPart("image", new File("src/test/resources/pom.xml.docx"))
//                .expect()
//                .body("success", equalTo(false))
//                .body("status", greaterThanOrEqualTo(400))
//                .when()
//                .post("https://api.imgur.com/3/upload")
//                .prettyPeek();
//
//    }
//
//    @DisplayName("Нет файла изображения")
//    @Test
//    void uploadNoFileImageNegativeTest() {
//        given()
//                .headers("Authorization", token)
//                .multiPart("image", "null")
//                .expect()
//                .body("success", equalTo(false))
//                .body("status", greaterThanOrEqualTo(400))
//                .when()
//                .post("https://api.imgur.com/3/upload")
//                .prettyPeek();
//
//    }
//
//    @DisplayName("Видеофайл больше допустимой длительности")
//    @Test
//    void fileExceedsMaxDurationVideoNegativeTest() {
//        given()
//                .headers("Authorization", token)
//                .multiPart("video", new File("src/test/resources/Video_mp4-20_Mb-95_sec.mp4"))
//                .expect()
//                .body("success", equalTo(false))
//                .body("status", greaterThanOrEqualTo(400))
//                .when()
//                .post("https://api.imgur.com/3/upload")
//                .prettyPeek();
//
//    }


}
