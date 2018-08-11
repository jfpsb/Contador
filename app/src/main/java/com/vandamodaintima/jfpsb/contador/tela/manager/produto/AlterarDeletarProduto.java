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
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;
import com.vandamodaintima.jfpsb.contador.tela.manager.codbarrafornecedor.TelaCodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.tela.manager.marca.TelaMarcaForResult;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

import java.util.ArrayList;

public class AlterarDeletarProduto extends AlterarDeletarEntidade<Produto> {

    private Produto produto = new Produto();
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

    private Fornecedor fornecedor;
    private Marca marca;

    private ProdutoManager produtoManager;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;
    private static final int TELA_COD_BARRA_FORNECEDOR = 3;

    private AlertDialog.Builder alertaRemoverFornecedor;
    private AlertDialog.Builder alertaRemoverMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.content_alterar_deletar_produto);

        produto = (Produto) getIntent().getExtras().getSerializable("produto");

        savedInstanceState.putString("entidade", "produto");
        savedInstanceState.putSerializable("produto", produto);

        super.onCreate(savedInstanceState);

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtPreco = findViewById(R.id.txtPreco);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtMarca = findViewById(R.id.txtMarca);

        txtCodBarra.setText(produto.getCod_barra());
        txtDescricao.setText(produto.getDescricao());
        txtPreco.setText(String.valueOf(produto.getPreco()));

        if (produto.getFornecedor() != null) {
            txtFornecedor.setText(produto.getFornecedor().getNome());
            fornecedor = produto.getFornecedor();
        }

        if (produto.getMarca() != null) {
            txtMarca.setText(produto.getMarca().getNome());
            marca = produto.getMarca();
        }

        setAlertaRemoverFornecedor();
        setAlertaRemoverMarca();

        setBtnEscolherFornecedor();
        setBtnEscolherMarca();
        setBtnRemoverFornecedor();
        setBtnRemoverMarca();
        setBtnGerenciarCodBarraFornecedor();
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(conn);
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
                Intent intent = new Intent(getApplicationContext(), TelaCodBarraFornecedor.class);
                intent.putExtra("produto", produto);
                startActivityForResult(intent, TELA_COD_BARRA_FORNECEDOR);
            }
        });
    }

    private void setBtnRemoverFornecedor() {
        btnRemoverFornecedor = findViewById(R.id.btnRemoverFornecedor);
        btnRemoverFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fornecedor != null) {
                    alertaRemoverFornecedor.show();
                } else {
                    Toast.makeText(AlterarDeletarProduto.this, "Produto Não Possui Fornecedor", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setBtnRemoverMarca() {
        btnRemoverMarca = findViewById(R.id.btnRemoverMarca);
        btnRemoverMarca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(marca != null) {
                    alertaRemoverMarca.show();
                } else {
                    Toast.makeText(AlterarDeletarProduto.this, "Produto Não Possui Marca", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");

                    if (fornecedor != null) {
                        txtFornecedor.setText(fornecedor.getNome());
                        Toast.makeText(this, "Fornecedor Escolhido. Aperte em \"Atualizar\" para Salvar.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Fornecedor Não Foi Escolhido", Toast.LENGTH_SHORT).show();
                }
                break;
            case ESCOLHER_MARCA:
                if (resultCode == RESULT_OK) {
                    marca = (Marca) data.getSerializableExtra("marca");

                    if (marca != null) {
                        txtMarca.setText(marca.getNome());
                        Toast.makeText(this, "Marca Escolhida. Aperte em \"Atualizar\" para Salvar", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Marca Não Foi Escolhida", Toast.LENGTH_SHORT).show();
                }
                break;
            case TELA_COD_BARRA_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    ArrayList<CodBarraFornecedor> codigos = (ArrayList<CodBarraFornecedor>) data.getSerializableExtra("codigos");

                    if (codigos.equals(produto.getCod_barra_fornecedor())) {
                        Toast.makeText(this, "Cód. de Barras de Fornecedores Não Foram Alterados", Toast.LENGTH_SHORT).show();
                    } else {
                        produto.setCod_barra_fornecedor(codigos);
                        Toast.makeText(this, "A Lista de Códigos Será Consolidada ao Apertar em \"Atualizar\"", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    @Override
    protected void setAlertBuilderDeletar(final Produto entidade) {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Produto");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Produto " + entidade.getCod_barra() + " - " + entidade.getDescricao() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = produtoManager.deletar(entidade.getCod_barra());

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
    protected void setAlertBuilderAtualizar(final Produto entidade) {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Produto");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto " + entidade.getCod_barra() + " - " + entidade.getDescricao() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    String cod_barra = txtCodBarra.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();

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
                    toUpdate.setMarca(marca);
                    toUpdate.setCod_barra_fornecedor(produto.getCod_barra_fornecedor());

                    boolean result = produtoManager.atualizar(toUpdate, entidade.getCod_barra());

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