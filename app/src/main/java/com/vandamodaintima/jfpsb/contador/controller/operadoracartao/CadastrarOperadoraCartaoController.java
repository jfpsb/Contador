package com.vandamodaintima.jfpsb.contador.controller.operadoracartao;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

public class CadastrarOperadoraCartaoController {
    private CadastrarView view;
    private OperadoraCartao operadoraCartao;

    CadastrarOperadoraCartaoController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        operadoraCartao = new OperadoraCartao(conexaoBanco);
    }

    public void reset() {

    }
}
