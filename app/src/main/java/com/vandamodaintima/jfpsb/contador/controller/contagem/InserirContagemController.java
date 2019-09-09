package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.manager.ContagemManager;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import java.util.Date;
import java.util.List;

public class InserirContagemController {
    private CadastrarView view;
    private ContagemManager contagemManager;
    private LojaManager lojaManager;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemManager = new ContagemManager(conexaoBanco);
        lojaManager = new LojaManager(conexaoBanco);
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

    public List<Loja> getLojas() {
        return lojaManager.listar();
    }
}
