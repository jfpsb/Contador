package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.grade.GradeArrayAdapter;
import com.vandamodaintima.jfpsb.contador.controller.grade.SpinnerGradeAdapter;
import com.vandamodaintima.jfpsb.contador.controller.grade.SpinnerTipoGradeAdapter;
import com.vandamodaintima.jfpsb.contador.controller.produto.grade.InserirProdutoGradeController;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.SubGrade;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;
import com.vandamodaintima.jfpsb.contador.view.grade.CadastrarGrade;
import com.vandamodaintima.jfpsb.contador.view.grade.CadastrarTipoGrade;

import java.util.ArrayList;
import java.util.List;

public class InserirProdutoGrade extends TelaCadastro {
    private Button btnInserirFormacaoAtual;
    private Button btnInserir;
    private Button btnLerCodigoBarras;
    private Button btnCadastrarGrade;
    private Button btnCadastrarTipoGrade;
    private EditText txtCodBarra;
    private EditText txtPrecoVenda;
    private EditText txtPrecoCusto;
    private Spinner spinnerTipoGrade;
    private Spinner spinnerGrade;
    private ListView listViewGradeFormacaoAtual;
    private GradeArrayAdapter gradeFormacaoAtualAdapter;
    private SpinnerGradeAdapter spinnerGradeAdapter;
    private SpinnerTipoGradeAdapter spinnerTipoGradeAdapter;

    private InserirProdutoGradeController controller;

    private static final int TELA_LER_CODIGO_BARRAS = 1;
    private static final int CADASTRAR_GRADE = 2;
    private static final int CADASTRAR_TIPO_GRADE = 3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        telaCadastroView = inflater.inflate(R.layout.fragment_inserir_produto_grade, container, false);

        btnLerCodigoBarras = telaCadastroView.findViewById(R.id.btnLerCodigoBarras);
        btnInserir = telaCadastroView.findViewById(R.id.btnInserir);
        btnInserirFormacaoAtual = telaCadastroView.findViewById(R.id.btnInserirFormacaoAtualGrade);
        btnCadastrarGrade = telaCadastroView.findViewById(R.id.btnCadastrarGrade);
        btnCadastrarTipoGrade = telaCadastroView.findViewById(R.id.btnCadastrarTipoGrade);
        txtPrecoVenda = telaCadastroView.findViewById(R.id.txtPrecoVenda);
        txtPrecoCusto = telaCadastroView.findViewById(R.id.txtPrecoCusto);
        spinnerGrade = telaCadastroView.findViewById(R.id.spinnerGrade);
        spinnerTipoGrade = telaCadastroView.findViewById(R.id.spinnerTipoGrade);
        txtCodBarra = telaCadastroView.findViewById(R.id.txtCodBarra);
        listViewGradeFormacaoAtual = telaCadastroView.findViewById(R.id.listViewGradeFormacaoAtual);

        btnLerCodigoBarras.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TelaLerCodigoBarrasCadastrarProduto.class);
            startActivityForResult(intent, TELA_LER_CODIGO_BARRAS);
        });

        btnInserir.setOnClickListener(v -> {
            controller.getModel().setCodBarra(txtCodBarra.getText().toString());

            List<SubGrade> subGrades = new ArrayList<>();
            for(Grade g : gradeFormacaoAtualAdapter.getObjects()) {
                SubGrade subGrade = new SubGrade(conexaoBanco);
                subGrade.setGrade(g);
                subGrade.setProdutoGrade(controller.getModel());
                subGrades.add(subGrade);
            }

            controller.getModel().setSubGrades(new ArrayList<>(subGrades));

            Boolean precoVenda = controller.setPrecoVenda(txtPrecoVenda.getText().toString());
            if (!precoVenda) {
                txtPrecoVenda.setText("0.0");
            }

            Boolean precoCusto = controller.setPrecoCusto(txtPrecoCusto.getText().toString());
            if (!precoCusto) {
                txtPrecoVenda.setText("0.0");
            }

            controller.salvar();
        });

        btnCadastrarGrade.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CadastrarGrade.class);
            startActivityForResult(intent, CADASTRAR_GRADE);
        });

        btnCadastrarTipoGrade.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), CadastrarTipoGrade.class);
            startActivityForResult(intent, CADASTRAR_TIPO_GRADE);
        });

        conexaoBanco = new ConexaoBanco(getContext());
        controller = new InserirProdutoGradeController(this, conexaoBanco, (ArrayList<ProdutoGrade>) getArguments().getSerializable("produtoGrades"));

        gradeFormacaoAtualAdapter = new GradeArrayAdapter(requireContext(), new ArrayList<Grade>());
        listViewGradeFormacaoAtual.setAdapter(gradeFormacaoAtualAdapter);

        listViewGradeFormacaoAtual.setOnItemClickListener((parent, view, position, id) -> {
            gradeFormacaoAtualAdapter.getObjects().remove(position);
            gradeFormacaoAtualAdapter.notifyDataSetChanged();
        });

        spinnerTipoGradeAdapter = new SpinnerTipoGradeAdapter(requireContext(), controller.getTipoGrades());
        spinnerTipoGrade.setAdapter(spinnerTipoGradeAdapter);

        spinnerGradeAdapter = new SpinnerGradeAdapter(requireContext(), controller.getGradesPorTipo(spinnerTipoGradeAdapter.getItem(0)));
        spinnerGrade.setAdapter(spinnerGradeAdapter);

        spinnerTipoGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TipoGrade tipoGrade = (TipoGrade) spinnerTipoGrade.getSelectedItem();
                spinnerGradeAdapter.getObjects().clear();
                spinnerGradeAdapter.getObjects().addAll(controller.getGradesPorTipo(tipoGrade));
                spinnerGradeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnInserirFormacaoAtual.setOnClickListener(v -> {
            Grade g = (Grade) spinnerGrade.getSelectedItem();
            gradeFormacaoAtualAdapter.add(g);
        });

        return telaCadastroView;
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
        txtPrecoVenda.getText().clear();
        gradeFormacaoAtualAdapter.clear();
        gradeFormacaoAtualAdapter.notifyDataSetChanged();
        controller.reset();
    }

    @Override
    public void focoEmViewInicial() {
        txtCodBarra.requestFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TELA_LER_CODIGO_BARRAS:
                if (resultCode == Activity.RESULT_OK) {
                    String codigo = data.getStringExtra("codigo_lido");
                    txtCodBarra.setText(codigo);
                }
                break;
            case CADASTRAR_GRADE:
                if(resultCode == Activity.RESULT_OK) {
                    TipoGrade tipoGrade = (TipoGrade) spinnerTipoGrade.getSelectedItem();
                    spinnerGradeAdapter.getObjects().clear();
                    spinnerGradeAdapter.getObjects().addAll(controller.getGradesPorTipo(tipoGrade));
                    spinnerGradeAdapter.notifyDataSetChanged();
                }
                break;
            case CADASTRAR_TIPO_GRADE:
                if(resultCode == Activity.RESULT_OK) {
                    spinnerTipoGradeAdapter.getObjects().clear();
                    spinnerTipoGradeAdapter.getObjects().addAll(controller.getTipoGrades());
                    spinnerTipoGradeAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    public List<ProdutoGrade> getProdutoGrades() {
        return controller.getProdutoGrades();
    }
}
