package com.vandamodaintima.jfpsb.contador.controller.contagem.contagemproduto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarContagemProdutoController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private ContagemProduto model;

    public AlterarDeletarContagemProdutoController(AlterarDeletarView view, ConexaoBanco conexaoBanco, String idContagemProduto) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        model = new ContagemProduto(conexaoBanco);
        model.load(idContagemProduto);
    }

    public ContagemProduto getModel() {
        return model;
    }

    public void atualizar() {
        if(model.getQuant() == 0) {
            view.mensagemAoUsuario("Quantidade não pode ser zero");
            return;
        }

        boolean result = model.atualizar();

        if(result) {
            view.mensagemAoUsuario("Contagem de produto foi salva com sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao atualizar contagem de produto");
        }
    }

    public void deletar() {
        Boolean result = model.deletar();
        if (result) {
            view.mensagemAoUsuario("Contagem de produto deletada com sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao deletar contagem de produto");
        }
    }
}
