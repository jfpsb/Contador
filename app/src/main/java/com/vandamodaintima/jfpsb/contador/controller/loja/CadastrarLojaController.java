package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ICadastrarView;

import java.util.ArrayList;

public class CadastrarLojaController implements IController {
    private ICadastrarView view;
    private Loja lojaModel;
    private ConexaoBanco conexaoBanco;

    public CadastrarLojaController(ICadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaModel = new Loja(conexaoBanco);
    }

    public void salvar() {
        if (lojaModel.getCnpj().isEmpty()) {
            view.mensagemAoUsuario("CNPJ Não Pode Estar Vazio");
            return;
        }

        if (lojaModel.getNome().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Estar Vazio");
            return;
        }

        if (lojaModel.getMatriz().getCnpj() == null) {
            lojaModel.setMatriz(null);
        }

        Boolean result = lojaModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Loja Cadastrada Com Sucesso!");
            view.aposCadastro();
            view.limparCampos();
        } else {
            view.mensagemAoUsuario("Erro Ao Cadastrar Loja");
        }
    }

    public ArrayList<Loja> getMatrizes() {
        ArrayList<Loja> matrizes = new ArrayList<>();
        Loja loja = new Loja("SEM MATRIZ");
        matrizes.add(loja);
        matrizes.addAll(lojaModel.listarMatrizes());
        return matrizes;
    }

    public void carregaMatriz(Object o) {
        if (o instanceof Loja)
            lojaModel.setMatriz((Loja) o);
    }

    public Loja getLoja() {
        return lojaModel;
    }

    @Override
    public void reset() {
        lojaModel = new Loja(conexaoBanco);
    }
}
