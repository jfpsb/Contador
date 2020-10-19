package com.vandamodaintima.jfpsb.contador.controller.produto;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.IController;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.CadastrarView;
import com.vandamodaintima.jfpsb.contador.view.produto.CadastrarProduto;

public class CadastrarProdutoController implements IController {

    private CadastrarProduto view;
    private Produto produtoModel;
    ConexaoBanco conexaoBanco;

    public CadastrarProdutoController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarProduto) view;
        this.conexaoBanco = conexaoBanco;
        produtoModel = new Produto(conexaoBanco);
    }

    public void cadastrar() {
        Double p = 0.0;

        if (produtoModel.getCodBarra().trim().isEmpty()) {
            view.mensagemAoUsuario("Código de Barra Não Pode Estar Vazio");
            return;
        }

        if (produtoModel.getDescricao().trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição Não Pode Estar Vazio");
            return;
        }

        if (produtoModel.getPreco() <= 0.0) {
            view.mensagemAoUsuario("Preço Não Pode Ser Zero ou Menor que Zero");
            return;
        }

        Boolean result = produtoModel.salvar();

        if (result) {
            view.mensagemAoUsuario("Produto Cadastro Com Sucesso");
            view.aposCadastro(produtoModel.getCodBarra());
        } else {
            view.mensagemAoUsuario("Produto Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            Produto produto = produtoModel.listarPorId(codigo);

            if (produto != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
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

    public boolean isDouble(String d) {
        try {
            Double.valueOf(d);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    @Override
    public void reset() {
        produtoModel = new Produto(conexaoBanco);
    }
}
