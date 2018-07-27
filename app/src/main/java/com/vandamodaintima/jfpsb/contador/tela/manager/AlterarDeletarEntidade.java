package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

/**
 * Created by jfpsb on 15/02/2018.
 */

public abstract class AlterarDeletarEntidade extends ActivityBase {
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder alertBuilderDeletar;
    protected AlertDialog.Builder alertBuilderAtualizar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    protected abstract void setBtnAtualizar();
    protected abstract void setBtnDeletar();
    protected abstract void setAlertBuilderDeletar();
    protected abstract void setAlertBuilderAtualizar();
}
