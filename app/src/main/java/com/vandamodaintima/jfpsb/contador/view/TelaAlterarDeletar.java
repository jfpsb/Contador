package com.vandamodaintima.jfpsb.contador.view;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class TelaAlterarDeletar extends ActivityBaseView implements AlterarDeletarView {
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder alertBuilderDeletar;
    protected AlertDialog.Builder alertBuilderAtualizar;
    protected SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void inicializaBotoes() {
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
        sqLiteDatabase.close();
        super.onDestroy();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fecharTela() {
        finish();
    }

    @Override
    public void setAlertBuilderDeletar() {
        try {
            throw new Exception("Sobrescreva na fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setAlertBuilderAtualizar() {
        try {
            throw new Exception("Sobrescreva na fragment");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}