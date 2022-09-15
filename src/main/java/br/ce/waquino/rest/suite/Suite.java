package br.ce.waquino.rest.suite;

import br.ce.waquino.rest.core.BaseTest;
import br.ce.waquino.rest.tests_refact.AuthTest;
import br.ce.waquino.rest.tests_refact.ContasTeste;
import br.ce.waquino.rest.tests_refact.MovimentacaoTest;
import br.ce.waquino.rest.tests_refact.SaldoTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;

@RunWith(org.junit.runners.Suite.class)
@org.junit.runners.Suite.SuiteClasses({
        ContasTeste.class,
        MovimentacaoTest.class,
        SaldoTest.class,
        AuthTest.class
})
public class Suite extends BaseTest {

    @BeforeClass
    public static void  login(){
        Map<String, String> login = new HashMap<>();
        login.put("email", "tayssa.avila@hotmail.com");
        login.put("senha", "84051138");

        String TOKEN = given()
                .body(login)
                .when()
                .post("/signin")
                .then()
                .statusCode(200)
                .extract().path("token")
                ;
        requestSpecification.header("Authorization", "JWT " + TOKEN);
        get("/reset").then().statusCode(200);

    }

}
