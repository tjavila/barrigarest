package br.ce.waquino.rest.tests_refact;

import br.ce.waquino.rest.core.BaseTest;
import br.ce.waquino.rest.utils.BarrigaUtils;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

public class SaldoTest extends BaseTest {

    @Test
    public void deveCalcularSaldoDasContas() {
        Integer CONTA_ID = BarrigaUtils.getContaIdPeloNome("Conta para saldo");
        given()
        .when()
                .get("/saldo")
        .then()
                .statusCode(200)
                .body("find{it.conta_id == "+CONTA_ID+"}.saldo", is("534.00"))
        ;
    }
}
