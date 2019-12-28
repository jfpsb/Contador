package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public interface PesquisarView {
    /**
     * Mostra mensagem ao usuário na tela
     *
     * @param mensagem
     */
    void mensagemAoUsuario(String mensagem);

    /**
     * Configura o adapter usado na ListView da tela de pesquisa
     *
     * @param adapter
     */
    void setListViewAdapter(ListAdapter adapter);

    /**
     * Se na tela tiver um campo pra mostrar quantidade de registros retornados na pesquisa
     * vai ser setado aqui
     *
     * @param quantidade
     */
    void setTextoQuantidadeBusca(int quantidade);

    /**
     * Realiza a pesquisa
     */
    void realizarPesquisa();

    /**
     * Determina a ação quando o usuário tocar em algum item da lista
     *
     * @param adapterView
     * @param i
     */
    void cliqueEmItemLista(AdapterView<?> adapterView, int i);

    /**
     * Retorna contexto da activity
     *
     * @return
     */
    Context getContext();
}