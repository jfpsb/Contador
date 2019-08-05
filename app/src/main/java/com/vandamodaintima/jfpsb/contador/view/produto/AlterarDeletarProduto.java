package com.vandamodaintima.jfpsb.contador.view.produto;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.AlterarDeletarProdutoController;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;
import com.vandamodaintima.jfpsb.contador.view.codbarrafornecedor.TelaCodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.view.marca.TelaMarcaForResult;

import java.util.ArrayList;

public class AlterarDeletarProduto extends TelaAlterarDeletar {

    private ProdutoModel produtoModel;
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedor;
    private EditText txtMarca;
    private Button btnEscolherFornecedor;
    private Button btnEscolherMarca;
    private Button btnGerenciarCodBarraFornecedor;
    private Button btnRemoverFornecedor;
    private Button btnRemoverMarca;

    private FornecedorModel fornecedorModel;
    private MarcaModel marcaModel;

    AlterarDeletarProdutoController alterarDeletarProdutoController;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;
    private static final int TELA_COD_BARRA_FORNECEDOR = 3;

    private AlertDialog.Builder alertaRemoverFornecedor;
    private AlertDialog.Builder alertaRemoverMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conexaoBanco = new ConexaoBanco(getApplicationContext());

        stub.setLayoutResource(R.layout.activity_alterar_deletar_produto);
        stub.inflate();

        String id = getIntent().getExtras().getString("produto");

        produtoModel = new ProdutoModel(conexaoBanco);
        fornecedorModel = new FornecedorModel(conexaoBanco);
        marcaModel = new MarcaModel(conexaoBanco);

        //Carrega dados de produto
        produtoModel.load(id);

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtMarca = findViewById(R.id.txtMarca);

        alterarDeletarProdutoController = new AlterarDeletarProdutoController(this);

        txtCodBarra.setText(produtoModel.getCod_barra());
        txtDescricao.setText(produtoModel.getDescricao());
        txtPreco.setText(String.valueOf(produtoModel.getPreco()));

        if (produtoModel.getFornecedor() != null) {
            txtFornecedor.setText(produtoModel.getFornecedor().getNome());
            fornecedorModel = produtoModel.getFornecedor();
        }

        if (produtoModel.getMarca() != null) {
            txtMarca.setText(produtoModel.getMarca().getNome());
            marcaModel = produtoModel.getMarca();
        }
    }

    @Override
    public void inicializaBotoes() {
        super.inicializaBotoes();

        setAlertaRemoverMarca();
        setAlertaRemoverFornecedor();

        btnEscolherFornecedor = findViewById(R.id.btnEscolherFornecedor);
        btnEscolherMarca = findViewById(R.id.btnEscolherMarca);
        btnGerenciarCodBarraFornecedor = findViewById(R.id.btnGerenciarCodBarraFornecedor);
        btnRemoverFornecedor = findViewById(R.id.btnRemoverFornecedor);
        btnRemoverMarca = findViewById(R.id.btnRemoverMarca);

        btnEscolherFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaFornecedorForResult.class);
                startActivityForResult(intent, ESCOLHER_FORNECEDOR);
            }
        });

        btnEscolherMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaMarcaForResult.class);
                startActivityForResult(intent, ESCOLHER_MARCA);
            }
        });

        btnGerenciarCodBarraFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaCodBarraFornecedor.class);
                intent.putExtra("codigos", produtoModel.getCod_barra_fornecedor());
                startActivityForResult(intent, TELA_COD_BARRA_FORNECEDOR);
            }
        });

        btnRemoverFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!fornecedorModel.getCnpj().isEmpty()) {
                    alertaRemoverFornecedor.show();
                } else {
                    mensagemAoUsuario("Produto Não Possui Fornecedor");
                }
            }
        });

        btnRemoverMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!marcaModel.getNome().isEmpty()) {
                    alertaRemoverMarca.show();
                } else {
                    mensagemAoUsuario("Produto Não Possui Marca");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    String id = data.getStringExtra("fornecedor");
                    fornecedorModel.load(id);
                    txtFornecedor.setText(fornecedorModel.getNome());
                    mensagemAoUsuario("Fornecedor Escolhido. Aperte em \"Atualizar\" para Salvar.");
                } else {
                    mensagemAoUsuario("Fornecedor Não Foi Escolhido");
                }
                break;
            case ESCOLHER_MARCA:
                if (resultCode == RESULT_OK) {
                    String id = data.getStringExtra("marca");
                    marcaModel.load(id);
                    txtMarca.setText(marcaModel.getNome());
                    mensagemAoUsuario("Marca Escolhida. Aperte em \"Atualizar\" para Salvar");
                } else {
                    mensagemAoUsuario("Marca Não Foi Escolhida");
                }
                break;
            case TELA_COD_BARRA_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> codigos = (ArrayList<String>) data.getSerializableExtra("codigos");

                    if (produtoModel.getCod_barra_fornecedor().equals(codigos)) {
                        mensagemAoUsuario("Cód. de Barras de Fornecedores Não Foram Alterados");
                    } else {
                        produtoModel.setCod_barra_fornecedor(codigos);
                        mensagemAoUsuario("A Lista de Códigos Será Consolidada ao Apertar em \"Atualizar\"");
                    }
                }
                break;
        }
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Produto");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Produto " + produtoModel.getCod_barra() + " - " + produtoModel.getDescricao() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alterarDeletarProdutoController.deletar(produtoModel);
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
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Produto");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto " + produtoModel.getCod_barra() + " - " + produtoModel.getDescricao() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                produtoModel.setDescricao(txtDescricao.getText().toString().toUpperCase());
                produtoModel.setFornecedor(fornecedorModel);
                produtoModel.setMarca(marcaModel);
                produtoModel.setPreco(Double.parseDouble(txtPreco.getText().toString()));

                alterarDeletarProdutoController.atualizar(produtoModel);
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Alterado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlertaRemoverFornecedor() {
        alertaRemoverFornecedor = new AlertDialog.Builder(this);
        alertaRemoverFornecedor.setTitle("Remover Fornecedor");
        alertaRemoverFornecedor.setMessage("Tem Certeza Que Deseja Remover o Fornecedor Deste Produto?");

        alertaRemoverFornecedor.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fornecedorModel = new FornecedorModel(conexaoBanco);
                txtFornecedor.getText().clear();
                Toast.makeText(AlterarDeletarProduto.this, "Fornecedor Removido", Toast.LENGTH_SHORT).show();
            }
        });

        alertaRemoverFornecedor.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarProduto.this, "Fornecedor Não Foi Removido", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setAlertaRemoverMarca() {
        alertaRemoverMarca = new AlertDialog.Builder(this);
        alertaRemoverMarca.setTitle("Remover Marca");
        alertaRemoverMarca.setMessage("Tem Certeza Que Deseja Remover a Marca Deste Produto?");

        alertaRemoverMarca.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                marcaModel = new MarcaModel(conexaoBanco);
                txtMarca.getText().clear();
                Toast.makeText(AlterarDeletarProduto.this, "Marca Removida", Toast.LENGTH_SHORT).show();
            }
        });

        alertaRemoverMarca.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarProduto.this, "Marca Não Foi Removida", Toast.LENGTH_SHORT).show();
            }
        });
    }
}