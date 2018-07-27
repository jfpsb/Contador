package com.vandamodaintima.jfpsb.contador.tela.manager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFornecedor extends FragmentBase {
    protected Button btnCadastrar;
    protected EditText txtCnpj;
    protected TextView lblCnpjRepetido;
    protected FornecedorManager fornecedorManager;

    protected AlertDialog.Builder alertaCadastro;

    protected Animation slidedown;

    public CadastrarFornecedor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_fornecedor, container, false);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        lblCnpjRepetido = viewInflate.findViewById(R.id.lblCnpjRepetido);

        setManagers();
        setBtnCadastrar();
        setTxtCnpj();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void setTxtCnpj() {
        slidedown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        txtCnpj.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String texto = txtCnpj.getText().toString();

                if(! texto.isEmpty()) {
                    Fornecedor fornecedor = fornecedorManager.listarPorCnpj(texto);

                    if(fornecedor != null) {
                        btnCadastrar.setEnabled(false);
                        lblCnpjRepetido.setVisibility(View.VISIBLE);
                        lblCnpjRepetido.startAnimation(slidedown);
                    }
                    else {
                        btnCadastrar.setEnabled(true);
                        lblCnpjRepetido.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(((ActivityBase)getActivity()).getConn());
    }

    protected void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String cnpj = txtCnpj.getText().toString();

                    if(cnpj.isEmpty())
                        throw new Exception("CNPJ Não Pode Ficar Vazio!");

                    new RetornarEmpresa().execute("https://www.receitaws.com.br/v1/cnpj/", cnpj);
                }
                catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Contador", e.getMessage(), e);
                }
            }
        });
    }

    public class RetornarEmpresa extends AsyncTask<String, Void, Object> {
        private Toast toast = Toast.makeText(viewInflate.getContext(), null, Toast.LENGTH_LONG);

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

                if(response == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    jsonReader = new JsonReader(new InputStreamReader(in, "UTF-8"));

                    String cnpj = strings[1];
                    String nome = null;
                    String fantasia = null;
                    String message = null;

                    jsonReader.beginObject();

                    while (jsonReader.hasNext()) {
                        String indice = jsonReader.nextName();

                        if(indice.equals("nome")) {
                            nome = jsonReader.nextString();
                        }
                        else if (indice.equals("fantasia")){
                            fantasia = jsonReader.nextString();
                        }
                        else if(indice.equals("message")) {
                            message = jsonReader.nextString();
                            return message;
                        }
                        else {
                            jsonReader.skipValue();
                        }
                    }

                    jsonReader.endObject();

                    Fornecedor fornecedor = new Fornecedor();

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome);
                    fornecedor.setFantasia(fantasia);

                    return fornecedor;
                }
                else {
                    Log.i("Contador", "Erro em Request. Server retornou status " + response);
                }
            }
            catch (Exception e) {
                Log.e("Contador", e.getMessage(), e);
            }
            finally {
                try {
                    if(jsonReader != null)
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
            if(object == null) {
                Toast.makeText(getContext(), "Erro ao Retornar Empresa", Toast.LENGTH_SHORT).show();
            }
            else if(object instanceof Fornecedor){
                Fornecedor fornecedor = (Fornecedor)object;
                setAlertaCadastro(fornecedor, toast);
            }
            else {
                String mensagem = (String)object;
                toast.setText("Erro ao Inserir Fornecedor: " + mensagem);
                toast.show();
            }
        }
    }

    protected void setAlertaCadastro(final Fornecedor fornecedor, final Toast toast) {
        alertaCadastro = new AlertDialog.Builder(getContext());
        alertaCadastro.setTitle("Cadastrar Fornecedor");

        String mensagem = "Confirme os Dados do Fornecedor Encontrado Com CNPJ: " + fornecedor.getCnpj() + "\n\n";
        mensagem += "Nome: " + fornecedor.getNome() + "\n\n";

        if(!fornecedor.getFantasia().isEmpty()) {
            mensagem += "Nome Fantasia: " + fornecedor.getFantasia() + "\n\n";
        }

        mensagem += "Deseja Cadastrar Este Fornecedor?";

        alertaCadastro.setMessage(mensagem);

        alertaCadastro.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean result = fornecedorManager.inserir(fornecedor);

                if (result) {
                    toast.setText("Inserção de Fornecedor " + fornecedor.getNome() + " Efetuada com Sucesso");
                    toast.show();

                    PesquisaAposCadastro();
                } else {
                    toast.setText("Erro ao Inserir Fornecedor");
                    toast.show();
                }
            }
        });

        alertaCadastro.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                toast.setText("Fornecedor Não Foi Cadastrado");
                toast.show();
            }
        });

        alertaCadastro.show();
    }

    /**
     * Depois que o cadastro é efetuado esse método é responsável por atualizar a lista de pesquisa
     */
    protected void PesquisaAposCadastro() {
        try {
            // Atualiza lista em aba de pesquisa
            Fragment fragment = ((ActivityBase) (getActivity())).getAdapter().getItem(0);
            ((PesquisarFornecedor) fragment).populaListView();

            txtCnpj.setText("");
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage(), e);
        }
    }
}
