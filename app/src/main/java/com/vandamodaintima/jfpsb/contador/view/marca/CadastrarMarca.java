package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.marca.CadastrarMarcaController;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import static com.vandamodaintima.jfpsb.contador.view.ActivityBaseView.LOG;

public class CadastrarMarca extends TelaCadastro {

    protected AlertDialog.Builder alertaCadastro;
    private EditText txtNome;
    private Button btnCadastrar;
    protected CadastrarMarcaController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastrar_marca, container, false);

        txtNome = view.findViewById(R.id.txtNome);
        btnCadastrar = view.findViewById(R.id.btnCadastrar);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new CadastrarMarcaController(this, conexaoBanco);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nome = txtNome.getText().toString().trim().toUpperCase();
                    setAlertaCadastro(nome);
                } catch (Exception e) {
                    Log.e(LOG, e.getMessage(), e);
                }
            }
        });

        return view;
    }

    protected void setAlertaCadastro(final String nome) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar Marca");

        String mensagem = "Deseja Cadastrar a Marca " + nome + "?";
        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.cadastrar(nome);
            }
        });

        alertaCadastro.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Marca Não Foi Cadastrada");
            }
        });

        alertaCadastro.show();
    }

    @Override
    public void limparCampos() {
        txtNome.getText().clear();
    }

    @Override
    public void focoEmViewInicial() {
        txtNome.requestFocus();
    }
}
