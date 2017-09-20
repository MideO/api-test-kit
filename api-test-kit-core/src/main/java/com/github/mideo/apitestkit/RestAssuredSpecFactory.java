package com.github.mideo.apitestkit;


import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class RestAssuredSpecFactory {

    public static void useRelaxedHTTPSValidation() {
        RestAssured.useRelaxedHTTPSValidation();
    }

    public static void port(final int port) {
        RestAssured.port = port;
    }


    public static RequestSpecification givenARequestSpecification() {
        return given().log().ifValidationFails();
    }

}
