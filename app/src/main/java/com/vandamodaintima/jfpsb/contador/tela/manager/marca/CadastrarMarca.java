package com.vandamodaintima.jfpsb.contador.tela.manager.marca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;

public class CadastrarMarca extends FragmentBase {

    private EditText txtNome;
    private Button btnCadastrar;

    private MarcaManager marcaManager;

    public CadastrarMarca() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_marca, container, false);

        txtNome = viewInflate.findViewById(R.id.txtNome);
        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);

        setManagers();
        setBtnCadastrar();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        marcaManager = new MarcaManager(((ActivityBase)getActivity()).getConn());
    }

    private void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nome = txtNome.getText().toString();

                    if(nome.isEmpty()) {
                        throw new Exception("Campo de Nome Não Pode Estar Vazio!");
                    }

                    Marca marca = new Marca();

                    marca.setNome(nome.toUpperCase());

                    boolean result = marcaManager.inserir(marca);

                    if(result) {
                        Toast.makeText(getContext(), "Inserção de Marca " + marca.getNome() + " Efetuada Com Sucesso.", Toast.LENGTH_SHORT).show();

                        aposCadastro(marca);
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Inserir Marca", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                }
            }
        });
    }

    /**
     * Operação para ser feita após cadastro efetuado com sucesso
     */
    protected void aposCadastro(Marca marca) {
        try {
            // Atualiza lista em aba de pesquisa
            Fragment fragment = ((ActivityBase) (getActivity())).getAdapter().getItem(0);
            ((PesquisarMarca) fragment).populaListView();

            txtNome.setText("");
        }
        catch (Exception e) {
            Log.i("Contador", e.getMessage(), e);
        }
    }
}
