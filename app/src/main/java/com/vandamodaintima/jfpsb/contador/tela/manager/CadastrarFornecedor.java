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
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFornecedor extends Fragment {
    private Button btnCadastrar;
    private EditText txtCnpj;
    private EditText txtNome;
    private DAOFornecedor daoFornecedor;
    private ConexaoBanco conn;

    public CadastrarFornecedor() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_fornecedor, container, false);

        daoFornecedor = new DAOFornecedor(conn.conexao());

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        txtNome = viewInflate.findViewById(R.id.txtNome);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fornecedor fornecedor = new Fornecedor();

                try {
                    if(fornecedor.getCnpj().isEmpty())
                        throw new Exception("Campo de CNPJ não pode ficar vazio!");

                    if(fornecedor.getNome().isEmpty())
                        throw new Exception("Campo de nome não pode ficar vazio!");

                    fornecedor.setCnpj(txtCnpj.getText().toString());
                    fornecedor.setNome(txtNome.getText().toString());

                    long id = daoFornecedor.inserir(fornecedor);

                    if(id != -1) {
                        Toast.makeText(view.getContext(), "Inserção de fornecedor " + fornecedor.getNome() + " efetuada com sucesso.", Toast.LENGTH_SHORT).show();

                        PesquisarFornecedor.populaListView();

                        txtCnpj.setText("");
                        txtNome.setText("");
                    }else {
                        Toast.makeText(view.getContext(), "Erro ao inserir fornecedor!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewInflate;
    }

}
