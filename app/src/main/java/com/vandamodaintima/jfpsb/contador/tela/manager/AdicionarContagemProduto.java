package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem_Produto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

public class AdicionarContagemProduto extends ActivityBase {

    DAOProduto daoProduto;
    DAOContagemProduto daoContagemProduto;
    private EditText txtCodBarra;
    private EditText txtFornecedor;
    private EditText txtPreco;
    private EditText txtQuantidade;
    private EditText txtDescricao;
    private Button btnAdicionar;
    private Button btnAddFornecedor;
    private Button btnLimparFornecedor;

    private Fornecedor fornecedor = new Fornecedor();
    private Produto produto;
    private Contagem contagem;
    private Contagem_Produto contagem_produto = new Contagem_Produto();
    private Boolean addFornecedorFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewStub stub = findViewById(R.id.layoutStub);
        stub.setLayoutResource(R.layout.content_adicionar_contagem_produto);
        stub.inflate();

        setDAOs();

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");
        produto = (Produto) getIntent().getExtras().getSerializable("produto");
        String nomeFornecedor = getIntent().getExtras().getString("fornecedor");

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtPreco = findViewById(R.id.txtPreco);
        txtQuantidade = findViewById(R.id.txtQuantidade);
        txtDescricao = findViewById(R.id.txtDescricao);
        btnAdicionar = findViewById(R.id.btnAdicionar);
        btnAddFornecedor = findViewById(R.id.btnAddFornecedor);
        btnLimparFornecedor = findViewById(R.id.btnLimparFornecedor);

        txtCodBarra.setText(String.valueOf(produto.getCod_barra()));

        if(nomeFornecedor != null) {
            txtFornecedor.setText(nomeFornecedor);
        } else {
            txtFornecedor.setText("Não Possui");
        }

        txtPreco.setText(String.valueOf(produto.getPreco()));
        txtDescricao.setText(produto.getDescricao());

        setBtnAdicionar();

        setBtnAddFornecedor();

        setBtnLimparFornecedor();
    }

    @Override
    protected void setDAOs() {
        daoContagemProduto = new DAOContagemProduto(conn.conexao());
        daoProduto = new DAOProduto(conn.conexao());
    }

    private void setBtnAdicionar() {
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String quant = txtQuantidade.getText().toString();

                    if(TestaIO.isStringEmpty(quant))
                        throw new Exception("O campo de quantidade não pode estar vazio!");

                    if(!TestaIO.isValidInt(quant))
                        throw new Exception("O valor em quantidade é inválido!");

                    contagem_produto.setContagem(contagem.getIdcontagem());
                    contagem_produto.setProduto(produto.getCod_barra());
                    contagem_produto.setQuant(Integer.parseInt(quant));

                    long result = daoContagemProduto.inserir(contagem_produto);

                    if(result != -1) {
                        Toast.makeText(AdicionarContagemProduto.this, "Contagem de produto inserida com sucesso!", Toast.LENGTH_SHORT).show();

                        if(addFornecedorFlag) {

                            produto.setFornecedor(fornecedor.getCnpj());

                            long resultProduto = daoProduto.atualizar(produto);

                            if(resultProduto != -1) {
                                Toast.makeText(AdicionarContagemProduto.this, "Fornecedor de produto atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdicionarContagemProduto.this, "Houve um erro ao atualizar fornecedor de produto", Toast.LENGTH_SHORT).show();
                            }
                        }

                        finish();
                    }
                    else {
                        Toast.makeText(AdicionarContagemProduto.this, "Erro ao Inserir Contagem!", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e) {
                    Toast.makeText(AdicionarContagemProduto.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBtnAddFornecedor() {
        btnAddFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addFornecedor = new Intent(AdicionarContagemProduto.this, AdicionarFornecedor.class);

                startActivityForResult(addFornecedor, 1);
            }
        });
    }

    private void setBtnLimparFornecedor() {
        btnLimparFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fornecedor.getCnpj() != null) {
                    fornecedor.setCnpj(null);
                    fornecedor.setNome(null);

                    txtFornecedor.setText("Removido Pelo Usuário");

                    addFornecedorFlag = true;

                    Toast.makeText(AdicionarContagemProduto.this, "O fornecedor foi removido manualmente pelo usuário", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AdicionarContagemProduto.this, "O produto já não possui fornecedor. Adicione no botão acima", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");

                txtFornecedor.setText(fornecedor.getNome());

                addFornecedorFlag = true;
            }

            if(resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "O fornecedor não foi escolhido", Toast.LENGTH_SHORT).show();

                fornecedor.setCnpj(null);

                addFornecedorFlag = false;
            }
        }
    }
}
