package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.view.fornecedor.CadastrarFornecedor;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CadastrarFornecedorController {
    protected CadastrarFornecedor view;
    DAOFornecedor daoFornecedor;
    protected Context context;

    public CadastrarFornecedorController(CadastrarView view, SQLiteDatabase sqLiteDatabase, Context context) {
        this.view = (CadastrarFornecedor) view;
        this.context = context;
        daoFornecedor = new DAOFornecedor(sqLiteDatabase);
    }

    public boolean cadastrar(Fornecedor fornecedor) {
        Boolean result = daoFornecedor.inserir(fornecedor);

        if(result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.limparCampos();
            return true;
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
        }

        return false;
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
            Fornecedor fornecedor = daoFornecedor.listarPorId(cnpj);

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

                    jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));

                    String cnpj = strings[1];
                    String nome = null;
                    String fantasia = null;
                    String message;

                    jsonReader.beginObject();

                    while (jsonReader.hasNext()) {
                        String indice = jsonReader.nextName();

                        if (indice.equals("nome")) {
                            nome = jsonReader.nextString();
                        } else if (indice.equals("fantasia")) {
                            fantasia = jsonReader.nextString();
                        } else if (indice.equals("message")) {
                            message = jsonReader.nextString();
                            return message;
                        } else {
                            jsonReader.skipValue();
                        }
                    }

                    jsonReader.endObject();

                    Fornecedor fornecedor = new Fornecedor();

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
                Toast.makeText(context, "Erro ao Retornar Empresa", Toast.LENGTH_SHORT).show();
            } else if (object instanceof Fornecedor) {
                Fornecedor fornecedor = (Fornecedor) object;
                view.setAlertaCadastro(fornecedor);
            } else {
                String mensagem = (String) object;
                toast.setText("Erro ao Inserir Fornecedor: " + mensagem);
                toast.show();
            }
        }
    }
}
