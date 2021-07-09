package ru.glazov66.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class AccountTests extends BaseTest{

    @Test
    void getAccountInfoTest() {
        given()
                .header("Authorization", token)
                .log().uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)   //используем placeholder "username"
                .then()
                .statusCode(200);

    }

    @Test
    void getAccountInfoWithLoggingTest() {
        given()
                .header("Authorization", "Bearer 81ed217eee6d991be324edc8754a07e4ce686bb9")
                .log().method()
                .log().uri()
                .when()
                .get("https://api.imgur.com/3/account/testprogmath")
                .prettyPeek()
                .then()
                .statusCode(200);

    }

    @Test
    void getAccountInfoWithAssertionsInGivenTest() {
        given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .expect()
                .statusCode(200)
                .body("data.url", equalTo("testprogmath"))
                .body("data.avatar_name", equalTo("flavor/beer"))
                .body("data.reputation_name", equalTo("Neutral"))
                .when()
                .get("https://api.imgur.com/3/account/testprogmath")
                .prettyPeek();


    }

    @Test
    void getAccountInfoWithAssertionsAfterTest() {
        Response response = given()
                .header("Authorization", token)
                .log()
                .method()
                .log()
                .uri()
                .when()
                .get("https://api.imgur.com/3/account/{username}", username)
                .prettyPeek();
        assertThat(response.jsonPath().get("data.url"), equalTo(username));


    }

    private static void getProperties() {
        try {
            properties.load(new FileInputStream("src/test/resources/application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
