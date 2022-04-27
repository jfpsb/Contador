package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.marca.CadastrarMarcaController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedorForResult;

import static com.vandamodaintima.jfpsb.contador.view.ActivityBaseView.LOG;

public class CadastrarMarca extends TelaCadastro {

    protected AlertDialog.Builder alertaCadastro;
    private EditText txtNome;
    private Button btnEscolherFornecedor;
    private Button btnRemoverFornecedor;
    private EditText txtFornecedor;
    private Button btnCadastrar;
    private static final int ESCOLHER_FORNECEDOR = 1;
    protected CadastrarMarcaController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        telaCadastroView = inflater.inflate(R.layout.fragment_cadastrar_marca, container, false);

        txtNome = telaCadastroView.findViewById(R.id.txtNome);
        btnEscolherFornecedor = telaCadastroView.findViewById(R.id.btnEscolherFornecedor);
        btnRemoverFornecedor = telaCadastroView.findViewById(R.id.btnRemoverFornecedor);
        txtFornecedor = telaCadastroView.findViewById(R.id.txtFornecedor);
        btnCadastrar = telaCadastroView.findViewById(R.id.btnCadastrar);

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

        btnEscolherFornecedor.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TelaFornecedorForResult.class);
            startActivityForResult(intent, ESCOLHER_FORNECEDOR);
        });

        btnRemoverFornecedor.setOnClickListener(v -> {
            controller.setFornecedorNull();
            txtFornecedor.getText().clear();
            Toast.makeText(getContext(), "Fornecedor Foi Removido Desta Marca", Toast.LENGTH_SHORT).show();
        });

        return telaCadastroView;
    }

    protected void setAlertaCadastro(final String nome) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar Marca");

        String mensagem = "Deseja Cadastrar a Marca " + nome + "?";
        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", (dialog, which) -> {
            controller.getMarca().setNome(nome);
            controller.salvar();
        });

        alertaCadastro.setNegativeButton("Não", (dialog, which) -> mensagemAoUsuario("Marca Não Foi Cadastrada"));

        alertaCadastro.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == Activity.RESULT_OK) {
                    Fornecedor fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    controller.carregaFornecedor(fornecedor);
                    txtFornecedor.setText(controller.getMarca().getFornecedor().getNome());
                }
                break;
        }
    }

    @Override
    public void limparCampos() {
        txtNome.getText().clear();
        controller.reset();
    }

    @Override
    public void focoEmViewInicial() {
        txtNome.requestFocus();
    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {

    }
}
