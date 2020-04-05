package com.vandamodaintima.jfpsb.contador.view.marca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.marca.AlterarDeletarMarcaController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedorForResult;

public class AlterarDeletarMarca extends TelaAlterarDeletar {
    private EditText txtNome;
    private Button btnEscolherFornecedor;
    private Button btnRemoverFornecedor;
    private EditText txtFornecedor;
    private AlterarDeletarMarcaController controller;

    private static final int ESCOLHER_FORNECEDOR = 1;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_marca);
        stub.inflate();

        setBtnAtualizar();

        txtNome = findViewById(R.id.txtNome);
        btnEscolherFornecedor = findViewById(R.id.btnEscolherFornecedor);
        btnRemoverFornecedor = findViewById(R.id.btnRemoverFornecedor);
        txtFornecedor = findViewById(R.id.txtFornecedor);

        navigationView.inflateMenu(R.menu.menu_alterar_deletar_marca);
        navigationView.inflateHeaderView(R.layout.nav_alterar_deletar_marca);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AlterarDeletarMarcaController(this, conexaoBanco);

        String id = getIntent().getStringExtra("marca");

        controller.carregaMarca(id);

        setAlertBuilderAtualizar();
        setAlertBuilderDeletar();

        txtNome.setText(controller.getMarca().getNome());

        btnEscolherFornecedor.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TelaFornecedorForResult.class);
            startActivityForResult(intent, ESCOLHER_FORNECEDOR);
        });

        btnRemoverFornecedor.setOnClickListener(v -> {
            controller.setFornecedorNull();
            txtFornecedor.getText().clear();
            Toast.makeText(getContext(), "Fornecedor Foi Removido Desta Marca", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Marca");

        alertBuilderDeletar.setMessage("Tem Certeza que Deseja Deletar a Marca?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                controller.deletar();
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Marca Não foi Deletada");
            }
        });
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Marca");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Atualizar a Marca?");

        alertBuilderAtualizar.setPositiveButton("Sim", (dialog, which) -> {
            try {
                controller.getMarca().setNome(txtNome.getText().toString().toUpperCase());
                controller.atualizar();
            } catch (Exception e) {
                Log.e(LOG, e.getMessage(), e);
                Toast.makeText(AlterarDeletarMarca.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mensagemAoUsuario("Marca Não Foi Alterada");
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if (resultCode == Activity.RESULT_OK) {
                    Fornecedor fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");
                    controller.carregaFornecedor(fornecedor);
                    txtFornecedor.setText(controller.getMarca().getFornecedor().getNome());
                }
                break;
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }
}
