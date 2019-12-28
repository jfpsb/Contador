package com.vandamodaintima.jfpsb.contador.view.interfaces;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

public interface IAdicionarContagemProduto {
    ConexaoBanco getConexaoBanco();

    void mensagemAoUsuario(String mensagem);
}
