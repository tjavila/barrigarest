package br.ce.wcaquino.rest;

import static org.hamcrest.Matchers.*;

import io.restassured.http.ContentType;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class AuthTest {
    @Test
    public void deveAcessaSWAPI(){
        given()
                .log().all()
        .when()
                .get("https://swapi.dev/api/people/1")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Luke Skywalker"))
        ;
    }

    @Test
    public void deveAcessaClima(){
        given()
                .log().all()
                .queryParam("q", "Fortaleza,BR")
                .queryParam("appid", "97bf820141d048d6ed3a0b784ffd7ddd")
                .queryParam("units", "metric")
        .when()
                .get("https://api.openweathermap.org/data/2.5/weather")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Fortaleza"))
                .body("main.temp", greaterThan(25f))
        ;
    }

    @Test
    public void naoDeveAcessarSemSenha(){
        given()
                .log().all()
        .when()
                .get("http://restapi.wcaquino.me/basicauth")
        .then()
                .log().all()
                .statusCode(401)
        ;
    }

    @Test
    public void deveFazerAutenticacao(){
        given()
                .log().all()
        .when()
                .get("http://admin:senha@restapi.wcaquino.me/basicauth")
        .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacao2(){
        given()
                .log().all()
                .auth().basic("admin", "senha")
        .when()
                .get("http://restapi.wcaquino.me/basicauth")
        .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoChallenge(){
        given()
                .log().all()
                .auth().preemptive().basic("admin", "senha")
        .when()
                .get("http://restapi.wcaquino.me/basicauth2")
        .then()
                .log().all()
                .statusCode(200)
                .body("status", is("logado"))
        ;
    }

    @Test
    public void deveFazerAutenticacaoComToken(){
        Map<String, Object> login = new HashMap<String, Object>();
        login.put("email", "tayssa.avila@hotmail.com");
        login.put("senha", "84051138");

        //login
        //receber token
        String token = given()
                .log().all()
                .body(login)
                .contentType(ContentType.JSON)
        .when()
                .post("http://barrigarest.wcaquino.me/signin")
        .then()
                .log().all()
                .statusCode(200)
                .extract().path("token")
        ;

        //receber contas
        given()
                .log().all()
                .header("Authorization", "JWT " + token)
        .when()
                .get("http://barrigarest.wcaquino.me/contas")
        .then()
                .log().all()
                .statusCode(200)
                .body("nome", hasItem("Conta de Teste"))
        ;
    }

    @Test
    public void deveAcessarAplicacaoWeb() {
        //login
        //receber token
        String cookie = given()
                .log().all()
                .formParam("email", "tayssa.avila@hotmail.com")
                .formParam("senha", "84051138")
                .contentType(ContentType.URLENC.withCharset("UTF-8"))
        .when()
                .post("http://seubarriga.wcaquino.me/logar")
        .then()
                .log().all()
                .statusCode(200)
                .extract().header("set-cookie")
        ;

        cookie = cookie.split("=")[1].split(";")[0];
        System.out.println(cookie);

        //obter conta
        given()
                .log().all()
                .cookie("connect.sid", cookie)
        .when()
                .get("http://seubarriga.wcaquino.me/contas")
        .then()
                .log().all()
                .statusCode(200)
                .body("html.body.table.tbody.tr[0].td[0]", is("Conta de Teste"))
                ;
    }
}
