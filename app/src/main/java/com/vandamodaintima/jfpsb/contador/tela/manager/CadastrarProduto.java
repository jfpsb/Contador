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
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;
import com.vandamodaintima.jfpsb.contador.util.TratamentoMensagensSQLite;

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

        Cursor cursor2 = ManipulaCursor.retornaCursorComHintNull(cursor, "SELECIONE O FORNECEDOR", new String[] {"_id", "nome"});

        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursor2, new String[] {"nome"}, new int[] {android.R.id.text1},0);
        simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFornecedor.setAdapter(simpleCursorAdapter);

        spinnerFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                    cursor.moveToPosition(i);

                    fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("_id")));
                    fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                }
                else {
                    fornecedor.setCnpj(null);
                }
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

                    if(TestaIO.isStringEmpty(cod_barra))
                        throw new Exception("Código de barras não pode estar vazio!");

                    if(!TestaIO.isValidInt(cod_barra))
                        throw new Exception("O código de barras precisa conter somente números!");

                    if(TestaIO.isStringEmpty(descricao))
                        throw new Exception("A descrição do produto não pode estar vazia!");

                    if(TestaIO.isStringEmpty(preco))
                        throw new Exception("O preço do produto não pode estar vazio!");

                    if(!TestaIO.isValidDouble(preco))
                        throw new Exception("O valor de preço digitado é inválido");

                    produto.setCod_barra(cod_barra);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.setDescricao(descricao.toUpperCase());
                    produto.setFornecedor(fornecedor.getCnpj());

                    long[] result = daoProduto.inserir(produto);

                    if(result[0] != -1) {
                        Toast.makeText(viewInflate.getContext(), "O produto " + produto.getDescricao() + " foi inserido com sucesso!" , Toast.LENGTH_SHORT).show();

                        PesquisarProduto.populaListView();

                        txtPreco.setText("");
                        txtDescricao.setText("");
                        txtCodBarra.setText("");
                        spinnerFornecedor.setSelection(0);
                    }
                    else {
                        TratamentoMensagensSQLite.trataErroEmInsert(viewInflate.getContext(), result[1]);
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
