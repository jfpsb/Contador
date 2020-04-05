package com.vandamodaintima.jfpsb.contador.view.fornecedor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.AlterarDeletarFornecedorController;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.IAposPesquisarFornecedor;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

public class AlterarDeletarFornecedor extends TelaAlterarDeletar implements IAposPesquisarFornecedor {

    private EditText txtCnpj;
    private EditText txtNome;
    private EditText txtFantasia;
    private EditText txtEmail;
    private EditText txtTelefone;

    private AlterarDeletarFornecedorController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_fornecedor);
        stub.inflate();

        setBtnAtualizar();

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        txtFantasia = findViewById(R.id.txtFantasia);
        txtEmail = findViewById(R.id.txtEmail);
        txtTelefone = findViewById(R.id.txtTelefone);

        navigationView.inflateMenu(R.menu.menu_alterar_deletar_fornecedor);
        navigationView.inflateHeaderView(R.layout.nav_alterar_deletar_fornecedor);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AlterarDeletarFornecedorController(this, conexaoBanco);

        String id = getIntent().getStringExtra("fornecedor");
        controller.carregaFornecedor(id);

        setAlertBuilderAtualizar();
        setAlertBuilderDeletar();

        txtCnpj.setText(controller.getFornecedor().getCnpj());
        txtNome.setText(controller.getFornecedor().getNome());
        txtFantasia.setText(controller.getFornecedor().getFantasia());
        txtEmail.setText(controller.getFornecedor().getEmail());
        txtTelefone.setText(controller.getFornecedor().getTelefone());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.menuItemDeletar:
                        AlertDialog alertDialog = alertBuilderDeletar.create();
                        alertDialog.show();
                        break;
                    case R.id.menuItemAtualizarDados:
                        controller.pesquisarNaReceita(controller.getFornecedor().getCnpj());
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Fornecedor");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar o Fornecedor " + controller.getFornecedor().getCnpj() + " - " + controller.getFornecedor().getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", (dialog, which) -> {
            try {
                controller.getFornecedor().setCnpj(txtCnpj.getText().toString());
                controller.getFornecedor().setNome(txtNome.getText().toString().toUpperCase());
                controller.getFornecedor().setFantasia(txtFantasia.getText().toString().toUpperCase());
                controller.getFornecedor().setEmail(txtEmail.getText().toString().toUpperCase());
                controller.getFornecedor().setTelefone(txtTelefone.getText().toString());

                controller.atualizar();
            } catch (Exception e) {
                Toast.makeText(AlterarDeletarFornecedor.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Não Foi Atualizado", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void aposPesquisarFornecedor(Fornecedor fornecedor) {
        txtNome.setText(fornecedor.getNome());
        txtFantasia.setText(fornecedor.getFantasia());
        txtEmail.setText(fornecedor.getEmail());
        txtTelefone.setText(fornecedor.getTelefone());
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Fornecedor");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Deletar o Fornecedor " + controller.getFornecedor().getCnpj() + " - " + controller.getFornecedor().getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.deletar();
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarFornecedor.this, "Fornecedor Não Foi Deletado", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
