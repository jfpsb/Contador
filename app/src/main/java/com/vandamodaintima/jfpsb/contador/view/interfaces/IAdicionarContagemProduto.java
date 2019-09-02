package com.vandamodaintima.jfpsb.contador.view.interfaces;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

public interface IAdicionarContagemProduto {
    void abrirTelaProdutoForResult();

    ConexaoBanco getConexaoBanco();

    void mensagemAoUsuario(String mensagem);
}
