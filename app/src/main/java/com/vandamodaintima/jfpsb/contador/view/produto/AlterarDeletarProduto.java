package com.vandamodaintima.jfpsb.contador.view.produto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.AlterarDeletarProdutoController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.view.marca.TelaMarcaForResult;
import com.vandamodaintima.jfpsb.contador.view.produto.grade.GerenciarProdutoGrades;

import java.io.Serializable;
import java.util.List;

public class AlterarDeletarProduto extends TelaAlterarDeletar {
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedor;
    private EditText txtMarca;
    private EditText txtNcm;
    private Button btnEscolherFornecedor;
    private Button btnEscolherMarca;
    private Button btnRemoverFornecedor;
    private Button btnRemoverMarca;
    private Button btnGerenciarGrades;
    private TextView txtQuantidadeGrades;

    AlterarDeletarProdutoController controller;

    private static final int ESCOLHER_FORNECEDOR = 1;
    private static final int ESCOLHER_MARCA = 2;
    private static final int GERENCIAR_GRADES = 3;

    private AlertDialog.Builder alertaRemoverFornecedor;
    private AlertDialog.Builder alertaRemoverMarca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AlterarDeletarProdutoController(this, conexaoBanco);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_produto);
        stub.inflate();

        setBtnAtualizar();

        txtCodBarra = findViewById(R.id.txtCodBarra);
        txtDescricao = findViewById(R.id.txtDescricaoGrade);
        txtPreco = findViewById(R.id.txtPrecoVenda);
        txtFornecedor = findViewById(R.id.txtFornecedor);
        txtMarca = findViewById(R.id.txtMarca);
        txtNcm = findViewById(R.id.txtNcm);
        btnEscolherFornecedor = findViewById(R.id.btnEscolherFornecedor);
        btnEscolherMarca = findViewById(R.id.btnEscolherMarca);
        btnRemoverFornecedor = findViewById(R.id.btnRemoverFornecedor);
        btnRemoverMarca = findViewById(R.id.btnRemoverMarca);
        btnGerenciarGrades = findViewById(R.id.btnGerenciarGrades);
        txtQuantidadeGrades = findViewById(R.id.txtQuantidadeGrades);

        navigationView.inflateMenu(R.menu.menu_alterar_deletar_produto);
        navigationView.inflateHeaderView(R.layout.nav_alterar_deletar_produto);

        String id = getIntent().getStringExtra("produto");
        controller.carregaProduto(id);

        setAlertaRemoverFornecedor();
        setAlertaRemoverMarca();
        setAlertBuilderDeletar();
        setAlertBuilderAtualizar();

        txtCodBarra.setText(controller.getProduto().getCodBarra());
        txtDescricao.setText(controller.getProduto().getDescricao());
        txtNcm.setText(controller.getProduto().getNcm());
        txtQuantidadeGrades.setText(String.valueOf(controller.getProduto().getProdutoGrades().size()));

        if (controller.getProduto().getFornecedor() != null) {
            txtFornecedor.setText(controller.getProduto().getFornecedor().getNome());
        }

        if (controller.getProduto().getMarca() != null) {
            txtMarca.setText(controller.getProduto().getMarca().getNome());
        }

        btnGerenciarGrades.setOnClickListener(v -> {
            Intent intent = new Intent(this, GerenciarProdutoGrades.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("produtoGrades", (Serializable) controller.getProduto().getProdutoGrades());
            intent.putExtras(bundle);
            startActivityForResult(intent, GERENCIAR_GRADES);
        });

        btnEscolherFornecedor.setOnClickListener(v -> {
            Intent intent = new Intent(AlterarDeletarProduto.this, TelaFornecedorForResult.class);
            startActivityForResult(intent, ESCOLHER_FORNECEDOR);
        });

        btnEscolherMarca.setOnClickListener(v -> {
            Intent intent = new Intent(AlterarDeletarProduto.this, TelaMarcaForResult.class);
            startActivityForResult(intent, ESCOLHER_MARCA);
        });

        btnRemoverFornecedor.setOnClickListener(v -> {
            if (controller.getProduto().getFornecedor() != null) {
                alertaRemoverFornecedor.show();
            } else {
                mensagemAoUsuario("Produto Não Possui Fornecedor");
            }
        });

        btnRemoverMarca.setOnClickListener(v -> {
            if (controller.getProduto().getMarca() != null) {
                alertaRemoverMarca.show();
            } else {
                mensagemAoUsuario("Produto Não Possui Marca");
            }
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id1 = menuItem.getItemId();

            if (id1 == R.id.menuItemDeletar) {
                AlertDialog alertDialog = alertBuilderDeletar.create();
                alertDialog.show();
            }

            return true;
        });
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == RESULT_OK) {
                    Fornecedor fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    controller.carregaFornecedor(fornecedor);
                    txtFornecedor.setText(controller.getProduto().getFornecedor().getNome());
                    mensagemAoUsuario("Fornecedor Escolhido. Aperte em \"Atualizar\" para Salvar.");
                } else {
                    mensagemAoUsuario("Fornecedor Não Foi Escolhido");
                }
                break;
            case ESCOLHER_MARCA:
                if (resultCode == RESULT_OK) {
                    Marca marca = (Marca) data.getSerializableExtra("marca");
                    controller.carregaMarca(marca);
                    txtMarca.setText(controller.getProduto().getMarca().getNome());
                    mensagemAoUsuario("Marca Escolhida. Aperte em \"Atualizar\" para Salvar");
                } else {
                    mensagemAoUsuario("Marca Não Foi Escolhida");
                }
                break;
            case GERENCIAR_GRADES:
                Bundle bundle = data.getExtras();
                controller.getProduto().getProdutoGrades().clear();
                controller.getProduto().getProdutoGrades().addAll((List<ProdutoGrade>) bundle.getSerializable("produtoGrades"));
                txtQuantidadeGrades.setText(String.valueOf(controller.getProduto().getProdutoGrades().size()));
                mensagemAoUsuario("Grades Serão Salvas ao Salvar Produto");
                break;
        }
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Produto");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Produto " + controller.getProduto().getCodBarra() + " - " + controller.getProduto().getDescricao() + "?");
        alertBuilderDeletar.setPositiveButton("Sim", (dialogInterface, i) -> controller.deletar());
        alertBuilderDeletar.setNegativeButton("Não", (dialogInterface, i) -> Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Deletado", Toast.LENGTH_SHORT).show());
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Produto");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Produto " + controller.getProduto().getCodBarra() + " - " + controller.getProduto().getDescricao() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", (dialog, which) -> {
            controller.getProduto().setDescricao(txtDescricao.getText().toString());
            controller.getProduto().setNcm(txtNcm.getText().toString());
            controller.atualizar();
        });

        alertBuilderAtualizar.setNegativeButton("Não", (dialog, which) -> Toast.makeText(AlterarDeletarProduto.this, "Produto Não foi Alterado", Toast.LENGTH_SHORT).show());
    }

    private void setAlertaRemoverFornecedor() {
        alertaRemoverFornecedor = new AlertDialog.Builder(this);
        alertaRemoverFornecedor.setTitle("Remover Fornecedor");
        alertaRemoverFornecedor.setMessage("Tem Certeza Que Deseja Remover o Fornecedor Deste Produto?");

        alertaRemoverFornecedor.setPositiveButton("Sim", (dialog, which) -> {
            controller.setFornecedorNull();
            txtFornecedor.getText().clear();
            Toast.makeText(AlterarDeletarProduto.this, "Fornecedor Removido", Toast.LENGTH_SHORT).show();
        });

        alertaRemoverFornecedor.setNegativeButton("Não", (dialog, which) -> Toast.makeText(AlterarDeletarProduto.this, "Fornecedor Não Foi Removido", Toast.LENGTH_SHORT).show());
    }

    private void setAlertaRemoverMarca() {
        alertaRemoverMarca = new AlertDialog.Builder(this);
        alertaRemoverMarca.setTitle("Remover Marca");
        alertaRemoverMarca.setMessage("Tem Certeza Que Deseja Remover a Marca Deste Produto?");

        alertaRemoverMarca.setPositiveButton("Sim", (dialog, which) -> {
            controller.setMarcaNull();
            txtMarca.getText().clear();
            Toast.makeText(AlterarDeletarProduto.this, "Marca Removida", Toast.LENGTH_SHORT).show();
        });

        alertaRemoverMarca.setNegativeButton("Não", (dialog, which) -> Toast.makeText(AlterarDeletarProduto.this, "Marca Não Foi Removida", Toast.LENGTH_SHORT).show());
    }
}