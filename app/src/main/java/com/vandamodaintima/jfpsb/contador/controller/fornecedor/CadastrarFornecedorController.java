package com.vandamodaintima.jfpsb.contador.controller.fornecedor;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.manager.FornecedorManager;
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
    private FornecedorManager fornecedorManager;
    private ConexaoBanco conexaoBanco;

    public CadastrarFornecedorController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarFornecedor) view;
        this.conexaoBanco = conexaoBanco;
        fornecedorManager = new FornecedorManager(conexaoBanco);
    }

    public void cadastrar(String cnpj, String nome, String fantasia, String email) {
        if (cnpj.isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Ser Vazio");
            return;
        }

        if (nome.isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Ser Vazio");
            return;
        }

        fornecedorManager.getFornecedor().setCnpj(cnpj);
        fornecedorManager.getFornecedor().setNome(nome);
        fornecedorManager.getFornecedor().setFantasia(fantasia);
        fornecedorManager.getFornecedor().setEmail(email);

        Boolean result = fornecedorManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Fornecedor Cadastrado Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Fornecedor");
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
            Fornecedor fornecedor = fornecedorManager.listarPorId(cnpj);

            if (fornecedor != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
    }

    public class RetornarEmpresa extends AsyncTask<String, Void, Object> {
        private Toast toast = Toast.makeText(view.getContext(), null, Toast.LENGTH_LONG);

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
                    String email = null;
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
                            case "message":
                                message = jsonReader.nextString();
                                return message;
                            default:
                                jsonReader.skipValue();
                                break;
                        }
                    }

                    jsonReader.endObject();

                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome);
                    fornecedor.setFantasia(fantasia);
                    fornecedor.setEmail(email);

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
                Toast.makeText(view.getContext(), "Erro ao Retornar Fornecedor", Toast.LENGTH_SHORT).show();
            } else if (object instanceof Fornecedor) {
                Fornecedor fornecedor = (Fornecedor) object;
                view.setAlertaCadastro(fornecedor.getCnpj(), fornecedor.getNome(), fornecedor.getFantasia(), fornecedor.getEmail());
            } else {
                String mensagem = (String) object;
                toast.setText("Erro ao Inserir Fornecedor: " + mensagem);
                toast.show();
            }
        }
    }

    public Fornecedor getFornecedor() {
        return fornecedorManager.getFornecedor();
    }
}
