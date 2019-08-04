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

    private FornecedorModel fornecedor;
    private MarcaModel marca;

    AlterarDeletarProdutoController alterarDeletarProdutoController;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;
    private static final int TELA_COD_BARRA_FORNECEDOR = 3;

    private AlertDialog.Builder alertaRemoverFornecedor;
    private AlertDialog.Builder alertaRemoverMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_produto);
        stub.inflate();

        produtoModel = (ProdutoModel) getIntent().getExtras().getSerializable("produtoModel");

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtMarca = findViewById(R.id.txtMarca);

        inicializaBotoes();

        alterarDeletarProdutoController = new AlterarDeletarProdutoController(this);

        txtCodBarra.setText(produtoModel.getCod_barra());
        txtDescricao.setText(produtoModel.getDescricao());
        txtPreco.setText(String.valueOf(produtoModel.getPreco()));

        if (produtoModel.getFornecedor() != null) {
            txtFornecedor.setText(produtoModel.getFornecedor().getNome());
            fornecedor = produtoModel.getFornecedor();
        }

        if (produtoModel.getMarca() != null) {
            txtMarca.setText(produtoModel.getMarca().getNome());
            marca = produtoModel.getMarca();
        }

        setAlertaRemoverMarca();
        setAlertaRemoverFornecedor();

        setBtnEscolherFornecedor();
        setBtnEscolherMarca();
        setBtnRemoverFornecedor();
        setBtnRemoverMarca();
        setBtnGerenciarCodBarraFornecedor();
    }

    private void setBtnEscolherFornecedor() {
        btnEscolherFornecedor = findViewById(R.id.btnEscolherFornecedor);
        btnEscolherFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaFornecedorForResult.class);
                startActivityForResult(intent, ESCOLHER_FORNECEDOR);
            }
        });
    }

    private void setBtnEscolherMarca() {
        btnEscolherMarca = findViewById(R.id.btnEscolherMarca);
        btnEscolherMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaMarcaForResult.class);
                startActivityForResult(intent, ESCOLHER_MARCA);
            }
        });
    }

    private void setBtnGerenciarCodBarraFornecedor() {
        btnGerenciarCodBarraFornecedor = findViewById(R.id.btnGerenciarCodBarraFornecedor);
        btnGerenciarCodBarraFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlterarDeletarProduto.this, TelaCodBarraFornecedor.class);
                intent.putExtra("produto", produtoModel);

                startActivityForResult(intent, TELA_COD_BARRA_FORNECEDOR);
            }
        });
    }

    private void setBtnRemoverFornecedor() {
        btnRemoverFornecedor = findViewById(R.id.btnRemoverFornecedor);
        btnRemoverFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fornecedor != null) {
                    alertaRemoverFornecedor.show();
                } else {
                    mensagemAoUsuario("Produto Não Possui Fornecedor");
                }
            }
        });
    }

    private void setBtnRemoverMarca() {
        btnRemoverMarca = findViewById(R.id.btnRemoverMarca);
        btnRemoverMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (marca != null) {
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
                    fornecedor = (FornecedorModel) data.getSerializableExtra("fornecedor");

                    if (fornecedor != null) {
                        txtFornecedor.setText(fornecedor.getNome());
                        mensagemAoUsuario("Fornecedor Escolhido. Aperte em \"Atualizar\" para Salvar.");
                    }
                } else {
                    mensagemAoUsuario("Fornecedor Não Foi Escolhido");
                }
                break;
            case ESCOLHER_MARCA:
                if (resultCode == RESULT_OK) {
                    marca = (MarcaModel) data.getSerializableExtra("marca");

                    if (marca != null) {
                        txtMarca.setText(marca.getNome());
                        mensagemAoUsuario("Marca Escolhida. Aperte em \"Atualizar\" para Salvar");
                    }
                } else {
                    mensagemAoUsuario("Marca Não Foi Escolhida");
                }
                break;
            case TELA_COD_BARRA_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    ProdutoModel produtoAlterado = (ProdutoModel) data.getSerializableExtra("produto");

                    if (produtoAlterado.getCod_barra_fornecedor().equals(produtoModel.getCod_barra_fornecedor())) {
                        mensagemAoUsuario("Cód. de Barras de Fornecedores Não Foram Alterados");
                    } else {
                        produtoModel.setCod_barra_fornecedor(produtoAlterado.getCod_barra_fornecedor());
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
                produtoModel.setFornecedor(fornecedor);
                produtoModel.setMarca(marca);
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
                fornecedor = null;
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
                marca = null;
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