package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.CadastrarFornecedorController;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.IAposPesquisarFornecedor;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

public class CadastrarFornecedor extends TelaCadastro implements IAposPesquisarFornecedor {
    protected Button btnCadastrar;
    protected EditText txtCnpj;
    protected TextView lblCnpjRepetido;

    protected CadastrarFornecedorController controller;

    protected AlertDialog.Builder alertaCadastro;

    protected Animation slidedown;

    public CadastrarFornecedor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_fornecedor, container, false);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new CadastrarFornecedorController(this, conexaoBanco);

        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        lblCnpjRepetido = viewInflate.findViewById(R.id.lblCnpjRepetido);

        slidedown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        txtCnpj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                controller.checaCnpj(txtCnpj.getText().toString());
            }
        });

        btnCadastrar.setOnClickListener(view -> controller.pesquisarNaReceita(txtCnpj.getText().toString()));

        return viewInflate;
    }

    @Override
    public void limparCampos() {
        txtCnpj.getText().clear();
        controller.reset();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void focoEmViewInicial() {
        txtCnpj.requestFocus();
    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {

    }

    private void setAlertaCadastro(final Fornecedor fornecedor) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar Fornecedor");

        String mensagem = "Confirme os Dados do Fornecedor Encontrado Com CNPJ: " + fornecedor.getCnpj() + "\n\n";
        mensagem += "Nome: " + fornecedor.getNome() + "\n\n";

        if (!fornecedor.getFantasia().isEmpty()) {
            mensagem += "Nome Fantasia: " + fornecedor.getFantasia() + "\n\n";
        }

        mensagem += "Deseja Cadastrar Este Fornecedor?";

        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", (dialog, which) -> {
            controller.getFornecedor().setCnpj(fornecedor.getCnpj());
            controller.getFornecedor().setNome(fornecedor.getNome());
            controller.getFornecedor().setFantasia(fornecedor.getFantasia());
            controller.getFornecedor().setEmail(fornecedor.getEmail());
            controller.getFornecedor().setTelefone(fornecedor.getTelefone());
            controller.salvar();
        });
        alertaCadastro.setNegativeButton("Não", (dialog, which) -> mensagemAoUsuario("Fornecedor Não Foi Cadastrado"));

        alertaCadastro.show();
    }

    public void bloqueiaCampos() {
        btnCadastrar.setEnabled(false);
        lblCnpjRepetido.setVisibility(View.VISIBLE);
        lblCnpjRepetido.startAnimation(slidedown);
    }

    public void liberaCampos() {
        btnCadastrar.setEnabled(true);
        lblCnpjRepetido.setVisibility(View.GONE);
    }

    @Override
    public void aposPesquisarFornecedor(Fornecedor fornecedor) {
        setAlertaCadastro(fornecedor);
    }
}
