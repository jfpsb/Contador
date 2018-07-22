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
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarFornecedor extends FragmentBase {
    private Button btnCadastrar;
    private EditText txtCnpj;
    private EditText txtNome;
    private FornecedorManager fornecedorManager;

    public CadastrarFornecedor() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_fornecedor, container, false);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCnpj = viewInflate.findViewById(R.id.txtCnpj);
        txtNome = viewInflate.findViewById(R.id.txtNome);

        setManagers();

        setBtnCadastrar();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        fornecedorManager = new FornecedorManager(((ActivityBase)getActivity()).getConn());
        super.setManagers();
    }

    private void setBtnCadastrar() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fornecedor fornecedor = new Fornecedor();

                try {
                    String cnpj = txtCnpj.getText().toString();
                    String nome = txtNome.getText().toString();

                    if(TestaIO.isStringEmpty(cnpj))
                        throw new Exception("Campo de CNPJ não pode ficar vazio!");

                    if(TestaIO.isStringEmpty(nome))
                        throw new Exception("Campo de nome não pode ficar vazio!");

                    fornecedor.setCnpj(cnpj);
                    fornecedor.setNome(nome.toUpperCase());

                    boolean result = fornecedorManager.inserir(fornecedor);

                    if(result) {
                        Toast.makeText(view.getContext(), "Inserção de fornecedor " + fornecedor.getNome() + " efetuada com sucesso.", Toast.LENGTH_SHORT).show();

                        PesquisarFornecedor.populaListView();

                        txtCnpj.setText("");
                        txtNome.setText("");
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Inserir Fornecedor", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
