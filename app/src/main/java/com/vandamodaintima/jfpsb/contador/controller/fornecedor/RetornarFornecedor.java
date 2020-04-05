package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.model.Fornecedor;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RetornarFornecedor<View extends IAposPesquisarFornecedor> extends AsyncTask<String, Void, Fornecedor> {
    private Context context;
    private Toast toast;
    private View view;

    public RetornarFornecedor(View view) {
        this.context = view.getContext();
        this.view = view;
        toast = Toast.makeText(context, null, Toast.LENGTH_LONG);
    }

    @Override
    protected void onPreExecute() {
        toast.setText("Pesquisando CNPJ na Receita Federal");
        toast.show();
    }

    @Override
    protected Fornecedor doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        JsonReader jsonReader = null;
        Fornecedor fornecedor = new Fornecedor();

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
                String email = null;
                String telefone = null;
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
                        case "email":
                            email = jsonReader.nextString();
                            break;
                        case "telefone":
                            telefone = jsonReader.nextString();
                            break;
                        case "message":
                            message = jsonReader.nextString();
                            fornecedor.setCnpj(null);
                            fornecedor.setNome(message);
                            return fornecedor;
                        default:
                            jsonReader.skipValue();
                            break;
                    }
                }

                jsonReader.endObject();

                fornecedor.setCnpj(cnpj);
                fornecedor.setNome(nome);
                fornecedor.setFantasia(fantasia);
                fornecedor.setEmail(email);
                fornecedor.setTelefone(telefone);

                return fornecedor;
            } else {
                throw new Exception("Erro em Request. Server retornou status " + response);
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
    protected void onPostExecute(Fornecedor f) {
        if (f == null) {
            Toast.makeText(context, "Erro ao Retornar Fornecedor", Toast.LENGTH_SHORT).show();
        } else if (f.getCnpj() != null) {
            view.aposPesquisarFornecedor(f);
        } else {
            String mensagem = f.getNome();
            toast.setText("Erro ao Inserir Fornecedor: " + mensagem);
            toast.show();
        }
    }
}
