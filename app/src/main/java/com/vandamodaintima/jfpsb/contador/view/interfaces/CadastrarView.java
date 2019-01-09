package com.vandamodaintima.jfpsb.contador.view.interfaces;

public interface CadastrarView {
    void limparCampos();

    void mensagemAoUsuario(String mensagem);

    void aposCadastro(Object... args);

    void focoEmViewInicial();
}
