package com.vandamodaintima.jfpsb.contador.view.produto;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewStub;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.produto.TelaProdutoController;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;
import com.vandamodaintima.jfpsb.contador.view.TelaPesquisa;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class TelaProduto extends TabLayoutBaseView {
    protected TelaCadastro cadastrarProduto;
    protected TelaPesquisa pesquisarProduto;

    private AlphaAnimation inAnimation;
    private AlphaAnimation outAnimation;
    private LinearLayout progressBarHolder;
    private TextView txtProgressStatus;
    private ConexaoBanco conexaoBanco;

    private TelaProdutoController controller;

    private static final int ESCOLHER_ARQUIVO = 1;
    private static final int ESCOLHER_DIRETORIO = 2;
    private static final int PERMISSAO_WRITE_READ = 3;

    public TelaProduto() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        ViewStub stub2 = findViewById(R.id.telaLayoutViewStub);
        stub2.setLayoutResource(R.layout.activity_tela_produto);
        stub2.inflate();

        setFragments();

        progressBarHolder = findViewById(R.id.progressBarHolder);
        txtProgressStatus = findViewById(R.id.txtProgressStatus);

        setViewPagerTabLayout(pesquisarProduto, cadastrarProduto);

        conexaoBanco = new ConexaoBanco(this);
        controller = new TelaProdutoController(this, conexaoBanco);
    }

    protected void setFragments() {
        cadastrarProduto = new CadastrarProduto();
        pesquisarProduto = new PesquisarProduto();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_produto, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemImportarProdutoExcel:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                startActivityForResult(Intent.createChooser(intent, "Selecione o Arquivo Excel"), ESCOLHER_ARQUIVO);
                return true;
            case R.id.itemExportarProdutoExcel:
                Boolean permissaoRead = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                Boolean permissaoWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                if(permissaoRead && permissaoWrite) {
                    AbrirEscolhaDiretorioActivity();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_WRITE_READ);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    private void AbrirEscolhaDiretorioActivity() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        String fileName = String.format("Produtos em %s.xlsx", Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm.ss")));

        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_ARQUIVO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    controller.importarDeExcel(uri, getContentResolver());
                }
                break;
            case ESCOLHER_DIRETORIO:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        controller.exportarParaExcel(data.getData());
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }
}
