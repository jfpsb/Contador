package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.support.v4.widget.CursorAdapter;

public interface PesquisarView {

    int TELA_ALTERAR_DELETAR = 1;

    void mensagemAoUsuario(String mensagem);

    void setListViewAdapter(CursorAdapter adapter);

    /**
     * Se na tela tiver um campo pra mostrar quantidade de registros retornados na pesquisa
     * vai ser setado aqui
     * @param quantidade
     */
    void setTextoQuantidadeBusca(int quantidade);

    void realizarPesquisa(String... termos);
}