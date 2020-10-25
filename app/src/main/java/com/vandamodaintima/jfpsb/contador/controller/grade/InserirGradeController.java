package com.vandamodaintima.jfpsb.contador.controller.grade;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.view.produto.grade.InserirProdutoGrade;

public class InserirGradeController implements IController {

    private InserirProdutoGrade view;
    private Grade model;
    private ConexaoBanco conexaoBanco;

    public InserirGradeController(InserirProdutoGrade view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
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
            view.mensagemAoUsuario("Sucesso ao Cadastrar Grade");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Grade Não Foi Cadastrada");
        }
    }

    @Override
    public void reset() {
        model = new Grade(conexaoBanco);
    }

    public Grade getModel() {
        return model;
    }
}
