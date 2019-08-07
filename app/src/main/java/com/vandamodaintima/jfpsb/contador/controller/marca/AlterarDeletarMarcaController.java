package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarMarcaController {
    private AlterarDeletarView view;
    private MarcaModel marcaModel;

    public AlterarDeletarMarcaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaModel = new MarcaModel(conexaoBanco);
    }

    public void atualizar(String nome) {
        if (nome.trim().isEmpty()) {
            view.mensagemAoUsuario("Nome de Marca Não Pode Ser Vazio");
            return;
        }

        marcaModel.setNome(nome);

        Boolean result = marcaModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Marca Atualizada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Marca");
        }
    }

    public void deletar() {
        Boolean result = marcaModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Marca Deletada Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Marca");
        }
    }

    public void carregaMarca(String nome) {
        marcaModel.load(nome);
    }

    public String getNome() {
        return marcaModel.getNome();
    }
}
