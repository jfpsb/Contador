package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.AlterarDeletarContagemController;
import com.vandamodaintima.jfpsb.contador.view.TelaAlterarDeletar;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

public class AlterarDeletarContagem extends TelaAlterarDeletar {

    private EditText txtData;
    private EditText txtLoja;
    private CheckBox checkBoxFinalizada;

    private AlterarDeletarContagemController controller;

    private static final int ESCOLHER_DIRETORIO = 1;
    private static final int PEDIDO_PERMISSAO_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_alterar_deletar_contagem);
        stub.inflate();

        setBtnAtualizar();

        txtData = findViewById(R.id.txtDataInicial);
        txtLoja = findViewById(R.id.txtLoja);
        checkBoxFinalizada = findViewById(R.id.checkBoxFinalizada);

        navigationView.inflateMenu(R.menu.menu_alterar_deletar_contagem);
        navigationView.inflateHeaderView(R.layout.nav_alterar_deletar_contagem);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new AlterarDeletarContagemController(this, conexaoBanco);

        String loja = getIntent().getStringExtra("loja");
        String data = getIntent().getStringExtra("data");
        controller.carregaContagem(loja, data);

        setAlertBuilderAtualizar();
        setAlertBuilderDeletar();

        txtData.setText(controller.getFullDataString());
        txtLoja.setText(controller.getLojaNome());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.menuItemVerProdutos:
                        Intent visualizarContagem = new Intent(AlterarDeletarContagem.this, VisualizarProdutosContagem.class);
                        visualizarContagem.putExtra("loja", controller.getLoja());
                        visualizarContagem.putExtra("data", controller.getData());
                        startActivity(visualizarContagem);
                        break;
                    case R.id.menuItemExportarContagemExcel:
                        if (ContextCompat.checkSelfPermission(AlterarDeletarContagem.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(AlterarDeletarContagem.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PEDIDO_PERMISSAO_READ);
                        } else {
                            AbrirEscolhaDiretorioActivity();
                        }
                        break;
                    case R.id.menuItemDeletar:
                        AlertDialog alertDialog = alertBuilderDeletar.create();
                        alertDialog.show();
                        break;
                    case R.id.menuItemAdicionarContagem:
                        Intent intent = new Intent(AlterarDeletarContagem.this, AdicionarContagemProduto.class);
                        intent.putExtra("loja", controller.getLoja());
                        intent.putExtra("data", controller.getData());
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PEDIDO_PERMISSAO_READ:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão Concedida para Acessar Memória Interna", Toast.LENGTH_SHORT).show();
                    AbrirEscolhaDiretorioActivity();
                }

                break;
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
        Intent intentDiretorio = new Intent(this, DirectoryChooserActivity.class);

        DirectoryChooserConfig config = DirectoryChooserConfig.builder().
                newDirectoryName("Contador - Contagem Em Excel").allowReadOnlyDirectory(true).
                build();

        intentDiretorio.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);

        startActivityForResult(intentDiretorio, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ESCOLHER_DIRETORIO) {
            if (resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String diretorio = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);
                controller.exportarParaExcel(diretorio);
            }
        }
    }
}
