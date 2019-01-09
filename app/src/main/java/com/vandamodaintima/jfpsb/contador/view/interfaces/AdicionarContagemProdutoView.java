package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.vandamodaintima.jfpsb.contador.model.Produto;

public interface AdicionarContagemProdutoView {
    void mensagemAoUsuario(String mensagem);

    void setListViewAdapter(ListAdapter adapter);

    void realizarPesquisa();

    void limparCampos();

    void cliqueEmItemLista(AdapterView<?> adapterView, int i);

    void abrirTelaProdutoForResult();

    void abrirTelaEscolhaProdutoDialog(ListAdapter adapter);

    void retornarProdutoEncontrado(Produto produto);
}
