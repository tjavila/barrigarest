package br.ce.waquino.rest.tests_refact;
import br.ce.waquino.rest.core.BaseTest;
import br.ce.waquino.rest.utils.BarrigaUtils;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class ContasTeste extends BaseTest {

    @Test
    public void deveIncluirContaComSucesso(){

        given()
                .body("{\"nome\": \"Conta inserida\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveAlterarContaComSucesso(){
        Integer CONTA_ID = BarrigaUtils.getContaIdPeloNome("Conta para alterar");
        given()
                .body("{\"nome\": \"Conta alterada\"}")
                .pathParam("id", CONTA_ID)
        .when()
                .put("/contas/{id}")
        .then()
                .statusCode(200)
                .body("nome", is("Conta alterada"))
        ;
    }

    @Test
    public void naoDeveInserirContaComMesmoNome(){

        given()
                .body("{\"nome\": \"Conta mesmo nome\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }
}
