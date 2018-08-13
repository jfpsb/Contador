package com.vandamodaintima.jfpsb.contador.tela.manager.codbarrafornecedor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.entidade.CodBarraFornecedor;
import com.vandamodaintima.jfpsb.contador.entidade.Produto;
import com.vandamodaintima.jfpsb.contador.tela.TabLayoutActivityBase;
import com.vandamodaintima.jfpsb.contador.tela.FragmentBase;

public class CadastrarCodBarraFornecedor extends FragmentBase {
    private Produto produto;

    private EditText txtCodBarraProduto;
    private EditText txtDescricao;
    private EditText txtCodBarra;
    private Button btnInserir;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(savedInstanceState == null)
            savedInstanceState = new Bundle();

        savedInstanceState.putInt("layout", R.layout.fragment_inserir_cod_barra_fornecedor);

        produto = (Produto) getArguments().getSerializable("produto");

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void setManagers() {

    }

    @Override
    protected void setViews() {
        if(produto.getCod_barra() != null && produto.getDescricao() != null) {
            ViewStub stub = viewInflate.findViewById(R.id.inserirCodBarraFornecedorStub);
            stub.setLayoutResource(R.layout.dado_produto_cod_barra_fornecedor);
            stub.inflate();

            txtCodBarraProduto = viewInflate.findViewById(R.id.txtCodBarraProduto);
            txtDescricao = viewInflate.findViewById(R.id.txtDescricao);

            txtCodBarraProduto.setText(produto.getCod_barra());
            txtDescricao.setText(produto.getDescricao());
        }

        txtCodBarra = viewInflate.findViewById(R.id.txtCodBarra);

        setBtnInserir();
    }

    private void setBtnInserir() {
        btnInserir = viewInflate.findViewById(R.id.btnInserir);

        btnInserir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cod_barra_fornecedor = txtCodBarra.getText().toString();

                try {
                    if (cod_barra_fornecedor.isEmpty())
                        throw new Exception("Campo de Código de Barras de Fornecedor Não Pode Ficar Vazio!");

                    CodBarraFornecedor codBarraFornecedor = new CodBarraFornecedor();

                    codBarraFornecedor.setProduto(produto);
                    codBarraFornecedor.setCodigo(cod_barra_fornecedor);

                    if (produto.getCod_barra_fornecedor().contains(codBarraFornecedor))
                        throw new Exception("Este Código Já Foi Adicionado");

                    produto.getCod_barra_fornecedor().add(codBarraFornecedor);

                    Toast.makeText(getContext(), "Código de Barras de Fornecedor Adicionado à Lista", Toast.LENGTH_SHORT).show();

                    Fragment fragment = ((TabLayoutActivityBase) getActivity()).getPagerAdapter().getItem(0);
                    ((ListarCodBarraFornecedor) fragment).populaListView();

                    txtCodBarra.getText().clear();
                } catch (Exception e) {
                    Log.e(LOG, e.getMessage(), e);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
