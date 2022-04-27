package com.vandamodaintima.jfpsb.contador.view.interfaces;

import android.content.Context;
import android.widget.ListAdapter;

public interface ICadastrarView {
    /**
     * Limpa campos da view após cadastro.
     */
    void limparCampos();

    /**
     * Mostra mensagem ao usuário.
     * @param mensagem Texto da mensagem
     */
    void mensagemAoUsuario(String mensagem);

    /**
     * Executa após cadastro com sucesso.
     * @param args
     */
    void aposCadastro(Object... args);

    /**
     * Foca em controle específico da view após cadastro com sucesso.
     */
    void focoEmViewInicial();

    /**
     * Configura o adapter caso exista ListView na view de cadastro.
     * @param adapter
     */
    void setListViewAdapter(ListAdapter adapter);

    Context getContext();
}
