package com.vandamodaintima.jfpsb.contador.tela;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class TelaPesquisa extends FragmentBase {
    protected Cursor cursorPesquisa = null;

    protected static final int TELA_ALTERAR_DELETAR = 1;

    public TelaPesquisa() {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        super.onDestroy();
    }
}
