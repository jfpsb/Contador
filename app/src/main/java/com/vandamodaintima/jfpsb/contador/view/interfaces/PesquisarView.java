package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.widget.ArrayAdapter;

public interface PesquisarView {

    int TELA_ALTERAR_DELETAR = 1;

    void mensagemAoUsuario(String mensagem);

    void populaLista(ArrayAdapter adapter);

    void realizarPesquisa(String... termos);
}