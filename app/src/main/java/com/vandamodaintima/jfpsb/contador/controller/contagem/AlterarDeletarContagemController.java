package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.net.Uri;
import android.os.AsyncTask;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.ExportarParaExcel;
import com.vandamodaintima.jfpsb.contador.controller.arquivo.ExcelContagemProdutoStrategy;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.List;

public class AlterarDeletarContagemController {
    AlterarDeletarView view;
    private Contagem contagemModel;
    private ContagemProduto contagemProdutoModel;
    private TipoContagem tipoContagemModel;
    private ConexaoBanco conexaoBanco;

    public AlterarDeletarContagemController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        contagemModel = new Contagem(conexaoBanco);
        contagemProdutoModel = new ContagemProduto(conexaoBanco);
        tipoContagemModel = new TipoContagem(conexaoBanco);
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

    public void carregaTipoContagem(Object o) {
        if (o instanceof TipoContagem)
            contagemModel.setTipoContagem((TipoContagem) o);
    }

    public Contagem getContagem() {
        return contagemModel;
    }

    public int getTipoContagemIndex() {
        int index = 0;

        for (TipoContagem tipo : getTipoContagens()) {
            if (contagemModel.getTipoContagem().getId() == tipo.getId()) {
                break;
            }
            index++;
        }

        return index;
    }

    public List<TipoContagem> getTipoContagens() {
        return tipoContagemModel.listar();
    }

    public void exportarParaExcel(Uri uri) {
        List<ContagemProduto> listagemPorProduto = contagemProdutoModel.listarPorContagemGroupByProduto(contagemModel);
        List<ContagemProduto> listagemPorGrade = contagemProdutoModel.listarPorContagemGroupByGrade(contagemModel);

        new ExportarParaExcel<>(view.getContext(), new ExcelContagemProdutoStrategy(), listagemPorProduto, listagemPorGrade).execute(uri);
    }
}
