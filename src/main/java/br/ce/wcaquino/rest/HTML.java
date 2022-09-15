package br.ce.wcaquino.rest;

import io.restassured.http.ContentType;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class HTML {
    @Test
    public void deveFazerBuscasComHTML(){
        given()
                .log().all()
        .when()
                .get("http://restapi.wcaquino.me/v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.HTML)
        ;
    }
}
