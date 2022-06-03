package ru.antipant.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {

    //https://selenoid.autotests.cloud/status
    //{"total":20,"used":0,"queued":0,"pending":0,"browsers":{"chrome":{"100.0":{},"99.0":{}},"firefox":{"97.0":{},"98.0":{}},"opera":{"84.0":{},"85.0":{}}}}

    @Test
    void checkTotal() {
        given()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkWithoutTotal() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total", is(20));
    }

    @Test
    void checkWithLogsTotal() {
        given()
                .log().uri()
                .log().body()
                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .log().all()
                .log().status()
                .body("total", is(20));
    }

    @Test
    void checkChrome() {
        get("https://selenoid.autotests.cloud/status")
                .then()
                .body("browsers.chrome", hasKey("100.0"));
    }

    @Test
    void checkTotalBadPractice() {
        Response response = get("https://selenoid.autotests.cloud/status")
                .then().extract().response();
        System.out.println(response.asString());

        String expectedResponse = "{\"total\":20,\"used\":0,\"queued\":0,\"pending\":0,\"browsers\"" +
                ":{\"chrome\":{\"100.0\":{},\"99.0\":{}}," +
                "\"firefox\":{\"97.0\":{},\"98.0\":{}}," +
                "\"opera\":{\"84.0\":{},\"85.0\":{}}}}";

        assertEquals(expectedResponse, response.asString());
    }

    @Test
    void checkTotalGoodPractice() {
        Integer actualTotal = get("https://selenoid.autotests.cloud/status")
                .then().extract().path("total");
        System.out.println(actualTotal);

        Integer expectedResponse = 20;

        assertEquals(expectedResponse, actualTotal);
    }

    @Test
    void check401Status() {
        get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(401);
    }

    @Test
    void check200Status() {
        given().auth().basic("user1", "1234")
                .get("https://selenoid.autotests.cloud/wd/hub/status")
                .then()
                .statusCode(200);
    }

}
