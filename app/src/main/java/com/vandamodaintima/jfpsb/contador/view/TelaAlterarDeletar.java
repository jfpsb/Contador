package com.vandamodaintima.jfpsb.contador.view;

import android.app.AlertDialog;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class TelaAlterarDeletar extends ActivityBaseView implements AlterarDeletarView {
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder alertBuilderDeletar;
    protected AlertDialog.Builder alertBuilderAtualizar;
    protected SQLiteDatabase sqLiteDatabase;

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
