package com.vandamodaintima.jfpsb.contador.view.loja;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.loja.AlterarDeletarLojaController;
import com.vandamodaintima.jfpsb.contador.controller.loja.SpinnerLojaAdapter;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

public class AlterarDeletarLoja extends TelaAlterarDeletar {

    private EditText txtCnpj;
    private EditText txtNome;
    private EditText txtTelefone;
    private EditText txtEndereco;
    private EditText txtInscricaoEstadual;
    private Spinner spinnerMatrizes;
    AlterarDeletarLojaController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_loja);
        stub.inflate();

        setBtnAtualizar();

        txtCnpj = findViewById(R.id.txtCnpj);
        txtNome = findViewById(R.id.txtNome);
        spinnerMatrizes = findViewById(R.id.spinnerMatrizes);
        navigationView.inflateMenu(R.menu.menu_alterar_deletar_loja);
        navigationView.inflateHeaderView(R.layout.nav_alterar_deletar_loja);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AlterarDeletarLojaController(this, conexaoBanco);

        String id = getIntent().getStringExtra("loja");

        controller.carregaLoja(id);

        setAlertBuilderDeletar();
        setAlertBuilderAtualizar();

        txtCnpj.setText(controller.getLoja().getCnpj());
        txtNome.setText(controller.getLoja().getNome());
        ArrayAdapter spinnerAdapter = new SpinnerLojaAdapter(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, android.R.id.text1, controller.getMatrizes());
        spinnerMatrizes.setAdapter(spinnerAdapter);

        if (controller.getLoja().getMatriz() != null)
            spinnerMatrizes.setSelection(controller.getMatrizIndex(controller.getLoja().getMatriz().getCnpj()));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {
                    case R.id.menuItemDeletar:
                        AlertDialog alertDialog = alertBuilderDeletar.create();
                        alertDialog.show();
                        break;
                    case R.id.menuItemVisualizarEstoque:
                        break;
                    case R.id.menuItemExportarEstoque:
                        break;
                }

                return true;
            }
        });
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Loja");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Apagar a Loja " + controller.getLoja().getNome() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.deletar();
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarLoja.this, "Loja Não Foi Deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Loja");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Loja " + controller.getLoja().getNome() + "?");

        alertBuilderAtualizar.setPositiveButton("Sim", (dialog, which) -> {
            try {
                controller.getLoja().setNome(txtNome.getText().toString().trim().toUpperCase());
                controller.getLoja().setTelefone(txtTelefone.getText().toString().trim());
                controller.getLoja().setEndereco(txtEndereco.getText().toString().trim());
                controller.getLoja().setInscricaoEstadual(txtInscricaoEstadual.getText().toString().trim());
                controller.carregaMatriz(spinnerMatrizes.getSelectedItem());
                controller.atualizar();
            } catch (Exception e) {
                Toast.makeText(AlterarDeletarLoja.this, "Erro ao Atualizar Loja: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(LOG, e.getMessage(), e);
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AlterarDeletarLoja.this, "A Loja Não Foi Alterada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
