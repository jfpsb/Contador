package com.vandamodaintima.jfpsb.contador.tela.manager.produto;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.AlterarFornecedorEmProdutoContainer;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarProduto extends AlterarDeletarEntidade {

    private Produto produto = new Produto();
    private Fornecedor fornecedor;
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedorAtual;
    private EditText txtCodBarraFornecedor;
    private Spinner spinnerMarca;

    private Button btnAlterarFornecedor;

    private ProdutoManager produtoManager;
    private MarcaManager marcaManager;

    private static final int ALTERAR_FORNECEDOR = 1;
    private static final String SEM_FORNECEDOR = "Sem Fornecedor";

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
        txtCodBarraFornecedor = findViewById(R.id.txtCodBarraFornecedor);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);
        btnAlterarFornecedor = findViewById(R.id.btnAlterarFornecedor);
        spinnerMarca = findViewById(R.id.spinnerMarca);

        txtCodBarra.setText(produto.getCod_barra());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(String.valueOf(produto.getPreco()));

        if (produto.getFornecedor() != null) {
            txtFornecedorAtual.setText(produto.getFornecedor().getNome());
            fornecedor = produto.getFornecedor();
        } else {
            txtFornecedorAtual.setText(SEM_FORNECEDOR);
        }

        if(produto.getCod_barra_fornecedor() != null && ! produto.getCod_barra_fornecedor().isEmpty()) {
            txtCodBarraFornecedor.setText(produto.getCod_barra_fornecedor());
        }

        setSpinnerMarca();
        setAlertBuilderDeletar();
        setAlertBuilderAtualizar();

        setManagers();
        setBtnAtualizar();
        setBtnDeletar();
        setBtnAlterarFornecedor();
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(conn);
        marcaManager = new MarcaManager(conn);
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
                if (resultCode == Activity.RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    txtFornecedorAtual.setText(fornecedor.getNome());
                    Toast.makeText(this, "Fornecedor Escolhido. Aperte em \"Atualizar\" para Finalizar.", Toast.LENGTH_SHORT).show();
                } else {
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
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto " + produto.getCod_barra() + " - " + produto.getDescricao() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cod_barra = txtCodBarra.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();
                    String cod_barra_fornecedor = txtCodBarraFornecedor.getText().toString();
                    int marca = (int) spinnerMarca.getSelectedItemId();

                    if (descricao.isEmpty())
                        throw new Exception("O campo de descrição não pode ficar vazio!");

                    if (preco.isEmpty())
                        throw new Exception("O campo de preço não pode ficar vazio!");

                    if (!TestaIO.isValidDouble(preco))
                        throw new Exception("O valor no campo preço é inválido!");

                    if(! cod_barra_fornecedor.isEmpty() && ! TestaIO.isValidInt(cod_barra_fornecedor)) {
                        throw new Exception("Digite um Código de Barras de Fornecedor Válido!");
                    }

                    Produto toUpdate = new Produto();

                    toUpdate.setCod_barra(cod_barra);
                    toUpdate.setDescricao(descricao.toUpperCase());
                    toUpdate.setPreco(Double.parseDouble(preco));
                    toUpdate.setFornecedor(fornecedor);
                    toUpdate.setCod_barra_fornecedor(cod_barra_fornecedor);

                    if(marca != -1) {
                        toUpdate.setMarca(marca);
                    }
                    else {
                        toUpdate.setMarca(0);
                    }

                    if(cod_barra_fornecedor.isEmpty()) {
                        toUpdate.setCod_barra_fornecedor(null);
                    }
                    else {
                        toUpdate.setCod_barra_fornecedor(cod_barra_fornecedor);
                    }

                    boolean result = produtoManager.atualizar(toUpdate, produto.getCod_barra());

                    if (result) {
                        Toast.makeText(AlterarDeletarProduto.this, "Produto Atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, null);
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

    private void setSpinnerMarca() {
        Cursor cursor = null, cursor2 = null;

        try {
            cursor = marcaManager.listarCursor();

            cursor2 = ManipulaCursor.retornaCursorComHintNull(cursor, "SELECIONE A MARCA", new String[]{"_id", "nome" });

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerMarca.setAdapter(simpleCursorAdapter);

            if(cursor != null && cursor.getCount() > 0 && produto.getMarca() != 0) {
                for(int i = 1; i < cursor2.getCount(); i++) {
                    cursor2.moveToPosition(i);

                    int id = cursor2.getInt(cursor2.getColumnIndexOrThrow("_id"));

                    if(id == produto.getMarca()) {
                        spinnerMarca.setSelection(i);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor != null)
                cursor.close();

            if(cursor2 != null)
                cursor2.close();
        }
    }
}