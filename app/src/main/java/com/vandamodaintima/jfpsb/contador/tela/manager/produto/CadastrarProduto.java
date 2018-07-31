package com.vandamodaintima.jfpsb.contador.tela.manager.produto;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Fornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Marca;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;
import com.vandamodaintima.jfpsb.contador.tela.manager.fornecedor.TelaFornecedorForResult;
import com.vandamodaintima.jfpsb.contador.util.ManipulaCursor;
import com.vandamodaintima.jfpsb.contador.util.TestaIO;

/**
 * A simple {@link Fragment} subclass.
 */
public class CadastrarProduto extends FragmentBase {

    private Button btnCadastrar;
    private Button btnEscolherFornecedor;
    private EditText txtCodBarra;
    private EditText txtDescricao;
    private EditText txtPreco;
    private EditText txtFornecedor;
    private EditText txtCodBarraFornecedor;
    private Spinner spinnerMarca;
    private TextView lblCodRepetido;

    private ProdutoManager produtoManager;
    private MarcaManager marcaManager;

    private Fornecedor fornecedor;

    private Animation slidedown;

    private static final int ESCOLHER_FORNECEDOR = 1;

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
        txtFornecedor = viewInflate.findViewById(R.id.txtFornecedor);
        txtCodBarraFornecedor = viewInflate.findViewById(R.id.txtCodBarraFornecedor);
        spinnerMarca = viewInflate.findViewById(R.id.spinnerMarca);
        btnEscolherFornecedor = viewInflate.findViewById(R.id.btnEscolherFornecedor);
        lblCodRepetido = viewInflate.findViewById(R.id.lblCnpjRepetido);

        setManagers();
        setBtnEscolherFornecedor();
        setBtnCadastrar();
        setTxtCodBarra();

        setSpinnerMarca();

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(((ActivityBase)getActivity()).getConn());
        marcaManager = new MarcaManager(((ActivityBase)getActivity()).getConn());
    }

    private void setBtnEscolherFornecedor() {
        btnEscolherFornecedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TelaFornecedorForResult.class);
                startActivityForResult(intent, ESCOLHER_FORNECEDOR);
            }
        });
    }

    private void setSpinnerMarca() {
        Cursor cursor = null, cursor2 = null;

        try {
            cursor = marcaManager.listarCursor();

            cursor2 = ManipulaCursor.retornaCursorComHintNull(cursor, "SELECIONE A MARCA", new String[]{"_id", "nome" });

            SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(viewInflate.getContext(), android.R.layout.simple_spinner_dropdown_item, cursor2, new String[]{"nome"}, new int[]{android.R.id.text1}, 0);
            simpleCursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerMarca.setAdapter(simpleCursorAdapter);

            setSpinnerMarcaOnItemSelectedListener();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            if(cursor != null)
                cursor.close();

            if(cursor2 != null)
                cursor2.close();
        }
    }

    private void setSpinnerMarcaOnItemSelectedListener() {
        if(spinnerMarca.getOnItemSelectedListener() == null) {
            spinnerMarca.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                        cursor.moveToPosition(position);

                        String nome = cursor.getString(cursor.getColumnIndexOrThrow("nome"));
                        Toast.makeText(getContext(), "Marca " + nome + "Escolhida", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "A Marca Não Foi Escolhida", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(getContext(), "A Marca Não Foi Escolhida", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case ESCOLHER_FORNECEDOR:
                if(resultCode == Activity.RESULT_OK) {
                    fornecedor = (Fornecedor) data.getSerializableExtra("fornecedor");

                    if(fornecedor != null) {
                        txtFornecedor.setText(fornecedor.getNome());
                    }
                }
                break;
        }
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
                    String cod_barra_fornecedor = txtCodBarraFornecedor.getText().toString();
                    int marca = (int) spinnerMarca.getSelectedItemId();

                    if(cod_barra.isEmpty())
                        throw new Exception("Código de Barras Não Pode Estar Vazio!");

                    if(descricao.isEmpty())
                        throw new Exception("A Descrição do Produto Não Pode Estar Vazia!");

                    if(preco.isEmpty() || ! TestaIO.isValidDouble(preco))
                        throw new Exception("Digite um Valor de Preço Válido!");

                    produto.setCod_barra(cod_barra);
                    produto.setCod_barra_fornecedor(cod_barra_fornecedor);
                    produto.setPreco(Double.parseDouble(preco));
                    produto.setDescricao(descricao.toUpperCase());
                    produto.setFornecedor(fornecedor);

                    if(marca != -1)
                        produto.setMarca(marca);

                    boolean result = produtoManager.inserir(produto);

                    if(result) {
                        Toast.makeText(viewInflate.getContext(), "O Produto " + produto.getDescricao() + " Foi Inserido com Sucesso!" , Toast.LENGTH_SHORT).show();

                        Fragment fragment = ((ActivityBase)getActivity()).getAdapter().getItem(0);
                        ((PesquisarProduto)fragment).populaListView();

                        txtPreco.getText().clear();
                        txtDescricao.getText().clear();
                        txtCodBarra.getText().clear();
                        txtCodBarraFornecedor.getText().clear();
                        txtFornecedor.getText().clear();
                        spinnerMarca.setSelection(0);
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
                        btnEscolherFornecedor.setEnabled(false);

                        lblCodRepetido.setVisibility(View.VISIBLE);
                        lblCodRepetido.startAnimation(slidedown);
                    }
                    else {
                        txtDescricao.setEnabled(true);
                        txtPreco.setEnabled(true);
                        btnCadastrar.setEnabled(true);
                        btnEscolherFornecedor.setEnabled(true);

                        lblCodRepetido.setVisibility(View.GONE);
                    }
                }
            }
        });
    }
}
