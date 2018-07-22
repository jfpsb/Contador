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
import com.vandamodaintima.jfpsb.contador.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.dao.manager.FornecedorManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarProduto extends FragmentBase {

    private Button btnCadastrar;
    private EditText txtCodBarra;
    private Spinner spinnerFornecedor;
    private EditText txtDescricao;
    private EditText txtPreco;

    private ProdutoManager produtoManager;
    private FornecedorManager fornecedorManager;

    private Fornecedor fornecedor = new Fornecedor();

    public CadastrarProduto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_cadastrar_produto, container, false);

        btnCadastrar = viewInflate.findViewById(R.id.btnCadastrar);
        txtCodBarra = viewInflate.findViewById(R.id.txtCodBarra);
        txtDescricao = viewInflate.findViewById(R.id.txtDescricao);
        txtPreco = viewInflate.findViewById(R.id.txtPreco);
        spinnerFornecedor = viewInflate.findViewById(R.id.spinnerFornecedores);

        setManagers();

        setSpinnerFornecedor();

        setBtnCadastrar();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(((ActivityBase)getActivity()).getConn());
        fornecedorManager = new FornecedorManager(((ActivityBase)getActivity()).getConn());
    }

    private void setSpinnerFornecedor() {
        Cursor cursor = null, cursor2 = null;

        try {
            cursor = fornecedorManager.listarCursor();

            cursor2 = ManipulaCursor.retornaCursorComHintNull(cursor, "SELECIONE O FORNECEDOR", new String[]{"_id", "nome"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFornecedor.setAdapter(simpleCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            cursor.close();
            cursor2.close();
        }

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
    }

    private void setBtnCadastrar() {
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
                    produto.setFornecedor(fornecedor);

                    boolean result = produtoManager.inserir(produto);

                    if(result) {
                        Toast.makeText(viewInflate.getContext(), "O produto " + produto.getDescricao() + " foi inserido com sucesso!" , Toast.LENGTH_SHORT).show();

                        PesquisarProduto.populaListView();

                        txtPreco.setText("");
                        txtDescricao.setText("");
                        txtCodBarra.setText("");
                        spinnerFornecedor.setSelection(0);
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Cadastrar Produto.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException nfe) {
                    Toast.makeText(viewInflate.getContext(), "O valor digitado no campo preço não é um número válido!", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
