package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
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
    private Spinner spinnerFornecedor;

    private ProdutoManager produtoManager;
    private FornecedorManager fornecedorManager;

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
        spinnerFornecedor = findViewById(R.id.spinnerFornecedor);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

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

        setSpinnerFornecedor();

        setBtnAtualizar();

        setBtnDeletar();
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(conn);
        produtoManager = new ProdutoManager(conn);
    }

    private void setSpinnerFornecedor() {
        Cursor spinnerCursor = null, spinnerCursor2 = null;

        try {
            spinnerCursor = fornecedorManager.listarCursor();

            // Cursor que deve ser utilizado, pois possui o primeiro elemento como hint
            spinnerCursor2 = ManipulaCursor.retornaCursorComHintNull(spinnerCursor, "SELECIONE O FORNECEDOR", new String[]{"_id", "nome", "cnpj"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFornecedor.setAdapter(simpleCursorAdapter);

            if (produto.getFornecedor() != null) {
                for (int i = 0; i < spinnerCursor2.getCount(); i++) {
                    spinnerCursor2.moveToPosition(i);

                    int id = spinnerCursor2.getInt(spinnerCursor.getColumnIndexOrThrow("_id"));

                    if (produto.getFornecedor().getId() == id) {
                        spinnerFornecedor.setSelection(i);
                        break;
                    }
                }
            } else {
                spinnerFornecedor.setSelection(0);
            }
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(spinnerCursor != null)
                spinnerCursor.close();

            if(spinnerCursor2 != null)
                spinnerCursor2.close();
        }

        spinnerFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    if (i != 0) {
                        Cursor c = (Cursor) adapterView.getItemAtPosition(i);

                        c.moveToPosition(i);

                        fornecedor = new Fornecedor();
                        fornecedor.setId(c.getInt(c.getColumnIndexOrThrow("_id")));
                        fornecedor.setCnpj(c.getString(c.getColumnIndexOrThrow("cnpj")));
                        fornecedor.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
                    } else {
                        fornecedor = null;
                    }
                }
                catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                    Toast.makeText(AlterarDeletarProduto.this, "Erro ao Escolher Fornecedor. Contate Suporte Caso Persista", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = AlertBuilderAtualizar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = AlertBuilderDeletar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void setAlertBuilderDeletar() {
        AlertBuilderDeletar = new AlertDialog.Builder(this);
        AlertBuilderDeletar.setTitle("Deletar Produto");
        AlertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Produto " + produto.getCod_barra() + " - " + produto.getDescricao() + "?");

        AlertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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

        AlertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void setAlertBuilderAtualizar() {
        AlertBuilderAtualizar = new AlertDialog.Builder(this);
        AlertBuilderAtualizar.setTitle("Atualizar Produto");
        AlertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto" + produto.getCod_barra() + " - " + produto.getDescricao() + "?");

        AlertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
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

        AlertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Alterado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}