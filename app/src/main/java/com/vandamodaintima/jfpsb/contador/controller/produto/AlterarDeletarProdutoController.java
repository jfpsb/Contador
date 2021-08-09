package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

public class AlterarDeletarProdutoController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private Produto model;

    public AlterarDeletarProdutoController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        model = new Produto(conexaoBanco);
    }

    public void atualizar() {
        if (model.getDescricao().trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição do Produto Não Pode Ser Vazia");
            return;
        }

        if (model.getPreco() == 0) {
            view.mensagemAoUsuario("Preço do Produto Não Pode Ser Zero");
            return;
        }

        Boolean result = model.atualizar();

        if (result) {
            view.mensagemAoUsuario("Produto Atualizado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Atualizar Produto");
        }
    }

    public void deletar() {
        Boolean result = model.deletar();

        if (result) {
            view.mensagemAoUsuario("Produto Deletado Com Sucesso");
            view.fecharTela();
        } else {
            view.mensagemAoUsuario("Erro ao Deletar Produto");
        }
    }

    public void carregaProduto(long id) {
        model.load(id);
    }

    public void carregaFornecedor(Fornecedor fornecedor) {
        model.setFornecedor(fornecedor);
    }

    public void carregaMarca(Marca marca) {
        model.setMarca(marca);
    }

    public void setFornecedorNull() {
        model.setFornecedor(null);
    }

    public void setMarcaNull() {
        model.setMarca(null);
    }

    public Produto getProduto() {
        return model;
    }

    public boolean setPreco(String preco) {
        try {
            model.setPreco(Double.valueOf(preco));
            return true;
        } catch (NumberFormatException ne) {
            model.setPreco(0.0);
            return false;
        }
    }
}
