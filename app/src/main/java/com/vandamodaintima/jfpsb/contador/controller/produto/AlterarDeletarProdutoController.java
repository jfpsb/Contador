package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.FornecedorModel;
import com.vandamodaintima.jfpsb.contador.model.MarcaModel;
import com.vandamodaintima.jfpsb.contador.model.ProdutoModel;
import com.vandamodaintima.jfpsb.contador.view.interfaces.AlterarDeletarView;

import java.util.ArrayList;

public class AlterarDeletarProdutoController {
    private AlterarDeletarView view;
    private ConexaoBanco conexaoBanco;
    private ProdutoModel produtoModel;
    private FornecedorModel fornecedorModel;
    private MarcaModel marcaModel;

    public AlterarDeletarProdutoController(AlterarDeletarView view, ConexaoBanco conexaoBanco) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new ProdutoModel(conexaoBanco);
        fornecedorModel = new FornecedorModel(conexaoBanco);
        marcaModel = new MarcaModel(conexaoBanco);
    }

    public void atualizar(String descricao, double preco) {
        if (descricao.trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição do Produto Não Pode Ser Vazia");
            return;
        }

        if (preco == 0) {
            view.mensagemAoUsuario("Preço do Produto Não Pode Ser Zero");
            return;
        }

        if(fornecedorModel.getCnpj() == null) {
            produtoModel.setFornecedor(null);
        }
        else {
            produtoModel.setFornecedor(fornecedorModel);
        }

        if( marcaModel.getNome() == null) {
            produtoModel.setMarca(null);
        }
        else {
            produtoModel.setMarca(marcaModel);
        }

        produtoModel.setDescricao(descricao.trim().toUpperCase());
        produtoModel.setPreco(preco);

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

        if(produtoModel.getFornecedor() != null)
            fornecedorModel = produtoModel.getFornecedor();

        if(produtoModel.getMarca() != null)
            marcaModel = produtoModel.getMarca();
    }

    public void carregaFornecedor(String id) {
        fornecedorModel.load(id);
    }

    public void carregaMarca(String id) {
        marcaModel.load(id);
    }

    public String getCodBarra() {
        return produtoModel.getCod_barra();
    }

    public ArrayList<String> getCodBarraFornecedor() {
        return produtoModel.getCod_barra_fornecedor();
    }

    public String getDescricao() {
        return produtoModel.getDescricao();
    }

    public Double getPreco() {
        return produtoModel.getPreco();
    }

    public FornecedorModel getFornecedor() {
        return fornecedorModel;
    }

    public MarcaModel getMarca() {
        return marcaModel;
    }

    public void setCodBarraFornecedor(ArrayList<String> codigos) {
        produtoModel.setCod_barra_fornecedor(codigos);
    }

    public void setFornecedorNull() {
        fornecedorModel = new FornecedorModel(conexaoBanco);
    }

    public void setMarcaNull() {
        marcaModel = new MarcaModel(conexaoBanco);
    }
}
