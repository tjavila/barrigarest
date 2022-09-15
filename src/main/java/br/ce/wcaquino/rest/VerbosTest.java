package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class VerbosTest {

    @Test
    public void deveSalvarUsuario(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Jose\", \"age\": 30}")
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
                ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"age\": 30}")
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(400)
                .body("id", is(nullValue()))
                .body("error", is("Name é um atributo obrigatório"))
        ;
    }

    @Test
    public void deveEditarUsuario(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Jose\", \"age\": 30}")
        .when()
                .put("http://restapi.wcaquino.me/users/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Jose"))
        ;
    }

    @Test
    public void deveCustomizarURL(){
        given()
                .log().all()
                .contentType("application/json")
                .body("{\"name\": \"Jose\", \"age\": 30}")
                .pathParams("entidade", "users")
                .pathParams("userId", 1)
                .when()
                .put("http://restapi.wcaquino.me/{entidade}/{userId}")
                .then()
                .log().all()
                .statusCode(200)
                .body("id", is(1))
                .body("name", is("Jose"))
        ;
    }
    @Test
    public void deveRemoverUsuario(){
        given()
                .log().all()
        .when()
                .delete("http://restapi.wcaquino.me/users/1")
        .then()
                .log().all()
                .statusCode(204)

        ;
    }

    @Test
    public void naoDeveRemoverUsuarioInexistente(){
        given()
                .log().all()
        .when()
                .delete("http://restapi.wcaquino.me/users/1000")
        .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Registro inexistente"))

        ;
    }

    @Test
    public void deveSalvarUsuarioMap(){
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", "Jose");
        params.put("age", 25);

        given()
                .log().all()
                .contentType("application/json")
                .body(params)
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
        ;
    }

    @Test
    public void deveSalvarUsuarioObjeto(){
        User user = new User("Jose", 35);

        given()
                .log().all()
                .contentType("application/json")
                .body(user)
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .body("id", is(notNullValue()))
        ;
    }

    @Test
    public void deveDesserealizarAoSalvarUsuarioObjeto(){
        User user = new User("usuario deserealizado", 35);
        User usuarioInserido = given()
                .log().all()
                .contentType("application/json")
                .body(user)
        .when()
                .post("http://restapi.wcaquino.me/users")
        .then()
                .log().all()
                .statusCode(201)
                .extract().body().as(User.class)
        ;

        System.out.println(usuarioInserido);
        assertThat(usuarioInserido.getId(), notNullValue());
        assertEquals("usuario deserealizado", usuarioInserido.getName());
        assertThat(usuarioInserido.getAge(), is(35));

    }
}


