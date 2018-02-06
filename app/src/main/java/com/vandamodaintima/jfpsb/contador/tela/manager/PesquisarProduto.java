package com.vandamodaintima.jfpsb.contador.tela.manager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vandamodaintima.jfpsb.contador.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PesquisarProduto extends Fragment {


    public PesquisarProduto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pesquisar_produto, container, false);
    }

}
