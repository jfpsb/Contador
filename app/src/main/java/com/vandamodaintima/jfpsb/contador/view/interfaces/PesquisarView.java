package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.widget.AdapterView;
import android.widget.ListAdapter;

public interface PesquisarView {

    int TELA_ALTERAR_DELETAR = 1;

    void mensagemAoUsuario(String mensagem);

    void setListViewAdapter(ListAdapter adapter);

    /**
     * Se na tela tiver um campo pra mostrar quantidade de registros retornados na pesquisa
     * vai ser setado aqui
     *
     * @param quantidade
     */
    void setTextoQuantidadeBusca(int quantidade);

    void realizarPesquisa();

    void cliqueEmItemLista(AdapterView<?> adapterView, int i);
}