package com.vandamodaintima.jfpsb.contador.view.contagem;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.InserirContagemController;
import com.vandamodaintima.jfpsb.contador.controller.contagem.SpinnerTipoContagemAdapter;
import com.vandamodaintima.jfpsb.contador.controller.loja.SpinnerLojaAdapter;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;

import java.util.Objects;

public class CadastrarContagem extends TelaCadastro {

    private Button btnCadastrar;
    private Spinner spinnerLoja;
    private Spinner spinnerTipoContagem;
    private InserirContagemController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        telaCadastroView = inflater.inflate(R.layout.fragment_cadastrar_contagem, container, false);

        btnCadastrar = telaCadastroView.findViewById(R.id.btnCadastrar);
        spinnerLoja = telaCadastroView.findViewById(R.id.spinnerLoja);
        spinnerTipoContagem = telaCadastroView.findViewById(R.id.spinnerTipoContagem);

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new InserirContagemController(this, conexaoBanco);

        btnCadastrar.setOnClickListener(view -> {
            controller.carregaLoja(spinnerLoja.getSelectedItem());
            controller.carregaTipoContagem(spinnerTipoContagem.getSelectedItem());
            controller.salvar();
        });

        ArrayAdapter<Loja> spinnerLojaAdapter = new SpinnerLojaAdapter(requireContext(), controller.getLojas());
        spinnerLoja.setAdapter(spinnerLojaAdapter);

        ArrayAdapter<TipoContagem> spinnerTipoContagemAdapter = new SpinnerTipoContagemAdapter(getContext(), controller.getTipoContagens());
        spinnerTipoContagem.setAdapter(spinnerTipoContagemAdapter);

        return telaCadastroView;
    }

    @Override
    public void limparCampos() {
        spinnerLoja.setSelection(0);
        spinnerTipoContagem.setSelection(0);
        controller.reset();
    }

    @Override
    public void focoEmViewInicial() {
        spinnerLoja.requestFocus();
    }

    @Override
    public void setListViewAdapter(ListAdapter adapter) {

    }
}
