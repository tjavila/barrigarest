package br.ce.wcaquino.rest;

import io.restassured.RestAssured;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FileTest {
    @Test
    public void deveObrigarEnvioArquivo(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/users.pdf"))
        .when()
                .post("http://restapi.wcaquino.me/upload")
        .then()
                .log().all()
                .statusCode(200)
                .body("name", is("users.pdf"))
        ;
    }

    @Test
    public void arquivoGrande(){
        given()
                .log().all()
                .multiPart("arquivo", new File("src/main/resources/app.pptx"))
                .when()
                .post("http://restapi.wcaquino.me/upload")
                .then()
                .log().all()
                .time(lessThan(1000L))
                .statusCode(413)
                //.body("name", is("users.pdf"))
        ;
    }

    @Test
    public void deveBaixarArquivo() throws IOException {
        byte[] image = given()
                .log().all()
        .when()
                .get("http://restapi.wcaquino.me/download")
        .then()
                //.log().all()
                .statusCode(200)
                .extract().asByteArray();
        ;
        File imagem = new File("src/main/resources/file.jpg");
        OutputStream out = new FileOutputStream(imagem);
        out.write(image);
        out.close();

        Assert.assertThat(imagem.length(), lessThan(100000L));
    }
}
