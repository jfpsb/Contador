package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarProduto extends AlterarDeletarEntidade {

    private Produto produto = new Produto();
    private Fornecedor fornecedor;
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedorAtual;

    private Button btnAlterarFornecedor;

    private ProdutoManager produtoManager;
    private FornecedorManager fornecedorManager;

    private static final int ALTERAR_FORNECEDOR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_produto);
        stub.inflate();

        produto = (Produto) getIntent().getExtras().getSerializable("produto");

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        txtFornecedorAtual = findViewById(R.id.txtFornecedorAtual);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);
        btnAlterarFornecedor = findViewById(R.id.btnAlterarFornecedor);

        txtCodBarra.setText(produto.getCod_barra());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(String.valueOf(produto.getPreco()));

        if(produto.getFornecedor() != null) {
            txtFornecedorAtual.setText(produto.getFornecedor().getNome());
            fornecedor = produto.getFornecedor();
        }
        else{
            txtFornecedorAtual.setText("Sem Fornecedor");
        }

        setAlertBuilderDeletar();
        setAlertBuilderAtualizar();

        setManagers();
        setBtnAtualizar();
        setBtnDeletar();
        setBtnAlterarFornecedor();
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(conn);
        produtoManager = new ProdutoManager(conn);
    }

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderAtualizar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = alertBuilderDeletar.create();
                alertDialog.show();
            }
        });
    }

    private void setBtnAlterarFornecedor() {
        btnAlterarFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, AlterarFornecedorEmProdutoContainer.class);
                startActivityForResult(intent, ALTERAR_FORNECEDOR);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ALTERAR_FORNECEDOR:
                if(resultCode == Activity.RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    Log.i("Contador", "Fornecedor: " + fornecedor.getNome());
                    Toast.makeText(this, "Fornecedor Escolhido. Aperte em \"Atualizar\" para Finalizar.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Fornecedor Não Foi Alterado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Produto");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Produto " + produto.getCod_barra() + " - " + produto.getDescricao() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = produtoManager.deletar(produto.getCod_barra());

                if(result) {
                    Toast.makeText(AlterarDeletarProduto.this, "Produto Deletado Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                else {
                    Toast.makeText(AlterarDeletarProduto.this, "Erro ao Deletar Produto", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Produto");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto" + produto.getCod_barra() + " - " + produto.getDescricao() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cod_barra = txtCodBarra.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();

                    if(TestaIO.isStringEmpty(descricao))
                        throw new Exception("O campo de descrição não pode ficar vazio!");

                    if(TestaIO.isStringEmpty(preco))
                        throw new Exception("O campo de preço não pode ficar vazio!");

                    if(!TestaIO.isValidDouble(preco))
                        throw new Exception("O valor no campo preço é inválido!");

                    Produto toUpdate = new Produto();

                    toUpdate.setCod_barra(cod_barra);
                    toUpdate.setDescricao(descricao.toUpperCase());
                    toUpdate.setPreco(Double.parseDouble(preco));
                    toUpdate.setFornecedor(fornecedor);

                    boolean result = produtoManager.atualizar(toUpdate, produto.getCod_barra());

                    if(result) {
                        Toast.makeText(AlterarDeletarProduto.this, "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    }
                    else {
                        Toast.makeText(AlterarDeletarProduto.this, "Erro ao Atualizar Produto", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e) {
                    Log.e("Contador", "Erro", e);
                    Toast.makeText(AlterarDeletarProduto.this, "Erro ao Atualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Alterado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}