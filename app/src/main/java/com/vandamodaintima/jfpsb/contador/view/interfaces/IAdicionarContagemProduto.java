package com.vandamodaintima.jfpsb.contador.view.interfaces;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.contagem.TelaVerProdutoContado;

public interface IAdicionarContagemProduto {
    void abrirTelaProdutoForResult(String... codigo);

    ConexaoBanco getConexaoBanco();

    void mensagemAoUsuario(String mensagem);
}
