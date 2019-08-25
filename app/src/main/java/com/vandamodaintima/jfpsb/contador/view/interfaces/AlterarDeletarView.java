package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;

public interface AlterarDeletarView {
    void mensagemAoUsuario(String mensagem);
    void fecharTela();
    void setAlertBuilderDeletar();
    void setAlertBuilderAtualizar();
    void inicializaBotoes();
    Context getContext();
}
