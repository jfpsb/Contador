package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.Button;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;

/**
 * Created by jfpsb on 15/02/2018.
 */

public class AlterarDeletarEntidade extends AppCompatActivity {
    protected ConexaoBanco conn;
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_tela);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        conn = new ConexaoBanco(getApplicationContext());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onDestroy() {
        conn.fechar();
        super.onDestroy();
    }

    protected void setBtnAtualizar() {

    }

    protected void setBtnDeletar() {

    }

    protected void setAlertBuilder(final Object objeto) {

    }
}
