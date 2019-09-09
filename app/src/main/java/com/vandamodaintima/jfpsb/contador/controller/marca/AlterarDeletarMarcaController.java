package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.manager.MarcaManager;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarMarcaController {
    private AlterarDeletarView view;
    private MarcaManager marcaManager;

    public AlterarDeletarMarcaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaManager = new MarcaManager(conexaoBanco);
    }

    public void atualizar(String nome) {
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de Marca Não Pode Ser Vazio");
            return;
        }

        String id = marcaManager.getMarca().getNome();
        marcaManager.getMarca().setNome(nome);

        Boolean result = marcaManager.atualizar(id);

        if (result) {
            view.mensagemAoUsuario("Marca Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Marca");
        }
    }

    public void deletar() {
        Boolean result = marcaManager.deletar();

        if (result) {
            view.mensagemAoUsuario("Marca Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Marca");
        }
    }

    public void carregaMarca(String nome) {
        marcaManager.load(nome);
    }

    public Marca getMarca() {
        return marcaManager.getMarca();
    }
}
