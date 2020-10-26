package com.vandamodaintima.jfpsb.contador.controller.grade;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.grade.CadastrarTipoGrade;

public class CadastrarTipoGradeController implements IController {
    private TipoGrade model;
    private ConexaoBanco conexaoBanco;
    private CadastrarTipoGrade view;

    public CadastrarTipoGradeController(CadastrarTipoGrade view, ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        this.view = view;
        model = new TipoGrade(conexaoBanco);
    }

    @Override
    public void reset() {
        model = new TipoGrade();
    }

    @Override
    public void salvar() {
        if (model.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de Tipo de Grade Não Pode Ser Vazio");
            return;
        }

        Boolean result = model.salvar();

        if (result) {
            view.mensagemAoUsuario("Tipo de Grade Cadastrar Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Tipo de Grade");
        }
    }

    public TipoGrade getModel() {
        return model;
    }
}
