package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.util.List;

public class InserirContagemController {
    private CadastrarView view;
    private Contagem contagemModel;
    private Loja lojaModel;
    private TipoContagem tipoContagemModel;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        contagemModel = new Contagem(conexaoBanco);
        lojaModel = new Loja(conexaoBanco);
        tipoContagemModel = new TipoContagem(conexaoBanco);
    }

    public void cadastrar() {
        contagemModel.setData(Instant.now().atZone(ZoneId.systemDefault()));
        contagemModel.setFinalizada(false);

        Boolean result = contagemModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Contagem Cadastrada Com Sucesso");
            view.aposCadastro();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Contagem");
        }
    }

    public void carregaLoja(Object o) {
        if (o instanceof Loja)
            contagemModel.setLoja((Loja) o);
    }

    public void carregaTipoContagem(Object o) {
        if (o instanceof TipoContagem)
            contagemModel.setTipoContagem((TipoContagem) o);
    }

    public List<Loja> getLojas() {
        return lojaModel.listar();
    }

    public List<TipoContagem> getTipoContagens() {
        return tipoContagemModel.listar();
    }
}
