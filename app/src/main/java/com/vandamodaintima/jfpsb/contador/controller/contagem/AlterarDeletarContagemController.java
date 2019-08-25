package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemModel;
import com.vandamodaintima.jfpsb.contador.model.ContagemProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarContagemController {
    AlterarDeletarView view;
    private ContagemModel contagemModel;
    private ContagemProdutoModel contagemProdutoModel;
    private ConexaoBanco conexaoBanco;

    public AlterarDeletarContagemController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        contagemModel = new ContagemModel(conexaoBanco);
        contagemProdutoModel = new ContagemProdutoModel(conexaoBanco);
    }

    public void atualizar(Boolean finalizada) {
        contagemModel.setFinalizada(finalizada);

        Boolean result = contagemModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Contagem Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Contagem");
        }
    }

    public void deletar() {
        Boolean result = contagemModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem");
        }
    }

    public void carregaContagem(String loja, String data) {
        contagemModel.load(loja, data);
    }

    public String getFullDataString() {
        return contagemModel.getFullDataString();
    }

    public String getLojaNome() {
        return contagemModel.getLoja().getNome();
    }

    public String getLoja() {
        return contagemModel.getLoja().getCnpj();
    }

    public String getData() {
        return contagemModel.getDataParaSQLite();
    }

    public void exportarParaExcel(String dir) {
        new ExportarContagemProdutoParaExcel(view.getContext()).execute(dir, contagemProdutoModel.listarPorContagemGroupByProduto(contagemModel));
    }
}
