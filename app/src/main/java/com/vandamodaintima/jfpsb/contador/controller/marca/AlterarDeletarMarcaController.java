package com.vandamodaintima.jfpsb.contador.controller.marca;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarMarcaController {
    private AlterarDeletarView view;
    private Marca marcaModel;
    private Fornecedor fornecedorModel;

    public AlterarDeletarMarcaController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        marcaModel = new Marca(conexaoBanco);
        fornecedorModel = new Fornecedor(conexaoBanco);
    }

    public void atualizar() {

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

    public Marca getMarca() {
        return marcaModel;
    }

    public void setFornecedorNull() {
        marcaModel.setFornecedor(null);
    }

    public void carregaFornecedor(Fornecedor fornecedor) {
        marcaModel.setFornecedor(fornecedor);
    }
}
