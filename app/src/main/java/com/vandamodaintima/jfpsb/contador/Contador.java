package com.vandamodaintima.jfpsb.contador;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.OperadoraCartao;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOOperadoraCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAORecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;
import com.vandamodaintima.jfpsb.contador.sincronizacao.DatabaseLogFile;
import com.vandamodaintima.jfpsb.contador.sincronizacao.SocketCliente;
import com.vandamodaintima.jfpsb.contador.view.contagem.TelaContador;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.TelaFornecedor;
import com.vandamodaintima.jfpsb.contador.view.loja.TelaLoja;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProduto;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class Contador extends AppCompatActivity {

    private static final int PERMISSOES_APP = 1;
    private ConexaoBanco conexaoBanco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidThreeTen.init(this);
        setContentView(R.layout.activity_contador);

        Button btnContador = findViewById(R.id.btnContador);
        Button btnProduto = findViewById(R.id.btnProduto);
        Button btnFornecedor = findViewById(R.id.btnFornecedor);
        Button btnLoja = findViewById(R.id.btnLoja);

        btnContador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaContador.class);
                startActivity(it);
            }
        });

        btnProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaProduto.class);
                startActivity(it);
            }
        });

        btnFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaFornecedor.class);
                startActivity(it);
            }
        });

        btnLoja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Contador.this, TelaLoja.class);
                startActivity(it);
            }
        });

        boolean permissaoRead = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoWrite = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean permissaoInternet = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED;

        if (!permissaoRead || !permissaoWrite || !permissaoCamera || !permissaoInternet)
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    PERMISSOES_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        conexaoBanco = new ConexaoBanco(getApplicationContext());
        //Teste();
        new SocketCliente(getApplicationContext(), conexaoBanco).start();
    }

    private void Teste() {
        DAOContagem daoContagem = new DAOContagem(conexaoBanco);
        /*DAOContagemProduto daoContagemProduto = new DAOContagemProduto(conexaoBanco);
        DAOFornecedor daoFornecedor = new DAOFornecedor(conexaoBanco);
        DAOLoja daoLoja = new DAOLoja(conexaoBanco);
        DAOMarca daoMarca = new DAOMarca(conexaoBanco);
        DAOOperadoraCartao daoOperadoraCartao = new DAOOperadoraCartao(conexaoBanco);
        DAOProduto daoProduto = new DAOProduto(conexaoBanco);
        DAORecebimentoCartao daoRecebimentoCartao = new DAORecebimentoCartao(conexaoBanco);
        DAOTipoContagem daoTipoContagem = new DAOTipoContagem(conexaoBanco);*/

        List<Contagem> contagems = daoContagem.listar();
        /*List<ContagemProduto> contagemProdutos = daoContagemProduto.listar();
        List<Fornecedor> fornecedors = daoFornecedor.listar();
        List<Loja> lojas = daoLoja.listar();
        List<Marca> marcas = daoMarca.listar();
        List<OperadoraCartao> operadoraCartaos = daoOperadoraCartao.listar();
        List<Produto> produtos = daoProduto.listar();
        List<RecebimentoCartao> recebimentoCartaos = daoRecebimentoCartao.listar();
        List<TipoContagem> tipoContagems = daoTipoContagem.listar();*/

        for(Contagem contagem : contagems) {
            escreverJson("INSERT", contagem);
        }

        /*for(ContagemProduto ContagemProduto : contagemProdutos) {
            escreverJson("INSERT", ContagemProduto);
        }

        for(Fornecedor Fornecedor : fornecedors) {
            escreverJson("INSERT", Fornecedor);
        }

        for(Loja Loja : lojas) {
            escreverJson("INSERT", Loja);
        }

        for(Marca Marca : marcas) {
            escreverJson("INSERT", Marca);
        }

        for(OperadoraCartao OperadoraCartao : operadoraCartaos) {
            escreverJson("INSERT", OperadoraCartao);
        }

        for(Produto Produto : produtos) {
            escreverJson("INSERT", Produto);
        }

        for(RecebimentoCartao RecebimentoCartao : recebimentoCartaos) {
            escreverJson("INSERT", RecebimentoCartao);
        }

        for(TipoContagem TipoContagem : tipoContagems) {
            escreverJson("INSERT", TipoContagem);
        }*/
    }

    private <E extends IModel> void escreverJson(String operacao, E entidade) {
        File dirDatabaseLog = getApplicationContext().getDir("DatabaseLog", Context.MODE_PRIVATE);
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                .registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
                    @Override
                    public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String data = json.getAsJsonPrimitive().getAsString();
                        return ZonedDateTime.parse(data, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                })
                .registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
                    @Override
                    public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                        return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    }
                })
                .create();
        ZonedDateTime lastWriteTime = Instant.now().atZone(ZoneId.systemDefault());
        DatabaseLogFile<E> databaseLogFile = new DatabaseLogFile<>();
        databaseLogFile.setLastWriteTime(lastWriteTime.minusYears(1));
        databaseLogFile.setOperacaoMySQL(operacao);
        databaseLogFile.setEntidade(entidade);

        String json = gson.toJson(databaseLogFile, new TypeToken<DatabaseLogFile<E>>() {
        }.getType());
        File logFile = new File(dirDatabaseLog, databaseLogFile.getFileName());

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        conexaoBanco.close();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSOES_APP:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[3] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissões Concedidas", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
