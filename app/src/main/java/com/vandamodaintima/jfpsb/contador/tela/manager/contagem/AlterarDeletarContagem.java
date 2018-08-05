package com.vandamodaintima.jfpsb.contador.tela.manager.contagem;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.entidade.Contagem;
import com.vandamodaintima.jfpsb.contador.excel.ManipulaExcel;
import com.vandamodaintima.jfpsb.contador.tela.manager.AlterarDeletarEntidade;
import com.vandamodaintima.jfpsb.contador.util.TrataDisplayData;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.util.Date;

public class AlterarDeletarContagem extends AlterarDeletarEntidade {

    private Contagem contagem;
    private EditText txtIDContagem;
    private EditText txtDataFinal;
    private EditText txtDataInicial;
    private EditText txtLojaAtual;
    private Button btnAdicionar;

    private ContagemManager contagemManager;

    private static final int ESCOLHER_DIRETORIO = 1;
    private static final int PEDIDO_PERMISSAO_READ = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.content_alterar_deletar_contagem);

        super.onCreate(savedInstanceState);

        contagem = (Contagem) getIntent().getExtras().getSerializable("contagem");

        txtIDContagem = findViewById(R.id.txtIDContagem);
        txtDataFinal = findViewById(R.id.txtDataFinal);
        txtDataInicial = findViewById(R.id.txtDataInicial);
        txtLojaAtual = findViewById(R.id.txtLojaAtual);
        btnAdicionar = findViewById(R.id.btnAdicionar);

        txtIDContagem.setText(String.valueOf(contagem.getIdcontagem()));
        if(contagem.getDatafinal() != null) {
            txtDataFinal.setText(TrataDisplayData.getDataEmStringDisplay(contagem.getDatafinal()));
        }

        txtDataInicial.setText(TrataDisplayData.getDataEmStringDisplay(contagem.getDatainicio()));
        txtLojaAtual.setText(contagem.getLoja().getNome());

        setBtnAdicionar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_alterar_deletar_contagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemVerProdutos:
                Intent visualizarContagem = new Intent(AlterarDeletarContagem.this, VisualizarProdutosContagem.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("contagem", contagem);
                visualizarContagem.putExtras(bundle);
                startActivity(visualizarContagem);
                return true;
            case R.id.itemExportarContagemExcel:
                if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PEDIDO_PERMISSAO_READ);
                } else {
                    AbrirEscolhaDiretorioActivity();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PEDIDO_PERMISSAO_READ:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissão Concedida para Acessar Memória Interna", Toast.LENGTH_SHORT).show();
                    AbrirEscolhaDiretorioActivity();
                }

                break;
        }
    }

    @Override
    protected void setManagers() {
        contagemManager = new ContagemManager(conn);
    }

    @Override
    protected void setBtnAtualizar() {
        btnAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dataFinal = txtDataFinal.getText().toString();

                if(dataFinal.isEmpty()) {
                    Toast.makeText(AlterarDeletarContagem.this, "O Campo de Data Final Não Pode Estar Vazio", Toast.LENGTH_SHORT).show();
                    return;
                }

                alertBuilderAtualizar.setMessage("Atualizar Data Final da Contagem para " + dataFinal + "?");

                AlertDialog alertDialog = alertBuilderAtualizar.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void setAlertBuilderAtualizar() {
        alertBuilderAtualizar = new AlertDialog.Builder(this);
        alertBuilderAtualizar.setTitle("Atualizar Contagem");

        alertBuilderAtualizar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Date data = TrataDisplayData.getDataDisplay(txtDataFinal.getText().toString());

                    Contagem toUpdate = new Contagem();
                    toUpdate.setIdcontagem(contagem.getIdcontagem());
                    toUpdate.setDatainicio(contagem.getDatainicio());
                    toUpdate.setLoja(contagem.getLoja());

                    toUpdate.setDatafinal(data);

                    boolean result = contagemManager.atualizar(toUpdate, contagem.getIdcontagem());

                    if (result) {
                        Toast.makeText(AlterarDeletarContagem.this, "A contagem com ID " + contagem.getIdcontagem() + " foi atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK, null);
                        finish();
                    }
                }
                catch (Exception e) {
                    Toast.makeText(AlterarDeletarContagem.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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
    protected void setAlertBuilderDeletar() {
        alertBuilderDeletar = new AlertDialog.Builder(this);
        alertBuilderDeletar.setTitle("Deletar Contagem");
        alertBuilderDeletar.setMessage("Tem certeza que deseja apagar a contagem de ID " + contagem.getIdcontagem() + "?");

        alertBuilderDeletar.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                boolean result = contagemManager.deletar(contagem.getIdcontagem());

                if(result) {
                    Toast.makeText(AlterarDeletarContagem.this, "Contagem Deletada Com Sucesso", Toast.LENGTH_SHORT).show();
                    setResult(Activity.RESULT_OK, null);
                    finish();
                }
                else {
                    Toast.makeText(AlterarDeletarContagem.this, "Erro ao Deletar Contagem", Toast.LENGTH_SHORT).show();
                }
            }
        });

        alertBuilderDeletar.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(AlterarDeletarContagem.this, "Contagem Não Foi Deletada", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setBtnAdicionar() {
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pesquisaContagemProduto = new Intent(AlterarDeletarContagem.this, PesquisaContagemProdutoContainer.class);

                Bundle bundle = new Bundle();

                bundle.putSerializable("contagem", contagem);

                pesquisaContagemProduto.putExtras(bundle);

                startActivity(pesquisaContagemProduto);
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

        if(requestCode == ESCOLHER_DIRETORIO) {
            if(resultCode == DirectoryChooserActivity.RESULT_CODE_DIR_SELECTED) {
                String diretorio = data.getStringExtra(DirectoryChooserActivity.RESULT_SELECTED_DIR);

                ManipulaExcel manipulaExcel = new ManipulaExcel(conn);

                boolean result = manipulaExcel.ExportaContagem(contagem, diretorio);

                if(result) {
                    Toast.makeText(this, "Arquivo Exportado Com Sucesso", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "Erro Ao Exportar Arquivo", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
