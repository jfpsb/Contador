package com.vandamodaintima.jfpsb.contador.view.fornecedor;

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
import android.view.WindowManager;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.fornecedor.TelaFornecedorController;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

public class TelaFornecedor extends TabLayoutBaseView {

    protected CadastrarFornecedor cadastrarFornecedor;
    protected PesquisarFornecedor pesquisarFornecedor;

    private ConexaoBanco conexaoBanco;
    private TelaFornecedorController controller;

    private static final int ESCOLHER_ARQUIVO = 1;
    private static final int ESCOLHER_DIRETORIO = 2;
    private static final int PERMISSAO_WRITE_READ = 3;
    private static final int CADASTRAR_MANUALMENTE = 4;

    public TelaFornecedor() {
        super(new String[]{"Pesquisar", "Cadastrar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        setFragments();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        setViewPagerTabLayout(pesquisarFornecedor, cadastrarFornecedor);

        conexaoBanco = new ConexaoBanco(getApplicationContext());
        controller = new TelaFornecedorController(this, conexaoBanco);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tela_fornecedor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.itemImportarFornecedorExcel:
                Intent intentImportar = new Intent(Intent.ACTION_GET_CONTENT);
                intentImportar.setType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                startActivityForResult(Intent.createChooser(intentImportar, "Selecione o Arquivo Excel"), ESCOLHER_ARQUIVO);
                return true;
            case R.id.itemCadastrarFornecedorManualmente:
                Intent intentCadastrarManualmente = new Intent(this, CadastrarFornecedorManualmente.class);
                startActivityForResult(intentCadastrarManualmente, CADASTRAR_MANUALMENTE);
                return true;
            case R.id.itemExportarFornecedorExcel:
                Boolean permissaoRead = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
                Boolean permissaoWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

                if (permissaoRead && permissaoWrite) {
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

        String fileName = String.format("Fornecedores em %s.xlsx", Instant.now().atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH.mm.ss")));

        intent.putExtra(Intent.EXTRA_TITLE, fileName);

        startActivityForResult(intent, ESCOLHER_DIRETORIO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_ARQUIVO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    controller.importarFornecedoresDeExcel(uri, getContentResolver());
                }
                break;
            case ESCOLHER_DIRETORIO:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null && data.getData() != null) {
                        controller.exportarParaExcel(data.getData());
                    }
                }
                break;
            case CADASTRAR_MANUALMENTE:
                pesquisarFornecedor.realizarPesquisa();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setFragments() {
        cadastrarFornecedor = new CadastrarFornecedor();
        pesquisarFornecedor = new PesquisarFornecedor();
    }

    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }
}
