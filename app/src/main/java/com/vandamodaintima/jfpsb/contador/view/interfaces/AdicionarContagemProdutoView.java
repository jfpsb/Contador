package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

public interface AdicionarContagemProdutoView {
    void mensagemAoUsuario(String mensagem);
    void setListViewAdapter(ListAdapter adapter);
    void realizarPesquisa();
    void limparCampos();
    void cliqueEmItemLista(AdapterView<?> adapterView, int i);
    void abreProdutoNaoEncontradoDialog();
    void abrirTelaEscolhaProdutoDialog(ListAdapter adapter);
    Context getContext();
}
