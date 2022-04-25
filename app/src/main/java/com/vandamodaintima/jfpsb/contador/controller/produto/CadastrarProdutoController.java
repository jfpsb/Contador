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
    private Produto model;
    ConexaoBanco conexaoBanco;

    public CadastrarProdutoController(CadastrarView view, ConexaoBanco conexaoBanco) {
        this.view = (CadastrarProduto) view;
        this.conexaoBanco = conexaoBanco;
        model = new Produto(conexaoBanco);
    }

    public void salvar() {
        if (model.getCodBarra().trim().isEmpty()) {
            view.mensagemAoUsuario("Código de produto não pode estar vazio");
            return;
        }

        if(model.getProdutoGrades().size() == 0) {
            view.mensagemAoUsuario("É necessário criar ao menos uma grade para cadastrar o produto");
            return;
        }

        if (model.getDescricao().trim().isEmpty()) {
            view.mensagemAoUsuario("Descrição de produto não pode estar vazia");
            return;
        }

        Boolean result = model.salvar();

        if (result) {
            view.mensagemAoUsuario("Produto Cadastrado Com Sucesso");
            view.aposCadastro(model.getCodBarra());
        } else {
            view.mensagemAoUsuario("Produto Não Foi Cadastrado");
        }
    }

    public void checaCodigoBarra(String codigo) {
        if (!codigo.isEmpty()) {
            Produto produto = model.listarPorId(codigo);

            if (produto != null) {
                view.bloqueiaCampos();
            } else {
                view.liberaCampos();
            }
        }
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
        model = new Produto(conexaoBanco);
    }
}
