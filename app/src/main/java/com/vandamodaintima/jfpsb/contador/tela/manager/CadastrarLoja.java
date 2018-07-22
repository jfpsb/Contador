package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.dao.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarLoja extends FragmentBase {
    private Button btnCadastrar;
    private LojaManager lojaManager;
    private EditText txtNome;
    private EditText txtCnpj;

    public CadastrarLoja() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_loja, container, false);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtNome = viewInflate.findViewById(R.id.txtNome);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);

        setManagers();

        setBtnCadastrar();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loja loja = new Loja();

                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(cnpj.isEmpty())
                        throw new Exception("O campo de cnpj está vazio");

                    if(nome.isEmpty())
                        throw new Exception("O campo de nome está vazio");

                    loja.setCnpj(cnpj);
                    loja.setNome(nome.toUpperCase());

                    boolean result = lojaManager.inserir(loja);

                    if (result) {
                        Toast.makeText(view.getContext(), "Inserção de loja " + loja.getNome() + " efetuada com sucesso.", Toast.LENGTH_SHORT).show();

                        PesquisarLoja.populaListView();

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

    @Override
    protected void setManagers() {
        lojaManager = new LojaManager(((ActivityBase)getActivity()).getConn());
    }
}
