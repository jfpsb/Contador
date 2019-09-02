package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListAdapter;

public interface ITelaVerProdutoContado {
    void setListViewAdapter(ListAdapter adapter);
    void realizarPesquisa();
    void cliqueEmItemLista(AdapterView<?> adapterView, int i);
    Context getContext();
    void mensagemAoUsuario(String mensagem);
}
