package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;

public class CadastrarFornecedorSemInternet extends CadastrarFornecedor {
    private EditText txtNome;
    private EditText txtFantasia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_fornecedor_sem_internet, container, false);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        txtNome = viewInflate.findViewById(R.id.txtNome);
        txtFantasia = viewInflate.findViewById(R.id.txtFantasia);
        lblCnpjRepetido = viewInflate.findViewById(R.id.lblCnpjRepetido);

        setManagers();
        setBtnCadastrar();
        setTxtCnpj();

        return viewInflate;
    }

    @Override
    protected void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();
                    String fantasia = txtFantasia.getText().toString();

                    if(cnpj.isEmpty())
                        throw new Exception("CNPJ Não Pode Ficar Vazio!");

                    if(nome.isEmpty())
                        throw new Exception("Nome Não Pode Ficar Vazio!");

                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome.toUpperCase());

                    if(! fantasia.isEmpty())
                        fornecedor.setFantasia(fantasia.toUpperCase());

                    Toast toast = Toast.makeText(getContext(), null, Toast.LENGTH_SHORT);

                    setAlertaCadastro(fornecedor, toast);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Contador", e.getMessage(), e);
                }
            }
        });
    }

    @Override
    protected void PesquisaAposCadastro() {
        ((CadastrarFornecedorSemInternetContainer) getActivity()).setResultCadastro(null);
    }
}
