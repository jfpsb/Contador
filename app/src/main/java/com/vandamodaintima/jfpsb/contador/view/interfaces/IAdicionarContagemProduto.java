package com.vandamodaintima.jfpsb.contador.view.interfaces;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

//TODO: deletar isso aqui
public interface IAdicionarContagemProduto {
    ConexaoBanco getConexaoBanco();

    void mensagemAoUsuario(String mensagem);
}
