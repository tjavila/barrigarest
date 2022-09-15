package br.ce.waquino.rest.tests_refact;
import br.ce.waquino.rest.core.BaseTest;
import br.ce.waquino.rest.tests.Movimentacao;
import br.ce.waquino.rest.utils.BarrigaUtils;
import br.ce.waquino.rest.utils.DateUtils;
import org.junit.Test;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    @Test
    public void deveInserirMovimentacaoComSucesso(){
        Movimentacao mov = getMovimentacaoValida();
        given()
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao(){

        given()
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número",
                        "Conta é obrigatório",
                        "Situação é obrigatório"
                ))
        ;
    }

    @Test
    public void naoDeveCadastrarMovimentacaoFutura(){
        Movimentacao mov = getMovimentacaoValida();
        mov.setData_transacao(DateUtils.getDataFutura(2));

        given()
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItems("Data da Movimentação deve ser menor ou igual à data atual"))
        ;
    }

    @Test
    public void naoDeveRemoverContaComMovimentacao(){
        Integer CONTA_ID = BarrigaUtils.getContaIdPeloNome("Conta com movimentacao");
        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)
                .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void deveRemoverMovimentacao(){
        Integer MOV_ID = BarrigaUtils.getMovIdPeloNome("Movimentacao para exclusao");
        given()
                .pathParam("id", MOV_ID)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204)
        ;
    }

    private Movimentacao getMovimentacaoValida(){
        Movimentacao mov = new Movimentacao();
        mov.setConta_id(BarrigaUtils.getContaIdPeloNome("Conta para movimentacoes"));
        mov.setDescricao("Movimentando");
        mov.setEnvolvido("Envolvimento");
        mov.setTipo("REC");
        mov.setData_transacao(DateUtils.getDataFutura(-1));
        mov.setData_pagamento(DateUtils.getDataFutura(5));
        mov.setValor(100f);
        mov.setStatus(true);
        return mov;
    }
}
