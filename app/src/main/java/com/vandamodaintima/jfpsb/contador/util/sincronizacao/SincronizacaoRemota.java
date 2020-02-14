package com.vandamodaintima.jfpsb.contador.util.sincronizacao;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.IDAO;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQL;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLContagem;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLContagemProduto;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLFornecedor;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLLoja;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLMarca;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLProduto;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQLTipoContagem;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class SincronizacaoRemota extends AsyncTask<Date, String, Void> {
    private WeakReference<Context> context;
    private ConexaoBanco conexaoBanco;

    public SincronizacaoRemota(Context context) {
        this.context = new WeakReference<>(context);
        this.conexaoBanco = new ConexaoBanco(context);
    }

    @Override
    protected Void doInBackground(Date... dates) {
        try {
            Date inicioSincronizacao = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");

            Log.i(ActivityBaseView.LOG, "Iniciando Sincronização às " + dateFormat.format(inicioSincronizacao));

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 2010);
            calendar.set(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            Date lastUpdate = calendar.getTime();

            File arquivoLastUpdate = new File(context.get().getApplicationInfo().dataDir, "LastUpdate.txt");

            if (arquivoLastUpdate.exists()) {
                Scanner scanner = new Scanner(arquivoLastUpdate);
                scanner.useDelimiter("\\Z");
                lastUpdate = dateFormat.parse(scanner.next());
            }

            Log.i(ActivityBaseView.LOG, dateFormat.format(new Date()) + ": Lendo Arquivos Remotos Com As Mudanças No Banco de Dados");

            ArrayList<EntidadeMySQL> fornecedoresRemotos = new ArquivoEntidade<>(context.get(), Fornecedor.class).lerXmlRemoto(lastUpdate);
            ArrayList<EntidadeMySQL> lojasRemotos = new ArquivoEntidade<>(context.get(), Loja.class).lerXmlRemoto(lastUpdate);
            ArrayList<EntidadeMySQL> marcasRemotas = new ArquivoEntidade<>(context.get(), Marca.class).lerXmlRemoto(lastUpdate);
            ArrayList<EntidadeMySQL> produtosRemotos = new ArquivoEntidade<>(context.get(), Produto.class).lerXmlRemoto(lastUpdate);
            ArrayList<EntidadeMySQL> tipoContagensRemotas = new ArquivoEntidade<>(context.get(), TipoContagem.class).lerXmlRemoto(lastUpdate);
            ArrayList<EntidadeMySQL> contagensRemotas = new ArquivoEntidade<>(context.get(), Contagem.class).lerXmlRemoto(lastUpdate);
            ArrayList<EntidadeMySQL> contagemProdutoRemotas = new ArquivoEntidade<>(context.get(), ContagemProduto.class).lerXmlRemoto(lastUpdate);

            Log.i(ActivityBaseView.LOG, dateFormat.format(new Date()) + ": Atualizando Banco de Dados Local Com Arquivos Remotos");

            DAOFornecedor daoFornecedor = new DAOFornecedor(conexaoBanco);
            DAOLoja daoLoja = new DAOLoja(conexaoBanco);
            DAOMarca daoMarca = new DAOMarca(conexaoBanco);
            DAOProduto daoProduto = new DAOProduto(conexaoBanco);
            DAOTipoContagem daoTipoContagem = new DAOTipoContagem(conexaoBanco);
            DAOContagem daoContagem = new DAOContagem(conexaoBanco);
            DAOContagemProduto daoContagemProduto = new DAOContagemProduto(conexaoBanco);

            sincronizaFornecedor(daoFornecedor, fornecedoresRemotos);
            sincronizaLoja(daoLoja, lojasRemotos);
            sincronizaMarca(daoMarca, marcasRemotas);
            sincronizaProduto(daoProduto, produtosRemotos);
            sincronizaTipoContagem(daoTipoContagem, tipoContagensRemotas);
            sincronizaContagem(daoContagem, contagensRemotas);
            sincronizaContagemProduto(daoContagemProduto, contagemProdutoRemotas);

            Log.i(ActivityBaseView.LOG, dateFormat.format(new Date()) + ": Atualizando Banco de Dados Remoto Com Arquivos Locais");

            ArrayList<EntidadeMySQL> fornecedoresLocais = new ArquivoEntidade<>(context.get(), Fornecedor.class).lerXmlLocal(lastUpdate);
            ArrayList<EntidadeMySQL> lojasLocais = new ArquivoEntidade<>(context.get(), Loja.class).lerXmlLocal(lastUpdate);
            ArrayList<EntidadeMySQL> marcasLocais = new ArquivoEntidade<>(context.get(), Marca.class).lerXmlLocal(lastUpdate);
            ArrayList<EntidadeMySQL> produtosLocais = new ArquivoEntidade<>(context.get(), Produto.class).lerXmlLocal(lastUpdate);
            ArrayList<EntidadeMySQL> tipoContagensLocais = new ArquivoEntidade<>(context.get(), TipoContagem.class).lerXmlLocal(lastUpdate);
            ArrayList<EntidadeMySQL> contagensLocais = new ArquivoEntidade<>(context.get(), Contagem.class).lerXmlLocal(lastUpdate);
            ArrayList<EntidadeMySQL> contagemProdutoLocais = new ArquivoEntidade<>(context.get(), ContagemProduto.class).lerXmlLocal(lastUpdate);

            //TODO: BD remoto
        } catch (Exception e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        }
        return null;
    }

    private void sincronizaContagem(IDAO<Contagem> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<Contagem> listaInsertUpdate = new ArrayList<>();
            ArrayList<Contagem> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLContagem entidadeMySQLContagem = (EntidadeMySQLContagem) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLContagem.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLContagem.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }

    private void sincronizaContagemProduto(IDAO<ContagemProduto> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<ContagemProduto> listaInsertUpdate = new ArrayList<>();
            ArrayList<ContagemProduto> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLContagemProduto entidadeMySQLContagemProduto = (EntidadeMySQLContagemProduto) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLContagemProduto.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLContagemProduto.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }

    private void sincronizaFornecedor(IDAO<Fornecedor> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<Fornecedor> listaInsertUpdate = new ArrayList<>();
            ArrayList<Fornecedor> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLFornecedor entidadeMySQLFornecedor = (EntidadeMySQLFornecedor) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLFornecedor.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLFornecedor.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }

    private void sincronizaLoja(IDAO<Loja> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<Loja> listaInsertUpdate = new ArrayList<>();
            ArrayList<Loja> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLLoja entidadeMySQLLoja = (EntidadeMySQLLoja) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLLoja.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLLoja.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }

    private void sincronizaMarca(IDAO<Marca> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<Marca> listaInsertUpdate = new ArrayList<>();
            ArrayList<Marca> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLMarca entidadeMySQLMarca = (EntidadeMySQLMarca) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLMarca.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLMarca.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }

    private void sincronizaProduto(IDAO<Produto> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<Produto> listaInsertUpdate = new ArrayList<>();
            ArrayList<Produto> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLProduto entidadeMySQLProduto = (EntidadeMySQLProduto) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLProduto.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLProduto.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }

    private void sincronizaTipoContagem(IDAO<TipoContagem> dao, ArrayList<EntidadeMySQL> entidadeMySQLS) {
        if (entidadeMySQLS.size() > 0) {
            ArrayList<TipoContagem> listaInsertUpdate = new ArrayList<>();
            ArrayList<TipoContagem> listaDelete = new ArrayList<>();

            for (EntidadeMySQL entidadeMySQL : entidadeMySQLS) {
                EntidadeMySQLTipoContagem entidadeMySQLTipoContagem = (EntidadeMySQLTipoContagem) entidadeMySQL;
                if (entidadeMySQL.getOperacaoMySql().equals("DELETE")) {
                    listaDelete.add(entidadeMySQLTipoContagem.getEntidadeSalva());
                } else {
                    listaInsertUpdate.add(entidadeMySQLTipoContagem.getEntidadeSalva());
                }
            }

            if (listaInsertUpdate.size() > 0)
                dao.inserirOuAtualizar(listaInsertUpdate);

            if (listaDelete.size() > 0)
                dao.inserirOuAtualizar(listaDelete);
        }
    }
}
