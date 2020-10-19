package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.os.Bundle;
import android.view.ViewStub;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;

public class GerenciarGrades extends TabLayoutBaseView {
    private InserirGrade inserirGrade;
    private VisualizarGrades visualizarGrades;

    public GerenciarGrades() {
        super(new String[]{"Inserir", "Visualizar"});
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_tela_tablayout);
        stub.inflate();

        ViewStub stub2 = findViewById(R.id.telaLayoutViewStub);
        stub2.setLayoutResource(R.layout.activity_tela_tablayout);
        stub2.inflate();

        inserirGrade = new InserirGrade();
        visualizarGrades = new VisualizarGrades();

        setViewPagerTabLayout(inserirGrade, visualizarGrades);
    }
}
