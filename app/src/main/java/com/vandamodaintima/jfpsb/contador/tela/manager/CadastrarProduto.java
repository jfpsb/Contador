package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
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
    private TextView lblCodRepetido;

    private ProdutoManager produtoManager;
    private FornecedorManager fornecedorManager;

    private Fornecedor fornecedor;

    private Animation slidedown;

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
        lblCodRepetido = viewInflate.findViewById(R.id.lblCnpjRepetido);

        setManagers();
        setSpinnerFornecedor();
        setBtnCadastrar();
        setTxtCodBarra();

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

            cursor2 = ManipulaCursor.retornaCursorComHintNull(cursor, "SELECIONE O FORNECEDOR", new String[]{"_id", "nome", "cnpj"});

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerFornecedor.setAdapter(simpleCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor != null)
                cursor.close();

            if(cursor2 != null)
                cursor2.close();
        }

        spinnerFornecedor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                    cursor.moveToPosition(i);

                    fornecedor = new Fornecedor();
                    fornecedor.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                    fornecedor.setCnpj(cursor.getString(cursor.getColumnIndexOrThrow("cnpj")));
                    fornecedor.setNome(cursor.getString(cursor.getColumnIndexOrThrow("nome")));
                }
                else {
                    fornecedor = null;
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

                    if(TestaIO.isStringEmpty(descricao))
                        throw new Exception("A descrição do produto não pode estar vazia!");

                    if(TestaIO.isStringEmpty(preco))
                        throw new Exception("O preço do produto não pode estar vazio!");

                    produto.setCod_barra(cod_barra);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.setDescricao(descricao.toUpperCase());
                    produto.setFornecedor(fornecedor);

                    boolean result = produtoManager.inserir(produto);

                    if(result) {
                        Toast.makeText(viewInflate.getContext(), "O produto " + produto.getDescricao() + " foi inserido com sucesso!" , Toast.LENGTH_SHORT).show();

                        txtPreco.setText("");
                        txtDescricao.setText("");
                        txtCodBarra.setText("");
                        spinnerFornecedor.setSelection(0);
                    }
                    else {
                        Toast.makeText(viewInflate.getContext(), "Erro ao Cadastrar Produto.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    Log.e("Contador", e.getMessage(), e);
                    Toast.makeText(viewInflate.getContext(), "Erro ao Cadastrar Produto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setTxtCodBarra() {
        slidedown = AnimationUtils.loadAnimation(getContext(), R.anim.slide_down);

        txtCodBarra.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String texto = txtCodBarra.getText().toString();

                if(! texto.isEmpty()) {
                    Produto produto = produtoManager.listarPorChave(texto);

                    if(produto != null) {
                        txtDescricao.setEnabled(false);
                        txtPreco.setEnabled(false);
                        btnCadastrar.setEnabled(false);
                        spinnerFornecedor.setEnabled(false);

                        lblCodRepetido.setVisibility(View.VISIBLE);
                        lblCodRepetido.startAnimation(slidedown);
                    }
                    else {
                        txtDescricao.setEnabled(true);
                        txtPreco.setEnabled(true);
                        btnCadastrar.setEnabled(true);
                        spinnerFornecedor.setEnabled(true);

                        lblCodRepetido.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
