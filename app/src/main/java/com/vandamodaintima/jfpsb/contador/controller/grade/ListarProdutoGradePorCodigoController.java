package com.vandamodaintima.jfpsb.contador.controller.grade;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProdutoGrade;

import java.util.List;

public class ListarProdutoGradePorCodigoController {
    private ConexaoBanco conexaoBanco;
    private ProdutoGrade produtoGrade;
    private DAOProdutoGrade daoProdutoGrade;

    public ListarProdutoGradePorCodigoController(ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        daoProdutoGrade = new DAOProdutoGrade(conexaoBanco);
    }

    public ProdutoGrade getProdutoGrade() {
        return produtoGrade;
    }

    public void setProdutoGrade(ProdutoGrade produtoGrade) {
        this.produtoGrade = produtoGrade;
    }

    public List<ProdutoGrade> getProdutoGrades(String codigo) {
        return daoProdutoGrade.listarPorCodBarra(codigo);
    }
}
