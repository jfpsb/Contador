package com.vandamodaintima.jfpsb.contador.tela.manager.loja;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;

public class CadastrarLoja extends FragmentBase {
    private Button btnCadastrar;
    private LojaManager lojaManager;
    private EditText txtNome;
    private EditText txtCnpj;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_cadastrar_loja);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        lojaManager = new LojaManager(((TabLayoutActivityBase) getActivity()).getConn());
    }

    @Override
    protected void setViews() {
        txtNome = viewInflate.findViewById(R.id.txtNome);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);

        setBtnCadastrar();
    }

    private void setBtnCadastrar() {
        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loja loja = new Loja();

                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if (cnpj.isEmpty())
                        throw new Exception("O campo de cnpj está vazio");

                    if (nome.isEmpty())
                        throw new Exception("O campo de nome está vazio");

                    loja.setCnpj(cnpj);
                    loja.setNome(nome.toUpperCase());

                    boolean result = lojaManager.inserir(loja);

                    if (result) {
                        Toast.makeText(view.getContext(), "Inserção de Loja " + loja.getNome() + " Efetuada Com Sucesso.", Toast.LENGTH_SHORT).show();

                        Fragment fragment = ((TabLayoutActivityBase) getActivity()).getPagerAdapter().getItem(0);
                        ((PesquisarLoja) fragment).populaListView();

                        txtNome.setText("");
                    } else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Inserir Loja", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
