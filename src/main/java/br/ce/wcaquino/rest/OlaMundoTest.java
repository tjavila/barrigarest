package br.ce.wcaquino.rest;

import static io.restassured.RestAssured.*;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.Matchers;
import org.junit.Assert;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.testng.internal.collections.Ints.asList;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class OlaMundoTest {
    @Test
    public void testOlaMundo(){
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        Assert.assertTrue(response.getBody().asString().equals("Ola Mundo!"));
        Assert.assertTrue("O status code deveria ser 200", response.statusCode()==200);
        Assert.assertEquals(201, response.statusCode());

        //throw new RuntimeException();

        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

    }

    @Test
    public void exemplosRest (){
        Response response = request(Method.GET, "http://restapi.wcaquino.me/ola");
        ValidatableResponse validacao = response.then();
        validacao.statusCode(200);

        get("http://restapi.wcaquino.me/ola").then().statusCode(200);

        given()
                //Pré condições
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .assertThat()
            .statusCode(200);
    }

    @Test
    public void testeMatchersHamcrest(){
        assertThat("Maria", Matchers.is("Maria"));
        assertThat(128, Matchers.isA(Integer.class));

        List<Integer> impares = asList(1,3,5,7,9);
        assertThat(impares, hasSize(5));
        assertThat(impares, contains(1,3,5,7,9));
        assertThat(impares, containsInAnyOrder(1,3,5,9,7));
        assertThat(impares, hasItem(1));
        assertThat(impares, hasItems(1,3));


    }

    @Test
    public void validarBody(){
        given()//Pré condições
        .when()
            .get("http://restapi.wcaquino.me/ola")
        .then()
            .assertThat()
            .statusCode(200)
            .body(is("Ola Mundo!"))
            .body(containsString("Mundo"))
            .body(is(not(nullValue())));
    }
}
