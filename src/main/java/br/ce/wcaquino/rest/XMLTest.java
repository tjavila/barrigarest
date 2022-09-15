package br.ce.wcaquino.rest;

import org.junit.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class XMLTest {
    @Test
    public void testandoXML(){
        given()
        .when()
                .get("http://restapi.wcaquino.me/usersXML/3")
        .then()
                .statusCode(200)
                .body("user.name", is("Ana Julia"))
                .rootPath("user.filhos")
                .body("name.size()", is(2))
        ;
    }
}
