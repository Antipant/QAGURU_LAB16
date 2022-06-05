package ru.antipant.tests;

import io.restassured.http.ContentType;
import model.AuthRequest;
import model.AuthResponse;
import model.UserRequest;
import model.UserResponse;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ReqressInTests {

    String baseUrl = "https://reqres.in",
            loginUrl = "/api/login",
            usersUrl = "/api/users",
            users2Url = "/api/users/2",
            name = "morpheus",
            job1 = "leader",
            job2 = "zion resident",
            email = "eve.holt@reqres.in",
            password = "cityslicka",
            token = "QpwL5tke4Pnpja7X4",
            error = "Missing password";

    @Test
    void loginTest() {

        AuthRequest request = new AuthRequest(email, password);
        AuthResponse response = given()
                .log().uri()
                .log().body()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(baseUrl + loginUrl)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(AuthResponse.class);
        org.assertj.core.api.Assertions.assertThat(response.token).contains(token);
    }

    @Test
    void missingPasswordTest() {

        AuthRequest request = new AuthRequest(email);
        AuthResponse response = given()
                .log().uri()
                .log().body()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(baseUrl + loginUrl)
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .extract().as(AuthResponse.class);
        org.assertj.core.api.Assertions.assertThat(response.error).contains(error);
    }

    @Test
    void createTest() {

        UserRequest request = new UserRequest(name, job1);
        UserResponse response = given()
                .log().uri()
                .log().body()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .post(baseUrl + usersUrl)
                .then()
                .log().status()
                .log().body()
                .statusCode(201)
                .extract().as(UserResponse.class);
        org.assertj.core.api.Assertions.assertThat(response.job).contains(request.job);
        org.assertj.core.api.Assertions.assertThat(response.name).contains(request.name);

    }

    @Test
    void updateTest() {

        UserRequest request = new UserRequest(name, job2);
        UserResponse response = given()
                .log().uri()
                .log().body()
                .body(request)
                .contentType(ContentType.JSON)
                .when()
                .put(baseUrl + users2Url)
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .extract().as(UserResponse.class);
        org.assertj.core.api.Assertions.assertThat(response.job).contains(request.job);
        org.assertj.core.api.Assertions.assertThat(response.name).contains(response.name);

    }

    @Test
    void deleteTest() {

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete(baseUrl + users2Url)
                .then()
                .log().status()
                .log().body()
                .statusCode(204);
    }
}