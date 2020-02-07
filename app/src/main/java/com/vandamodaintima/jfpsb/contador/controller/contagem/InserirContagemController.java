package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.model.manager.TipoContagemManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.Date;
import java.util.List;

public class InserirContagemController {
    private CadastrarView view;
    private ContagemManager contagemManager;
    private LojaManager lojaManager;
    private TipoContagemManager tipoContagemManager;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemManager = new ContagemManager(conexaoBanco);
        lojaManager = new LojaManager(conexaoBanco);
        tipoContagemManager = new TipoContagemManager(conexaoBanco);
    }

    public void cadastrar() {
        contagemManager.getContagem().setData(new Date());
        contagemManager.getContagem().setFinalizada(false);

        Boolean result = contagemManager.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem Cadastrada Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Contagem");
        }
    }

    public void carregaLoja(Object o) {
        if (o instanceof Loja)
            contagemManager.getContagem().setLoja((Loja) o);
    }

    public void carregaTipoContagem(Object o) {
        if (o instanceof TipoContagem)
            contagemManager.getContagem().setTipoContagem((TipoContagem) o);
    }

    public List<Loja> getLojas() {
        return lojaManager.listar();
    }

    public List<TipoContagem> getTipoContagens() {
        return tipoContagemManager.listar();
    }
}
