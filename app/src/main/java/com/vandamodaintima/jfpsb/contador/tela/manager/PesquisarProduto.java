package com.vandamodaintima.jfpsb.contador.tela.manager;


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
import com.vandamodaintima.jfpsb.contador.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.dao.manager.ProdutoManager;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.ActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarProduto extends FragmentBase {

    private RadioGroup radioGroup;
    private EditText txtPesquisaProduto;
    private static ProdutoManager produtoManager;
    private ListView listView;
    private static ProdutoCursorAdapter produtoCursorAdapter;
    private static int TIPO_PESQUISA = 1;
    private static TextView txtQuantProdutosCadastrados;

    public PesquisarProduto() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewInflate = inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);

        setManagers();

        radioGroup = viewInflate.findViewById(R.id.radioGroupOpcao);

        txtPesquisaProduto = viewInflate.findViewById(R.id.txtPesquisaProduto);
        txtQuantProdutosCadastrados = viewInflate.findViewById(R.id.txtQuantProdutosCadastrados);
        listView = viewInflate.findViewById(R.id.listViewProduto);

        setTxtPesquisaProduto();

        setRadioGroup();

        setListView(viewInflate);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {
        produtoManager = new ProdutoManager(((ActivityBase) getActivity()).getConn());
    }

    /**
     * Popula a lista novamente
     */
    public static void populaListView() {
        if(cursorLista != null)
            cursorLista.close();

        cursorLista = produtoManager.listarCursor();

        txtQuantProdutosCadastrados.setText(String.valueOf(cursorLista.getCount()));

        produtoCursorAdapter.changeCursor(cursorLista);
    }

    public static void populaListView(String termo) {
        if(cursorLista != null)
            cursorLista.close();

        try {

            switch (TIPO_PESQUISA) {
                case 1:
                    cursorLista = produtoManager.listarCursorPorCodBarra(termo);
                    break;
                case 2:
                    cursorLista = produtoManager.listarCursorPorDescricao(termo);
                    break;
                case 3:
                    cursorLista = produtoManager.listarCursorPorFornecedor(termo);
                    break;
            }

            txtQuantProdutosCadastrados.setText(String.valueOf(cursorLista.getCount()));

            produtoCursorAdapter.changeCursor(cursorLista);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Contador", e.getMessage(), e);
        }
    }

    private void setListView(final View viewInflate) {
        if(cursorLista != null)
            cursorLista.close();

        try {
            cursorLista = produtoManager.listarCursor();

            txtQuantProdutosCadastrados.setText(String.valueOf(cursorLista.getCount()));

            produtoCursorAdapter = new ProdutoCursorAdapter(viewInflate.getContext(), cursorLista);

            listView.setAdapter(produtoCursorAdapter);

            setListViewOnItemClickListener(viewInflate);
        } catch (Exception e) {
            Toast.makeText(viewInflate.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListViewOnItemClickListener(final View viewInflate) {
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

                startActivity(alterarProduto);
            }
        });
    }

    private void setRadioGroup() {
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

    private void setTxtPesquisaProduto(){
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
