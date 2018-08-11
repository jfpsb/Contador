package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;

public abstract class AlterarDeletarEntidade<T> extends ActivityBase {
    protected Button btnAtualizar;
    protected Button btnDeletar;
    protected AlertDialog.Builder alertBuilderDeletar;
    protected AlertDialog.Builder alertBuilderAtualizar;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        int layout = bundle.getInt("layout");

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(layout);
        stub.inflate();

        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        T objeto = (T) bundle.getSerializable(bundle.getString("entidade"));

        setBtnAtualizar();
        setBtnDeletar();
        setAlertBuilderAtualizar(objeto);
        setAlertBuilderDeletar(objeto);
    }

    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderAtualizar.create();
                alertDialog.show();
            }
        });
    }

    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderDeletar.create();
                alertDialog.show();
            }
        });
    }

    protected abstract void setAlertBuilderDeletar(T entidade);
    protected abstract void setAlertBuilderAtualizar(T entidade);
}
