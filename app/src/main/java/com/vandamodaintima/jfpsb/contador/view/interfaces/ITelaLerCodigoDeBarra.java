package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.ListAdapter;

import androidx.lifecycle.Lifecycle;

public interface ITelaLerCodigoDeBarra {
    void mensagemAoUsuario(String mensagem);

    void abrirProdutoNaoEncontradoDialog(String codigo);

    void playBarcodeBeep();

    Context getContext();

    Lifecycle getLifecycle();
}
