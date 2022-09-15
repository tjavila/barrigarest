package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class UserJsonTest {

    public static RequestSpecification reqSpec;
    public static ResponseSpecification resSpec;
    @BeforeClass
    public static void setup(){
        baseURI = "http://restapi.wcaquino.me";
        //port = 80;
        //basePath = "/v2";

        RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
        reqBuilder.log(LogDetail.ALL);
        reqSpec = reqBuilder.build();

        ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
        resBuilder.expectStatusCode(200);
        resSpec = resBuilder.build();

        requestSpecification = reqSpec;
        responseSpecification = resSpec;
    }
    @Test
    public void verificarPrimeiroNome(){


        given()
        .when()
                .get("/users/")
        .then()
                //.body("id", is(1))
                //.body("name", containsString("Silva"))
                //.body("age", greaterThan(18))
        ;
    }

    @Test
    public void outroExemplo(){
        Response response = RestAssured.request(Method.GET, "users/1");

        //path
        Assert.assertEquals(new Integer(1), response.path("id"));
        Assert.assertEquals(new Integer(1), response.path("%s","id"));

        //jsonpath
        JsonPath jpath = new JsonPath(response.asString());
        Assert.assertEquals(1, jpath.getInt("id"));

        //from
        int id = JsonPath.from(response.asString()).getInt("id");
        Assert.assertEquals(1, id);
    }

    @Test
    public void segundoNivel(){
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .body("id", is(2))
                .body("name", containsString("Joaquina"))
                .body("endereco.rua", is("Rua dos bobos"))
        ;

    }

    @Test
    public void verificarLista(){
        given()

                .when()
                .get("/users/3")
                .then()
                .statusCode(200)
                .body("id", is(3))
                .body("name", containsString("Ana"))
                .body("filhos", hasSize(2))
                .body("filhos.name", hasItem("Luizinho"))

        ;
    }

    @Test
    public void erroUsuarioinexistente(){
        given()

                .when()
                .get("/users/4")
                .then()
                .statusCode(404)
                .body("error", is("Usuário inexistente"))

        ;
    }

    @Test
    public void verificarListaRaiz(){
        given()

                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
        ;
    }

    @Test
    public void verificacoesAvançadas(){
        given()

                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .body("$", hasSize(3))
                .body("age.findAll{it <= 25}.size()", is(2))
                .body("findAll{it.age <= 25}.name", hasItem("Maria Joaquina"))
                .body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
                .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
        ;

    }
    @Test
    public void unindoJsonPathComJava(){
        ArrayList<String> names =
        given()
                .when()
                .get("http://restapi.wcaquino.me/users")
                .then()
                .statusCode(200)
                .extract().path("name.findAll{it.startsWith('Maria')}")
        ;

        Assert.assertEquals(1, names.size());
        Assert.assertTrue(names.get(0).equalsIgnoreCase("mArIa Joaquina"));
        Assert.assertEquals(names.get(0).toUpperCase(),"maria joaquina".toUpperCase());
    }
}
