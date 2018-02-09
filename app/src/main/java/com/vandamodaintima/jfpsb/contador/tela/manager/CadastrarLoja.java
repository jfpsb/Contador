package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.entidade.Loja;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarLoja extends Fragment {
    private Button btnCadastrar;
    private ConexaoBanco conn;
    private DAOLoja daoLoja;
    private EditText txtNome;

    public CadastrarLoja() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_loja, container, false);

        daoLoja = new DAOLoja(conn.conexao());

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtNome = viewInflate.findViewById(R.id.txtNome);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Loja loja = new Loja();

                loja.setNome(txtNome.getText().toString());

                try {
                    if(loja.getNome().isEmpty())
                        throw new Exception("O campo de nome está vazio");

                    long id = daoLoja.inserir(loja);

                    if (id != -1) {
                        Toast.makeText(view.getContext(), "Inserção de loja " + loja.getNome() + " efetuada com sucesso.", Toast.LENGTH_SHORT).show();

                        PesquisarLoja.populaListView();

                        txtNome.setText("");
                    } else {
                        Toast.makeText(view.getContext(), "Erro ao inserir loja!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(view.getContext(), "O nome da loja não pode ser vazio!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewInflate;
    }
}
