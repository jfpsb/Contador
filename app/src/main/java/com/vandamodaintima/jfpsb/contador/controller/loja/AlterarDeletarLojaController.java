package com.vandamodaintima.jfpsb.contador.controller.loja;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.manager.LojaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.ArrayList;

public class AlterarDeletarLojaController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private LojaManager lojaManager;

    public AlterarDeletarLojaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        lojaManager = new LojaManager(conexaoBanco);
    }

    public void atualizar(String nome) {
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome Não Pode Ser Vazio");
            return;
        }

        if (lojaManager.getLoja().getMatriz().getCnpj().equals("0")) {
            lojaManager.getLoja().setMatriz(null);
        }

        lojaManager.getLoja().setNome(nome);

        Boolean result = lojaManager.atualizar(lojaManager.getLoja().getCnpj());

        if (result) {
            view.mensagemAoUsuario("Loja Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Loja");
        }
    }

    public void deletar() {
        Boolean result = lojaManager.deletar();

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
        matrizes.addAll(lojaManager.listarMatrizes());

        return matrizes;
    }

    public void carregaMatriz(Object o) {
        if (o instanceof Loja)
            lojaManager.getLoja().setMatriz((Loja) o);
    }

    public int getMatrizIndex(String cnpj) {
        int index = 0, aux = 1;

        for (Loja loja : lojaManager.listarMatrizes()) {
            if (loja.getCnpj().equals(cnpj)) {
                index = aux;
                break;
            }
            aux++;
        }

        return index;
    }

    public void carregaLoja(String id) {
        lojaManager.load(id);
    }

    public Loja getLoja() {
        return lojaManager.getLoja();
    }
}
