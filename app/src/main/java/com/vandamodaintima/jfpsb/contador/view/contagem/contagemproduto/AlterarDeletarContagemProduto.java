package com.vandamodaintima.jfpsb.contador.view.contagem.contagemproduto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.contagemproduto.AlterarDeletarContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarContagemProduto extends TelaAlterarDeletar {
    private EditText txtCodigoBarra;
    private EditText txtCodigoBarraAlt;
    private EditText txtDescricaoProduto;
    private EditText txtDescricaoGrade;
    private EditText txtPrecoCusto;
    private EditText txtPrecoVenda;
    private EditText txtQuantidade;
    private Button btnAtualizar;
    private AlterarDeletarContagemProdutoController controller;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_contagem_produto);
        stub.inflate();

        txtCodigoBarra = findViewById(R.id.txtCodigoBarra);
        txtCodigoBarraAlt = findViewById(R.id.txtCodigoBarraAlt);
        txtDescricaoProduto = findViewById(R.id.txtDescricaoProduto);
        txtDescricaoGrade = findViewById(R.id.txtDescricaoGrade);
        txtPrecoCusto = findViewById(R.id.txtPrecoCusto);
        txtPrecoVenda = findViewById(R.id.txtPrecoVenda);
        txtQuantidade = findViewById(R.id.txtQuantidade);
        btnAtualizar = findViewById(R.id.btnAtualizar);

        String idContagemProduto = getIntent().getStringExtra("contagemproduto");
        controller = new AlterarDeletarContagemProdutoController(this, conexaoBanco, idContagemProduto);

        txtCodigoBarra.setText(controller.getModel().getProdutoGrade().getCodBarra());
        txtCodigoBarraAlt.setText(controller.getModel().getProdutoGrade().getCodBarraAlternativo());
        txtDescricaoProduto.setText(controller.getModel().getProdutoGrade().getProduto().getDescricao());
        txtDescricaoGrade.setText(controller.getModel().getProdutoGrade().getGradesToShortString());
        txtPrecoCusto.setText(String.valueOf(controller.getModel().getProdutoGrade().getPreco_custo()));
        txtPrecoVenda.setText(String.valueOf(controller.getModel().getProdutoGrade().getPreco_venda()));
        txtQuantidade.setText(String.valueOf(controller.getModel().getQuant()));

        setBtnAtualizar();
        setAlertBuilderAtualizar();
        setAlertBuilderDeletar();

        txtQuantidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() > 0) {
                    controller.getModel().setQuant(Integer.parseInt(charSequence.toString()));
                } else {
                    controller.getModel().setQuant(0);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Contagem de Produto");
        alertBuilderDeletar.setMessage("Tem certeza que deseja deletar esta contagem de produto?");
        alertBuilderDeletar.setPositiveButton("Sim", (dialogInterface, i) -> controller.deletar());
        alertBuilderDeletar.setCancelable(true);
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Contagem de Produto");
        alertBuilderAtualizar.setPositiveButton("Sim", (dialogInterface, i) -> controller.atualizar());
        alertBuilderAtualizar.setNegativeButton("Não", (dialogInterface, i) -> mensagemAoUsuario("Contagem de produto não foi alterada"));
    }

    @Override
    public void onClick(View view) {
        alertBuilderAtualizar.setMessage("Tem certeza que deseja atualizar a quantidade desta contagem de produto para " + controller.getModel().getQuant() + "?");
        super.onClick(view);
    }
}
