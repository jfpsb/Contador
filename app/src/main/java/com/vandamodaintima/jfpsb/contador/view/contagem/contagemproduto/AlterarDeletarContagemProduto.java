package com.vandamodaintima.jfpsb.contador.view.contagem.contagemproduto;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarContagemProduto extends ActivityBaseView implements AlterarDeletarView {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void mensagemAoUsuario(String mensagem) {

    }

    @Override
    public void fecharTela() {

    }

    @Override
    public void setAlertBuilderDeletar() {

    }

    @Override
    public void setAlertBuilderAtualizar() {

    }

    @Override
    public Context getContext() {
        return this;
    }
}
