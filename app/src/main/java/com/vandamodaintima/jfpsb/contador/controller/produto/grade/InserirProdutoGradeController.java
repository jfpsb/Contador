package com.vandamodaintima.jfpsb.contador.controller.produto.grade;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Grade;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.TipoGrade;
import com.vandamodaintima.jfpsb.contador.view.produto.grade.InserirProdutoGrade;

import java.util.ArrayList;
import java.util.List;

public class InserirProdutoGradeController implements IController {
    private InserirProdutoGrade view;
    private ProdutoGrade model;
    private ConexaoBanco conexaoBanco;
    private TipoGrade tipoGrade;
    private Grade grade;
    private List<ProdutoGrade> produtoGrades;

    public InserirProdutoGradeController(InserirProdutoGrade view, ConexaoBanco conexaoBanco, ArrayList<ProdutoGrade> produtoGrades) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        this.produtoGrades = produtoGrades;
        model = new ProdutoGrade(conexaoBanco);
        tipoGrade = new TipoGrade(conexaoBanco);
        grade = new Grade(conexaoBanco);
    }

    @Override
    public void reset() {
        model = new ProdutoGrade(conexaoBanco);
    }

    @Override
    public void salvar() {
        if (model.getCodBarra().trim().isEmpty()) {
            view.mensagemAoUsuario("Código de Barras Não Pode Ser Vazio");
            return;
        }

        if (model.getPreco_custo() <= 0.0) {
            view.mensagemAoUsuario("Preço Não Pode Ser Zero ou Menor que Zero");
            return;
        }

        if (model.getGrades() == null || model.getGrades().size() == 0) {
            view.mensagemAoUsuario("Escolha Pelo Menos Uma Grade");
            return;
        }

        produtoGrades.add(model);
        view.mensagemAoUsuario("Grade de Produto Criada Com Sucesso");
        view.aposCadastro();
    }

    public List<TipoGrade> getTipoGrades() {
        return tipoGrade.listar();
    }

    public List<Grade> getGradesPorTipo(TipoGrade tipoGrade) {
        return grade.listarPorTipoGrade(tipoGrade);
    }

    public Boolean setPrecoVenda(String preco) {
        try {
            model.setPreco_venda(Double.parseDouble(preco));
            return true;
        } catch (NumberFormatException ne) {
            model.setPreco_venda(0.0);
            return false;
        }
    }

    public Boolean setPrecoCusto(String preco) {
        try {
            model.setPreco_custo(Double.parseDouble(preco));
            return true;
        } catch (NumberFormatException ne) {
            model.setPreco_custo(0.0);
            return false;
        }
    }

    public ProdutoGrade getModel() {
        return model;
    }

    public List<ProdutoGrade> getProdutoGrades() {
        return produtoGrades;
    }
}
