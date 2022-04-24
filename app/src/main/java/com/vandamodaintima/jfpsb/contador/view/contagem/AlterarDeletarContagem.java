package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.AlterarDeletarContagemController;
import com.vandamodaintima.jfpsb.contador.controller.contagem.SpinnerTipoContagemAdapter;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

import java.util.List;

/**
 * Tela para alterar dados de contagem ou finalizar contagem. Também é possível deletar e exportar a contagem para Excel e abrir tela para adicionar contagem de produto.
 */
public class AlterarDeletarContagem extends TelaAlterarDeletar {

    private EditText txtData;
    private EditText txtLoja;
    private Spinner spinnerTipoContagem;
    private CheckBox checkBoxFinalizada;

    private AlterarDeletarContagemController controller;

    private static final int ESCOLHER_DIRETORIO = 1;
    private static final int PERMISSAO_WRITE_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_contagem);
        stub.inflate();

        setBtnAtualizar();

        txtData = findViewById(R.id.txtDataInicial);
        txtLoja = findViewById(R.id.txtLoja);
        spinnerTipoContagem = findViewById(R.id.spinnerTipoContagem);
        checkBoxFinalizada = findViewById(R.id.checkBoxFinalizada);

        navigationView.inflateMenu(R.menu.menu_alterar_deletar_contagem);
        navigationView.inflateHeaderView(R.layout.nav_alterar_deletar_contagem);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AlterarDeletarContagemController(this, conexaoBanco);

        String id = getIntent().getStringExtra("id");
        controller.carregaContagem(id);

        setAlertBuilderAtualizar();
        setAlertBuilderDeletar();

        txtData.setText(controller.getContagem().getFullDataString());
        txtLoja.setText(controller.getContagem().getLoja().getNome());
        checkBoxFinalizada.setChecked(controller.getContagem().getFinalizada());

        spinnerTipoContagem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object o = spinnerTipoContagem.getSelectedItem();
                controller.carregaTipoContagem(o);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        List<TipoContagem> tipoContagems = controller.getTipoContagens();
        ArrayAdapter<TipoContagem> spinnerTipoContagemAdapter = new SpinnerTipoContagemAdapter(getContext(), tipoContagems);
        spinnerTipoContagem.setAdapter(spinnerTipoContagemAdapter);

        spinnerTipoContagem.setSelection(controller.getTipoContagemIndex());

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int menuItemItemId = menuItem.getItemId();
            if (menuItemItemId == R.id.menuItemVerProdutos) {
                Intent visualizarContagem = new Intent(AlterarDeletarContagem.this, VisualizarProdutosContagem.class);
                visualizarContagem.putExtra("id", controller.getContagem().getId());
                startActivity(visualizarContagem);
            } else if (menuItemItemId == R.id.menuItemExportarContagemExcel) {
                Boolean permissaoRead = ContextCompat.checkSelfPermission(AlterarDeletarContagem.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
                Boolean permissaoWrite = ContextCompat.checkSelfPermission(AlterarDeletarContagem.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;

                if (permissaoRead && permissaoWrite) {
                    AbrirEscolhaDiretorioActivity();
                } else {
                    ActivityCompat.requestPermissions(AlterarDeletarContagem.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_WRITE_READ);
                }
            } else if (menuItemItemId == R.id.menuItemDeletar) {
                AlertDialog alertDialog = alertBuilderDeletar.create();
                alertDialog.show();
            } else if (menuItemItemId == R.id.menuItemAdicionarContagem) {
                Intent intent = new Intent(AlterarDeletarContagem.this, AdicionarContagemProduto.class);
                intent.putExtra("id", controller.getContagem().getId());
                startActivity(intent);
            }
            return true;
        });
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSAO_WRITE_READ) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão Concedida para Ler e Gravar na Memória Interna", Toast.LENGTH_SHORT).show();
                AbrirEscolhaDiretorioActivity();
            } else {
                Toast.makeText(this, "Conceda as Permissões Para Poder Exportar os Arquivos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Contagem");
        alertBuilderAtualizar.setMessage("Tem Certeza Que Deseja Alterar o Status Desta Contagem?");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Boolean finalizada = checkBoxFinalizada.isChecked();
                controller.atualizar(finalizada);
            }
        });

        alertBuilderAtualizar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarContagem.this, "Contagem Não Foi Atualizada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Contagem");
        alertBuilderDeletar.setMessage("Tem Certeza Que Deseja Apagar a Contagem?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                controller.deletar();
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarContagem.this, "Contagem Não Foi Deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void AbrirEscolhaDiretorioActivity() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String fileName = String.format("Contagem %s - %s.xlsx", controller.getContagem().getLoja().getNome(), controller.getContagem().getFullDataStringForFileName());

        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOLHER_DIRETORIO) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null && data.getData() != null) {
                    controller.exportarParaExcel(data.getData());
                }
            }
        }
    }
}
