package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.ArrayList;

public class AlterarDeletarProdutoController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private Produto produtoModel;

    public AlterarDeletarProdutoController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new Produto(conexaoBanco);
    }

    public void atualizar() {
        if (produtoModel.getDescricao().trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição do Produto Não Pode Ser Vazia");
            return;
        }

        if (produtoModel.getPreco() == 0) {
            view.mensagemAoUsuario("Preço do Produto Não Pode Ser Zero");
            return;
        }

        Boolean result = produtoModel.atualizar();

        if (result) {
            view.mensagemAoUsuario("Produto Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto");
        }
    }

    public void deletar() {
        Boolean result = produtoModel.deletar();

        if (result) {
            view.mensagemAoUsuario("Produto Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Produto");
        }
    }

    public void carregaProduto(String id) {
        produtoModel.load(id);
    }

    public void carregaFornecedor(Fornecedor fornecedor) {
        produtoModel.setFornecedor(fornecedor);
    }

    public void carregaMarca(Marca marca) {
        produtoModel.setMarca(marca);
    }

    public void setFornecedorNull() {
        produtoModel.setFornecedor(null);
    }

    public void setMarcaNull() {
        produtoModel.setMarca(null);
    }

    public Produto getProduto() {
        return produtoModel;
    }

    public boolean setPreco(String preco) {
        try {
            produtoModel.setPreco(Double.valueOf(preco));
            return true;
        } catch (NumberFormatException ne) {
            produtoModel.setPreco(0.0);
            return false;
        }
    }
}
