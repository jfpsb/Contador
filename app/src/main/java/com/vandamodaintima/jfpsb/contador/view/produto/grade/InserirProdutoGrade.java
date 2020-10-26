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
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.TelaCadastro;
import com.vandamodaintima.jfpsb.contador.view.grade.CadastrarGrade;
import com.vandamodaintima.jfpsb.contador.view.grade.CadastrarTipoGrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class InserirProdutoGrade extends TelaCadastro {
    private Button btnInserirFormacaoAtual;
    private Button btnInserir;
    private Button btnLerCodigoBarras;
    private Button btnCadastrarGrade;
    private Button btnCadastrarTipoGrade;
    private EditText txtCodBarra;
    private EditText txtPreco;
    private Spinner spinnerTipoGrade;
    private Spinner spinnerGrade;
    private ListView listViewGradeFormacaoAtual;
    private GradeArrayAdapter gradeArrayAdapter;
    private SpinnerGradeAdapter spinnerGradeAdapter;
    private SpinnerTipoGradeAdapter spinnertipoGradeAdapter;

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
        txtPreco = telaCadastroView.findViewById(R.id.txtPreco);
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
            controller.getModel().setGrades(new ArrayList<>(gradeArrayAdapter.getObjects()));

            Boolean precoRes = controller.setPreco(txtPreco.getText().toString());
            if (!precoRes) {
                txtPreco.setText("0.0");
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

        gradeArrayAdapter = new GradeArrayAdapter(Objects.requireNonNull(getContext()), new ArrayList<>());
        listViewGradeFormacaoAtual.setAdapter(gradeArrayAdapter);

        listViewGradeFormacaoAtual.setOnItemClickListener((parent, view, position, id) -> {
            gradeArrayAdapter.getObjects().remove(position);
            gradeArrayAdapter.notifyDataSetChanged();
        });

        spinnertipoGradeAdapter = new SpinnerTipoGradeAdapter(getContext(), controller.getTipoGrades());
        spinnerTipoGrade.setAdapter(spinnertipoGradeAdapter);

        spinnerGradeAdapter = new SpinnerGradeAdapter(getContext(), controller.getGradesPorTipo(spinnertipoGradeAdapter.getItem(0)));
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
            gradeArrayAdapter.add(g);
        });

        return telaCadastroView;
    }

    @Override
    public void limparCampos() {
        txtCodBarra.getText().clear();
        txtPreco.getText().clear();
        gradeArrayAdapter.clear();
        gradeArrayAdapter.notifyDataSetChanged();
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
                    spinnertipoGradeAdapter.getObjects().clear();
                    spinnertipoGradeAdapter.getObjects().addAll(controller.getTipoGrades());
                    spinnertipoGradeAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    public List<ProdutoGrade> getProdutoGrades() {
        return controller.getProdutoGrades();
    }
}
