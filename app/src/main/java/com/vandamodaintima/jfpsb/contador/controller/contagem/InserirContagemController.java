package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;

import java.util.Date;
import java.util.List;

public class InserirContagemController implements IController {
    private ConexaoBanco conexaoBanco;
    private CadastrarView view;
    private Contagem contagemModel;
    private Loja lojaModel;
    private TipoContagem tipoContagemModel;

    public InserirContagemController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        contagemModel = new Contagem(conexaoBanco);
        lojaModel = new Loja(conexaoBanco);
        tipoContagemModel = new TipoContagem(conexaoBanco);
    }

    public void salvar() {
        contagemModel.setId(new Date().getTime());

        if(contagemModel.getLoja() == null) {
            view.mensagemAoUsuario("Escolha Uma Loja!");
            return;
        }

        if(contagemModel.getTipoContagem() == null) {
            view.mensagemAoUsuario("Escolha Um Tipo de Contagem!");
            return;
        }

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

    @Override
    public void reset() {
        contagemModel = new Contagem(conexaoBanco);
    }
}
