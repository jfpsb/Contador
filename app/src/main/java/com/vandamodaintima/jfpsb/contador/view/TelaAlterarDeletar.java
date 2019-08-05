package com.vandamodaintima.jfpsb.contador.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public abstract class TelaAlterarDeletar extends ActivityBaseView implements AlterarDeletarView {
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder alertBuilderDeletar;
    protected AlertDialog.Builder alertBuilderAtualizar;

    protected ConexaoBanco conexaoBanco;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        inicializaBotoes();
        super.onCreate(savedInstanceState);
    }

    public void inicializaBotoes() {
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        setAlertBuilderAtualizar();
        setAlertBuilderDeletar();

        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderAtualizar.create();
                alertDialog.show();
            }
        });

        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderDeletar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fecharTela() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
