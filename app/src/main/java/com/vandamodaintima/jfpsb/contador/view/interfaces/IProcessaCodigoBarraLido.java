package com.vandamodaintima.jfpsb.contador.view.interfaces;

public interface IProcessaCodigoBarraLido {
    void showAlertaQuantidadeProduto();
    void setAlertProdutoGradeNaoEncontrado();
    void showAlertaProdutoGradeNaoEncontrado();
    void abrirVisualizarProdutoGradeContagem(String codigo);
}
