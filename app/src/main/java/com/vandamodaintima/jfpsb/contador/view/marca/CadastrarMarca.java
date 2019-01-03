package com.vandamodaintima.jfpsb.contador.view.marca;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;

import java.util.Date;

public class CadastrarMarca extends FragmentBase {

    private EditText txtNome;
    private Button btnCadastrar;

//    private MarcaManager marcaManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_cadastrar_marca);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setBtnCadastrar() {
        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nome = txtNome.getText().toString();

                    if (nome.isEmpty()) {
                        throw new Exception("Campo de Nome Não Pode Estar Vazio!");
                    }

                    Marca marca = new Marca();

                    marca.setId(new Date().getTime());
                    marca.setNome(nome.toUpperCase());

//                    boolean result = marcaManager.inserir(marca);
//
//                    if (result) {
//                        Toast.makeText(getContext(), "Inserção de Marca " + marca.getNome() + " Efetuada Com Sucesso.", Toast.LENGTH_SHORT).show();
//
//                        aposCadastro(marca);
//                    } else {
//                        Toast.makeText(viewInflate.getContext(), "Erro ao Inserir Marca", Toast.LENGTH_SHORT).show();
//                    }
                } catch (Exception e) {
                    Log.e(LOG, e.getMessage(), e);
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
            Fragment fragment = ((TabLayoutActivityBase) (getActivity())).getPagerAdapter().getItem(0);
            ((PesquisarMarca) fragment).populaListView();

            txtNome.getText().clear();
        } catch (Exception e) {
            Log.i(LOG, e.getMessage(), e);
        }
    }
}
