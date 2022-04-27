package com.vandamodaintima.jfpsb.contador.controller.contagem;

import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.controller.contagem.contagemproduto.ContagemProdutoArrayAdapter;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ICadastrarView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IProcessaCodigoBarraLido;

import java.util.ArrayList;
import java.util.List;

public class InserirContagemProdutoController {
    private ICadastrarView view;
    private ProdutoGrade produtoGrade;
    private Contagem contagem;
    private ContagemProduto model;
    private ConexaoBanco conexaoBanco;
    private ContagemProdutoArrayAdapter contagemProdutoArrayAdapter;
    private boolean isCampoQuantHabilitado;

    public InserirContagemProdutoController(ICadastrarView view, ConexaoBanco conexaoBanco, String contagemId) {
        this.view = view;
        this.conexaoBanco = conexaoBanco;
        contagem = new Contagem(conexaoBanco);
        model = new ContagemProduto(conexaoBanco);
        produtoGrade = new ProdutoGrade(conexaoBanco);

        contagem.load(contagemId);
        model.setContagem(contagem);
        contagemProdutoArrayAdapter = new ContagemProdutoArrayAdapter(view.getContext(), R.layout.item_produto_contagem_com_grade, model.listarPorContagem(contagem));
        view.setListViewAdapter(contagemProdutoArrayAdapter);
    }

    public void cadastrar(int quantidade) {
        model.setQuant(quantidade);
        Boolean result = model.salvar();
        if (result) {
            contagemProdutoArrayAdapter.add(model);
            contagemProdutoArrayAdapter.notifyDataSetChanged();
            model = new ContagemProduto(conexaoBanco);
            model.setContagem(contagem);
            view.mensagemAoUsuario("Contagem de produto adicionada com sucesso");
        } else {
            view.mensagemAoUsuario("Erro ao adicionar contagem de produto");
        }
    }

    public void cadastrar() {
        model.setQuant(1);
        Boolean result = model.salvar();
        if (result) {
            contagemProdutoArrayAdapter.add(model);
            contagemProdutoArrayAdapter.notifyDataSetChanged();
            model = new ContagemProduto(conexaoBanco);
            model.setContagem(contagem);
            view.mensagemAoUsuario("Contagem de produto adicionada com sucesso");
        } else {
            view.mensagemAoUsuario("Erro ao adicionar contagem de produto");
        }
    }

    public List<ProdutoGrade> pesquisarProdutoGrade(String cod_barra) {
        List<ProdutoGrade> produtoGrades = new ArrayList<>();

        if (!cod_barra.trim().isEmpty()) {
            produtoGrades = produtoGrade.listarPorCodBarra(cod_barra);
        }

        return produtoGrades;
    }

    public void processaCodigoLido(String codigo_lido) {
        List<ProdutoGrade> produtoGrades = new ArrayList<>();

        if (!codigo_lido.trim().isEmpty()) {
            produtoGrades = produtoGrade.listarPorCodBarra(codigo_lido);
        }

        if(produtoGrades.size() == 0) {
            ((IProcessaCodigoBarraLido)view).showAlertaProdutoGradeNaoEncontrado();
        }
        else if (produtoGrades.size() == 1) {
            model.setProdutoGrade(produtoGrades.get(0));
            executaCadastro();
        } else {
            view.mensagemAoUsuario("Mais De Uma Grade Foi Encontrada. Selecione Na Lista");
            ((IProcessaCodigoBarraLido)view).abrirVisualizarProdutoGradeContagem(codigo_lido);
        }
    }

    public void executaCadastro() {
        if(isCampoQuantHabilitado) {
            ((IProcessaCodigoBarraLido)view).showAlertaQuantidadeProduto();
        }
        else {
            cadastrar();
        }
    }

    public void pesquisar() {
        contagemProdutoArrayAdapter.clear();
        contagemProdutoArrayAdapter.addAll(model.listarPorContagem(contagem));
        contagemProdutoArrayAdapter.notifyDataSetChanged();
    }

    public void carregaProdutoGrade(ProdutoGrade produtoGrade) {
        model.setProdutoGrade(produtoGrade);
    }

    public Contagem getContagem() {
        return model.getContagem();
    }

    public void setCampoQuantHabilitado(boolean campoQuantHabilitado) {
        isCampoQuantHabilitado = campoQuantHabilitado;
    }

    /**
     * Atualiza listagem de contagens de produto na view InserirContagemProduto
     */
    public void atualizarProdutoGrades() {
        contagemProdutoArrayAdapter.clear();
        contagemProdutoArrayAdapter.addAll(model.listarPorContagem(contagem));
        contagemProdutoArrayAdapter.notifyDataSetChanged();
    }
}
