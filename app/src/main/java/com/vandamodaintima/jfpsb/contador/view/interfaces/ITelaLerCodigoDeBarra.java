package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ListAdapter;

import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;

public interface ITelaLerCodigoDeBarra {
    void mensagemAoUsuario(String mensagem);
    void abrirProdutoNaoEncontradoDialog();
    void abrirTelaEscolhaProdutoDialog(ListAdapter adapter);
    Context getContext();
}
