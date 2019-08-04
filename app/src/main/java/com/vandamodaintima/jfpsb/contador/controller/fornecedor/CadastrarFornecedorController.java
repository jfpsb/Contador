package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.CadastrarFornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class CadastrarFornecedorController {
    protected CadastrarFornecedor view;
    FornecedorModel fornecedorModel;
    protected Context context;
    private ConexaoBanco conexaoBanco;

    public CadastrarFornecedorController(CadastrarView view, ConexaoBanco conexaoBanco, Context context) {
        this.view = (CadastrarFornecedor) view;
        this.context = context;
        this.conexaoBanco = conexaoBanco;
        fornecedorModel = new FornecedorModel(conexaoBanco);
    }

    public void cadastrar(FornecedorModel fornecedor) {
        Boolean result = fornecedor.inserir();

        if(result) {
            view.mensagemAoUsuario("FornecedorModel Cadastrado Com Sucesso");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar FornecedorModel");
        }
    }

    public void pesquisarNaReceita(String cnpj) {
        if (cnpj.isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Ficar Vazio");
            return;
        }

        new RetornarEmpresa().execute("https://www.receitaws.com.br/v1/cnpj/", cnpj);
    }

    public void checaCnpj(String cnpj) {
        if (!cnpj.isEmpty()) {
            FornecedorModel fornecedor = fornecedorModel.listarPorId(cnpj);

            if (fornecedor != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }

    public class RetornarEmpresa extends AsyncTask<String, Void, Object> {
        private Toast toast = Toast.makeText(context, null, Toast.LENGTH_LONG);

        @Override
        protected void onPreExecute() {
            toast.setText("Pesquisando CNPJ na Receita Federal");
            toast.show();
        }

        @Override
        protected Object doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            JsonReader jsonReader = null;

            try {
                URL url = new URL(strings[0] + strings[1]);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                int response = urlConnection.getResponseCode();

                if (response == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    jsonReader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8));

                    String cnpj = strings[1];
                    String nome = null;
                    String fantasia = null;
                    String message;

                    jsonReader.beginObject();

                    while (jsonReader.hasNext()) {
                        String indice = jsonReader.nextName();

                        switch (indice) {
                            case "nome":
                                nome = jsonReader.nextString();
                                break;
                            case "fantasia":
                                fantasia = jsonReader.nextString();
                                break;
                            case "message":
                                message = jsonReader.nextString();
                                return message;
                            default:
                                jsonReader.skipValue();
                                break;
                        }
                    }

                    jsonReader.endObject();

                    FornecedorModel fornecedor = new FornecedorModel(conexaoBanco);

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome);
                    fornecedor.setFantasia(fantasia);

                    return fornecedor;
                } else {
                    Log.i("Contador", "Erro em Request. Server retornou status " + response);
                }
            } catch (Exception e) {
                Log.e("Contador", e.getMessage(), e);
            } finally {
                try {
                    if (jsonReader != null)
                        jsonReader.close();
                } catch (Exception e) {
                    Log.e("Contador", "Fechando JsonReader: " + e.getMessage(), e);
                }

                urlConnection.disconnect();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object object) {
            if (object == null) {
                Toast.makeText(context, "Erro ao Retornar Fornecedor", Toast.LENGTH_SHORT).show();
            } else if (object instanceof FornecedorModel) {
                FornecedorModel fornecedor = (FornecedorModel) object;
                view.setAlertaCadastro(fornecedor);
            } else {
                String mensagem = (String) object;
                toast.setText("Erro ao Inserir Fornecedor: " + mensagem);
                toast.show();
            }
        }
    }
}
