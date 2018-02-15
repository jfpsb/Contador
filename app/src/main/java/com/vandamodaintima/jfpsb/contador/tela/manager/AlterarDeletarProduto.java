package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

public class AlterarDeletarProduto extends AlterarDeletarEntidade {

    private Produto produto = new Produto();
    private Fornecedor fornecedor = new Fornecedor();
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedorAtual;
    private Spinner spinnerFornecedor;

    private String nomeFornecedor;

    private DAOProduto daoProduto;
    private DAOFornecedor daoFornecedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_alterar_deletar_produto);
        stub.inflate();

        produto = (Produto) getIntent().getExtras().getSerializable("produto");
        nomeFornecedor = getIntent().getExtras().getString("fornecedor");

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        txtFornecedorAtual = findViewById(R.id.txtFornecedorAtual);
        spinnerFornecedor = findViewById(R.id.spinnerFornecedor);
        btnAtualizar = findViewById(R.id.btnAtualizar);
        btnDeletar = findViewById(R.id.btnDeletar);

        txtCodBarra.setText(String.valueOf(produto.getCod_barra()));
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(String.valueOf(produto.getPreco()));
        txtFornecedorAtual.setText(nomeFornecedor);

        setAlertBuilder(produto.getCod_barra());

        daoFornecedor = new DAOFornecedor(conn.conexao());
        daoProduto = new DAOProduto(conn.conexao());

        setSpinnerFornecedor();

        setBtnAtualizar();

        setBtnDeletar();
    }

    @Override
    protected void setAlertBuilder(final Object cod_barra) {
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Deletar Produto");
        builder.setMessage("Tem certeza que deseja apagar o produto de código de barras " + cod_barra + "?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                daoProduto.deletar((int) cod_barra);

                PesquisarProduto.populaListView();

                finish();
            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarProduto.this, "Produto não foi deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerFornecedor() {
        Cursor spinnerCursor = daoFornecedor.selectFornecedores();

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerCursor, new String[] {"nome"}, new int[] {android.R.id.text1}, 0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFornecedor.setAdapter(simpleCursorAdapter);

        for(int i = 0; i < spinnerCursor.getCount(); i++) {
            spinnerCursor.moveToPosition(i);

            String id = spinnerCursor.getString(spinnerCursor.getColumnIndexOrThrow("_id"));

            if(produto.getFornecedor().equals(id)) {
                spinnerFornecedor.setSelection(i);
                break;
            }
        }

        spinnerFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor c = (Cursor) adapterView.getItemAtPosition(i);

                c.moveToPosition(i);

                fornecedor.setCnpj(c.getString(c.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(c.getString(c.getColumnIndexOrThrow("nome")));
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
                try {
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();

                    if(TestaIO.isStringEmpty(descricao))
                        throw new Exception("O campo de descrição não pode ficar vazio!");

                    if(TestaIO.isStringEmpty(preco))
                        throw new Exception("O campo de preço não pode ficar vazio!");

                    if(!TestaIO.isValidDouble(preco))
                        throw new Exception("O valor no campo preço é inválido!");

                    produto.setDescricao(descricao);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.setFornecedor(fornecedor.getCnpj());

                    int result = daoProduto.atualizar(produto);

                    if(result != -1) {
                        Toast.makeText(AlterarDeletarProduto.this, "Produto com código de barras " + produto.getCod_barra() + " atualizado com sucesso!", Toast.LENGTH_SHORT).show();

                        PesquisarProduto.populaListView();
                    }

                }catch (Exception e) {
                    Toast.makeText(AlterarDeletarProduto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void setBtnDeletar() {
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
