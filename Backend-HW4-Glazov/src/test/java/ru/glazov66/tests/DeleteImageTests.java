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
import ru.glazov66.dto.PostImageResponse;

import java.io.File;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

@DisplayName("Тесты на удаление изображения")
public class DeleteImageTests extends BaseTest {

    private final String PATH_TO_UPLOADED_FILE = "src/test/resources/JPEG-Image_500_Kb.jpg";
    private final String EMPTY_ID = "";
    private final String RANDOM_ID = "kjahsfddrtgsfdjnyg";

    String uploadedFileId;

    MultiPartSpecification imageForDeleteMultiPartSpec;

    static RequestSpecification requestSpecImageForDelete;
    static RequestSpecification requestSpecForAuthorization;
    static RequestSpecification requestSpecForDeleteWithoutAuth;

    static ResponseSpecification uploadResponseSpecification;
    static ResponseSpecification deleteWithoutAuthResponseSpecification;
//    static ResponseSpecification deleteWithoutIdResponseSpecification;


    @BeforeEach
    void beforeTest() {

        uploadResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(200))
                .expectBody("success", equalTo(true))
                .build();

        deleteWithoutAuthResponseSpecification = new ResponseSpecBuilder()
                .expectBody("status", equalTo(401))
                .expectBody("success", equalTo(false))
                .build();


        imageForDeleteMultiPartSpec = new MultiPartSpecBuilder(new File(PATH_TO_UPLOADED_FILE))
                .controlName("image")
                .build();
        requestSpecImageForDelete = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .addMultiPart(imageForDeleteMultiPartSpec)
                .build();

        requestSpecForAuthorization = new RequestSpecBuilder()
                .addHeader("Authorization", token)
                .build();

        requestSpecForDeleteWithoutAuth = new RequestSpecBuilder()
                .build();
    }

    @DisplayName("Удаление своего изображения")
    @Test

    void uploadImageTest() {
        System.out.println("\nЗАГРУЖАЕМ ИЗОБРАЖЕНИЕ");
        uploadedFileId = given(requestSpecImageForDelete, uploadResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();

        System.out.println("\nУДАЛЯЕМ ТО ЧТО ЗАГРУЗИЛИ");
        given(requestSpecForAuthorization, uploadResponseSpecification)
                .delete(Endpoints.GET_DELETEHASH, username, uploadedFileId)
                .prettyPeek()
                .then()
                .statusCode(200);
    }


    @DisplayName("Удаление изображения без авторизации (негативный)")
    @Test
    void deleteImageFileWithoutAuthTest() {
        System.out.println("\nЗАГРУЖАЕМ ИЗОБРАЖЕНИЕ");
        uploadedFileId = given(requestSpecImageForDelete, uploadResponseSpecification)
                .post(Endpoints.UPLOAD_IMAGE)
                .prettyPeek()
                .then()
                .extract()
                .body()
                .as(PostImageResponse.class)
                .getData().getDeletehash();

        System.out.println("\nПЫТАЕМСЯ УДАЛИТЬ ТО ЧТО ЗАГРУЗИЛИ НЕ АВТОРИЗУЯСЬ");
        given(requestSpecForDeleteWithoutAuth, deleteWithoutAuthResponseSpecification)
                .delete(Endpoints.GET_DELETEHASH, username, uploadedFileId)
                .prettyPeek();
    }

    @DisplayName("Удаление изображения без ID (негативный)")
    @Test
    void deleteImageFileWithoutIdTest() {

        given(requestSpecForAuthorization, uploadResponseSpecification)

                .delete(Endpoints.GET_DELETEHASH, username, EMPTY_ID)
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

        given(requestSpecForAuthorization, uploadResponseSpecification)

                .delete(Endpoints.GET_DELETEHASH, username, RANDOM_ID)
                .prettyPeek()
                .then()
                .extract()
                .response()
                .jsonPath()
                .getString("data.error")
                .equals("Unauthorized");

    }


}

