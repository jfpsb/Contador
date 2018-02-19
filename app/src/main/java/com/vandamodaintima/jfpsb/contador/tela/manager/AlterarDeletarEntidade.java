package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

/**
 * Created by jfpsb on 15/02/2018.
 */

public class AlterarDeletarEntidade extends ActivityBase {
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        conn = new ConexaoBanco(getApplicationContext());
    }

    protected void setBtnAtualizar() {

    }

    protected void setBtnDeletar() {

    }

    protected void setAlertBuilder(final Object objeto) {

    }
}
