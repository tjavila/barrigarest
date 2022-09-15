package br.ce.wcaquino.rest;

import static org.junit.Assert.*;

import io.restassured.http.ContentType;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class EnvioDadosTest {
    @Test
    public void deveEnviarDadosViaQuery(){
        given()
                .log().all()
        .when()
                .get("http://restapi.wcaquino.me/v2/users?format=xml")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
        ;
    }

    @Test
    public void deveEnviarDadosViaQueryViaParam(){
        given()
                .log().all()
                .queryParam("format", "xml")
        .when()
                .get("http://restapi.wcaquino.me/v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
                .contentType(containsString("utf-8"))
        ;
    }

    @Test
    public void deveEnviarDadosViaHeader(){
        given()
                .log().all()
                .accept(ContentType.XML)
        .when()
                .get("http://restapi.wcaquino.me/v2/users")
        .then()
                .log().all()
                .statusCode(200)
                .contentType(ContentType.XML)
        ;
    }
}
