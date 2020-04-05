package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.ArrayList;

public class AlterarDeletarLojaController implements IController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private Loja lojaModel;

    public AlterarDeletarLojaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaModel = new Loja(conexaoBanco);
    }

    public void atualizar() {
        if (lojaModel.getNome().trim().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Ser Vazio");
            return;
        }

        if (lojaModel.getMatriz().getCnpj().equals("0")) {
            lojaModel.setMatriz(null);
        }

        Boolean result = lojaModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Loja Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Loja");
        }
    }

    public void deletar() {
        Boolean result = lojaModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Loja Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Loja");
        }
    }

    public ArrayList<Loja> getMatrizes() {
        ArrayList<Loja> matrizes = new ArrayList<>();
        Loja loja = new Loja();
        loja.setCnpj("0");
        loja.setNome("SEM MATRIZ");

        matrizes.add(loja);
        matrizes.addAll(lojaModel.listarMatrizes());

        return matrizes;
    }

    public void carregaMatriz(Object o) {
        if (o instanceof Loja)
            lojaModel.setMatriz((Loja) o);
    }

    public int getMatrizIndex(String cnpj) {
        int index = 0, aux = 1;

        for (Loja loja : lojaModel.listarMatrizes()) {
            if (loja.getCnpj().equals(cnpj)) {
                index = aux;
                break;
            }
            aux++;
        }

        return index;
    }

    public void carregaLoja(String id) {
        lojaModel.load(id);
    }

    public Loja getLoja() {
        return lojaModel;
    }

    @Override
    public void reset() {
        lojaModel = new Loja(conexaoBanco);
    }
}
