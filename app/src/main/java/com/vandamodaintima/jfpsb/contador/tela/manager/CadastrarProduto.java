package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarProduto extends Fragment {

    private Button btnCadastrar;
    private EditText txtCodBarra;
    private Spinner spinnerFornecedor;
    private EditText txtDescricao;
    private EditText txtPreco;

    private ConexaoBanco conn;
    private DAOProduto daoProduto;
    private DAOFornecedor daoFornecedor;

    private Fornecedor fornecedor = new Fornecedor();

    public CadastrarProduto() {
        // Required empty public constructor
    }

    public void setConn(ConexaoBanco conn) {
        this.conn = conn;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View viewInflate = inflater.inflate(R.layout.fragment_cadastrar_produto, container, false);

        daoProduto = new DAOProduto(conn.conexao());
        daoFornecedor = new DAOFornecedor(conn.conexao());

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCodBarra = viewInflate.findViewById(R.id.txtCodBarra);
        txtDescricao = viewInflate.findViewById(R.id.txtDescricao);
        txtPreco = viewInflate.findViewById(R.id.txtPreco);
        spinnerFornecedor = viewInflate.findViewById(R.id.spinnerFornecedores);

        Cursor cursor = daoFornecedor.selectFornecedores();

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursor, new String[] {"nome"}, new int[] {android.R.id.text1},0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFornecedor.setAdapter(simpleCursorAdapter);

        spinnerFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Produto produto = new Produto();

                try {
                    String cod_barra = txtCodBarra.getText().toString();
                    String descricao = txtDescricao.getText().toString();
                    String preco = txtPreco.getText().toString();

                    if(cod_barra.isEmpty())
                        throw new Exception("Código de barras não pode estar vazio!");

                    if(descricao.isEmpty())
                        throw new Exception("A descrição do produto não pode estar vazia!");

                    if(preco.isEmpty())
                        throw new Exception("O preço do produto não pode estar vazio!");

                    produto.setCod_barra(Integer.parseInt(cod_barra));
                    produto.setPreco(Double.parseDouble(preco));
                    produto.setDescricao(descricao);
                    produto.setFornecedor(fornecedor.getCnpj());

                    long id = daoProduto.inserir(produto);

                    if(id != -1) {
                        Toast.makeText(viewInflate.getContext(), "O produto " + produto.getDescricao() + " foi inserido com sucesso!" , Toast.LENGTH_SHORT).show();

                        PesquisarProduto.populaListView();

                        txtPreco.setText("");
                        txtDescricao.setText("");
                        txtCodBarra.setText("");
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao inserir produto!", Toast.LENGTH_SHORT).show();
                    }


                } catch (NumberFormatException nfe) {
                    Toast.makeText(viewInflate.getContext(), "O valor digitado no campo preço não é um número válido!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return viewInflate;
    }

}
