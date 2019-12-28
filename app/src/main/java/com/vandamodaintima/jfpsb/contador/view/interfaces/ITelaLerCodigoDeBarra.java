package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.ListAdapter;

public interface ITelaLerCodigoDeBarra {
    void mensagemAoUsuario(String mensagem);

    void abrirProdutoNaoEncontradoDialog(String codigo);

    void abrirTelaEscolhaProdutoDialog(ListAdapter adapter, String codigo);

    void realizarPesquisa();

    void playBarcodeBeep();

    Context getContext();
}
