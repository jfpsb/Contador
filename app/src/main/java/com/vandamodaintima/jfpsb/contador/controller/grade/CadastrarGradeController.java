package com.vandamodaintima.jfpsb.contador.controller.grade;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.grade.CadastrarGrade;

import java.util.List;

public class CadastrarGradeController implements IController {
    private Grade model;
    private TipoGrade tipoGrade;
    private ConexaoBanco conexaoBanco;
    CadastrarGrade view;

    public CadastrarGradeController(CadastrarGrade view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        model = new Grade(conexaoBanco);
        tipoGrade = new TipoGrade(conexaoBanco);
    }

    @Override
    public void reset() {
        model = new Grade(conexaoBanco);
    }

    @Override
    public void salvar() {
        if (model.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de Grade Não Pode Ser Vazio");
            return;
        }

        if (model.getTipoGrade() == null) {
            view.mensagemAoUsuario("Tipo de Grade Não Foi Selecionado");
            return;
        }

        Boolean result = model.salvar();

        if (result) {
            view.mensagemAoUsuario("Grade Cadastrada Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro ao Cadastrar Grade");
        }
    }

    public Grade getModel() {
        return model;
    }

    public List<TipoGrade> getTipoGrades() {
        return tipoGrade.listar();
    }
}
