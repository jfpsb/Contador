package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemProdutoManager;
import com.vandamodaintima.jfpsb.contador.model.manager.TipoContagemManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.List;

public class AlterarDeletarContagemController {
    AlterarDeletarView view;
    private ContagemManager contagemManager;
    private ContagemProdutoManager contagemProdutoManager;
    private TipoContagemManager tipoContagemManager;
    private ConexaoBanco conexaoBanco;

    public AlterarDeletarContagemController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        contagemManager = new ContagemManager(conexaoBanco);
        contagemProdutoManager = new ContagemProdutoManager(conexaoBanco);
        tipoContagemManager = new TipoContagemManager(conexaoBanco);
    }

    public void atualizar(Boolean finalizada) {
        contagemManager.getContagem().setFinalizada(finalizada);

        Boolean result = contagemManager.atualizar(contagemManager.getContagem().getLoja().getCnpj(), contagemManager.getContagem().getDataParaSQLite());

        if (result) {
            view.mensagemAoUsuario("Contagem Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Contagem");
        }
    }

    public void deletar() {
        Boolean result = contagemManager.deletar();

        if (result) {
            view.mensagemAoUsuario("Contagem Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Contagem");
        }
    }

    public void carregaContagem(String loja, String data) {
        contagemManager.load(loja, data);
    }

    public void carregaTipoContagem(Object o) {
        if (o instanceof TipoContagem)
            contagemManager.getContagem().setTipoContagem((TipoContagem) o);
    }

    public String getFullDataString() {
        return contagemManager.getContagem().getFullDataString();
    }

    public String getLojaNome() {
        return contagemManager.getContagem().getLoja().getNome();
    }

    public String getLoja() {
        return contagemManager.getContagem().getLoja().getCnpj();
    }

    public String getData() {
        return contagemManager.getContagem().getDataParaSQLite();
    }

    public boolean getFinalizada() {
        return  contagemManager.getContagem().getFinalizada();
    }

    public int getTipoContagemIndex() {
        int index = 0;

        for(TipoContagem tipo : getTipoContagens()) {
            if(contagemManager.getContagem().getTipoContagem().getId() == tipo.getId()) {
                break;
            }
            index++;
        }

        return index;
    }

    public List<TipoContagem> getTipoContagens() {
        return tipoContagemManager.listar();
    }

    public void exportarParaExcel(String dir) {
        new ExportarContagemProdutoParaExcel(view.getContext()).execute(dir, contagemProdutoManager.listarPorContagemGroupByProduto(contagemManager.getContagem()));
    }
}
