package com.vandamodaintima.jfpsb.contador.tela.manager.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.tela.manager.marca.TelaMarcaForResult;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarProduto extends AlterarDeletarEntidade {

    private Produto produto = new Produto();
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedorAtual;
    private EditText txtMarcaAtual;
    private EditText txtCodBarraFornecedor;
    private Button btnEscolherFornecedor;
    private Button btnEscolherMarca;

    private Fornecedor fornecedor;
    private Marca marca;

    private ProdutoManager produtoManager;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;
    private static final String SEM_FORNECEDOR = "Sem Fornecedor";
    private static final String SEM_MARCA = "Sem Marca";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.content_alterar_deletar_produto);

        super.onCreate(savedInstanceState);

        produto = (Produto) getIntent().getExtras().getSerializable("produto");

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        txtFornecedorAtual = findViewById(R.id.txtFornecedorAtual);
        txtMarcaAtual = findViewById(R.id.txtMarca);
        txtCodBarraFornecedor = findViewById(R.id.txtCodBarraFornecedor);
        btnEscolherFornecedor = findViewById(R.id.btnEscolherFornecedor);
        btnEscolherMarca = findViewById(R.id.btnEscolherMarca);

        txtCodBarra.setText(produto.getCod_barra());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(String.valueOf(produto.getPreco()));

        if (produto.getFornecedor() != null) {
            txtFornecedorAtual.setText(produto.getFornecedor().getNome());
            fornecedor = produto.getFornecedor();
        } else {
            txtFornecedorAtual.setText(SEM_FORNECEDOR);
        }

        if(produto.getMarca() != null) {
            txtMarcaAtual.setText(produto.getMarca().getNome());
            marca = produto.getMarca();
        }
        else {
            txtMarcaAtual.setText(SEM_MARCA);
        }

        if (produto.getCod_barra_fornecedor() != null && !produto.getCod_barra_fornecedor().isEmpty()) {
            txtCodBarraFornecedor.setText(produto.getCod_barra_fornecedor());
        }

        setBtnAlterarFornecedor();
        setBtnEscolherMarca();
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(conn);
    }

    private void setBtnAlterarFornecedor() {
        btnEscolherFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaFornecedorForResult.class);
                startActivityForResult(intent, ESCOLHER_FORNECEDOR);
            }
        });
    }

    private void setBtnEscolherMarca() {
        btnEscolherMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaMarcaForResult.class);
                startActivityForResult(intent, ESCOLHER_MARCA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");

                    if(fornecedor != null) {
                        txtFornecedorAtual.setText(fornecedor.getNome());
                        Toast.makeText(this, "Fornecedor Escolhido. Aperte em \"Atualizar\" para Salvar.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Fornecedor Não Foi Escolhido", Toast.LENGTH_SHORT).show();
                }
                break;
            case ESCOLHER_MARCA:
                if(resultCode == RESULT_OK) {
                    marca = (Marca) data.getSerializableExtra("marca");

                    if(marca != null) {
                        txtMarcaAtual.setText(marca.getNome());
                        Toast.makeText(this, "Marca Escolhida. Aperte em \"Atualizar\" para Salvar", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(this, "Marca Não Foi Escolhida", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Produto");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Produto?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = produtoManager.deletar(produto.getCod_barra());

                if (result) {
                    Toast.makeText(AlterarDeletarProduto.this, "Produto Deletado Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                } else {
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
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cod_barra = txtCodBarra.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();
                    String cod_barra_fornecedor = txtCodBarraFornecedor.getText().toString();

                    if (descricao.isEmpty())
                        throw new Exception("O Campo de Descrição Não Pode Ficar Vazio!");

                    if (preco.isEmpty())
                        throw new Exception("O Campo de Preço Não Pode Ficar Vazio!");

                    if (!TestaIO.isValidDouble(preco))
                        throw new Exception("O Valor no Campo Preço é Inválido!");

                    Produto toUpdate = new Produto();

                    toUpdate.setCod_barra(cod_barra);
                    toUpdate.setDescricao(descricao.toUpperCase());
                    toUpdate.setPreco(Double.parseDouble(preco));
                    toUpdate.setFornecedor(fornecedor);
                    toUpdate.setCod_barra_fornecedor(cod_barra_fornecedor);
                    toUpdate.setMarca(marca);

                    if (cod_barra_fornecedor.isEmpty()) {
                        toUpdate.setCod_barra_fornecedor(null);
                    } else {
                        toUpdate.setCod_barra_fornecedor(cod_barra_fornecedor);
                    }

                    boolean result = produtoManager.atualizar(toUpdate, produto.getCod_barra());

                    if (result) {
                        Toast.makeText(AlterarDeletarProduto.this, "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        finish();
                    } else {
                        Toast.makeText(AlterarDeletarProduto.this, "Erro ao Atualizar Produto", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    Log.e("Contador", "Erro ao Atualizar Produto", e);
                    Toast.makeText(AlterarDeletarProduto.this, "Erro ao Atualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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