package com.vandamodaintima.jfpsb.contador.tela.manager.produto;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.ProdutoCursorAdapter;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.TelaPesquisa;

public class PesquisarProduto extends TelaPesquisa {

    protected RadioGroup radioGroup;
    protected EditText txtPesquisaProduto;
    protected ProdutoManager produtoManager;
    protected ListView listView;
    protected ProdutoCursorAdapter produtoCursorAdapter;
    protected TextView txtQuantProdutosCadastrados;

    protected static int TIPO_PESQUISA = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_pesquisar_produto);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(((ActivityBase) getActivity()).getConn());
    }

    @Override
    protected void setViews() {
        txtQuantProdutosCadastrados = viewInflate.findViewById(R.id.txtQuantProdutosCadastrados);

        setTxtPesquisaProduto();
        setRadioGroup();
        setListView();
    }

    /**
     * Popula a lista novamente
     */
    public void populaListView() {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        cursorPesquisa = produtoManager.listarCursor();

        txtQuantProdutosCadastrados.setText(String.valueOf(cursorPesquisa.getCount()));

        produtoCursorAdapter.changeCursor(cursorPesquisa);
    }

    protected void populaListView(String termo) {
        if(cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            switch (TIPO_PESQUISA) {
                case 1:
                    cursorPesquisa = produtoManager.listarCursorPorCodBarra(termo);
                    break;
                case 2:
                    cursorPesquisa = produtoManager.listarCursorPorDescricao(termo);
                    break;
                case 3:
                    cursorPesquisa = produtoManager.listarCursorPorFornecedor(termo);
                    break;
            }

            txtQuantProdutosCadastrados.setText(String.valueOf(cursorPesquisa.getCount()));
            produtoCursorAdapter.changeCursor(cursorPesquisa);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Contador", e.getMessage(), e);
        }
    }

    @Override
    protected void setListView() {
        listView = viewInflate.findViewById(R.id.listViewProduto);

        if(cursorPesquisa != null)
            cursorPesquisa.close();

        try {
            cursorPesquisa = produtoManager.listarCursor();
            txtQuantProdutosCadastrados.setText(String.valueOf(cursorPesquisa.getCount()));
            produtoCursorAdapter = new ProdutoCursorAdapter(viewInflate.getContext(), cursorPesquisa);
            listView.setAdapter(produtoCursorAdapter);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        super.setListView();
    }

    @Override
    protected void setListOnItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);

                cursor.moveToPosition(i);

                // Como tem fornecedor uso o manager pra vir logo iniciado
                Produto produto = produtoManager.listarPorChave(cursor.getString(cursor.getColumnIndexOrThrow("_id")));

                Bundle bundle = new Bundle();

                bundle.putSerializable("produto", produto);

                Intent alterarProduto = new Intent(viewInflate.getContext(), AlterarDeletarProduto.class);

                alterarProduto.putExtras(bundle);

                startActivityForResult(alterarProduto, TELA_ALTERAR_DELETAR);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TELA_ALTERAR_DELETAR:
                if(resultCode == Activity.RESULT_OK) {
                    populaListView(txtPesquisaProduto.getText().toString());
                }
                else {
                    Toast.makeText(viewInflate.getContext(), "O Produto Não Foi Alterado", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    protected void setRadioGroup() {
        radioGroup = viewInflate.findViewById(R.id.radioGroupOpcao);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selecao = radioGroup.getCheckedRadioButtonId();

                switch (selecao) {
                    case (R.id.rbCodBarra):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonCodBarra);
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_NUMBER);
                        TIPO_PESQUISA = 1;
                        break;
                    case (R.id.rbDescricao):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonDescricao);
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        TIPO_PESQUISA = 2;
                        break;
                    case (R.id.rbFornecedor):
                        txtPesquisaProduto.setHint(R.string.hintRadioButtonNomeFornecedor);
                        txtPesquisaProduto.setInputType(InputType.TYPE_CLASS_TEXT);
                        TIPO_PESQUISA = 3;
                        break;
                    default:
                        Toast.makeText(getActivity(), "Aconteceu algo de errado", Toast.LENGTH_SHORT).show();
                        TIPO_PESQUISA = 1;
                        break;
                }
            }
        });
    }

    protected void setTxtPesquisaProduto(){
        txtPesquisaProduto = viewInflate.findViewById(R.id.txtPesquisaProduto);

        txtPesquisaProduto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                populaListView(txtPesquisaProduto.getText().toString());
            }
        });
    }
}