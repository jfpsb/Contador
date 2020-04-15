package com.vandamodaintima.jfpsb.contador.sincronizacao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vandamodaintima.jfpsb.contador.banco.ConexaoBanco;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.ContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.Fornecedor;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.model.Loja;
import com.vandamodaintima.jfpsb.contador.model.Marca;
import com.vandamodaintima.jfpsb.contador.model.OperadoraCartao;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.model.TipoContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.ADAO;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOOperadoraCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAORecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class Sincronizacao extends Thread {
    private static Socket clientSocket;
    private ConexaoBanco conexaoBanco;
    private static Gson gson = null;
    private static ZonedDateTime lastSync;
    private static File lastSyncFile = null;
    private static File logDir = null;
    private volatile boolean exit = false;

    private static final List<DBLog<Contagem>> TRANSIENTWRITECONTAGEM = new ArrayList<>();
    private static final List<DBLog<ContagemProduto>> TRANSIENTWRITECONTAGEMPRODUTO = new ArrayList<>();
    private static final List<DBLog<Fornecedor>> TRANSIENTWRITEFORNECEDOR = new ArrayList<>();
    private static final List<DBLog<Loja>> TRANSIENTWRITELOJA = new ArrayList<>();
    private static final List<DBLog<Marca>> TRANSIENTWRITEMARCA = new ArrayList<>();
    private static final List<DBLog<OperadoraCartao>> TRANSIENTWRITEOPERADORACARTAO = new ArrayList<>();
    private static final List<DBLog<Produto>> TRANSIENTWRITEPRODUTO = new ArrayList<>();
    private static final List<DBLog<RecebimentoCartao>> TRANSIENTWRITERECEBIMENTOCARTAO = new ArrayList<>();
    private static final List<DBLog<TipoContagem>> TRANSIENTWRITETIPOCONTAGEM = new ArrayList<>();

    private static final List<DBLog<Contagem>> TRANSIENTSENDCONTAGEM = new ArrayList<>();
    private static final List<DBLog<ContagemProduto>> TRANSIENTSENDCONTAGEMPRODUTO = new ArrayList<>();
    private static final List<DBLog<Fornecedor>> TRANSIENTSENDFORNECEDOR = new ArrayList<>();
    private static final List<DBLog<Loja>> TRANSIENTSENDLOJA = new ArrayList<>();
    private static final List<DBLog<Marca>> TRANSIENTSENDMARCA = new ArrayList<>();
    private static final List<DBLog<OperadoraCartao>> TRANSIENTSENDOPERADORACARTAO = new ArrayList<>();
    private static final List<DBLog<Produto>> TRANSIENTSENDPRODUTO = new ArrayList<>();
    private static final List<DBLog<RecebimentoCartao>> TRANSIENTSENDRECEBIMENTOCARTAO = new ArrayList<>();
    private static final List<DBLog<TipoContagem>> TRANSIENTSENDTIPOCONTAGEM = new ArrayList<>();

    private static List<DBLog<Contagem>> logsContagem = new ArrayList<>();
    private static List<DBLog<ContagemProduto>> logsContagemProduto = new ArrayList<>();
    private static List<DBLog<Fornecedor>> logsFornecedor = new ArrayList<>();
    private static List<DBLog<Loja>> logsLoja = new ArrayList<>();
    private static List<DBLog<Marca>> logsMarca = new ArrayList<>();
    private static List<DBLog<OperadoraCartao>> logsOperadoraCartao = new ArrayList<>();
    private static List<DBLog<Produto>> logsProduto = new ArrayList<>();
    private static List<DBLog<RecebimentoCartao>> logsRecebimentoCartao = new ArrayList<>();
    private static List<DBLog<TipoContagem>> logsTipoContagem = new ArrayList<>();

    public Sincronizacao(Context context, ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        logDir = context.getDir("Log", Context.MODE_PRIVATE);
        lastSyncFile = new File(logDir, "LastSync.txt");
        lastSync = getLastSync();
        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeGsonAdapter())
                .create();
    }

    @Override
    public void run() {
        if (new File(logDir, "Contagem.json").exists()) {
            String json = readAllText(new File(logDir, "Contagem.json"));
            if (json != null && !json.equals(""))
                logsContagem = gson.fromJson(json, new TypeToken<ArrayList<DBLog<Contagem>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Contagens Locais Recuperadas: ");
        }

        if (new File(logDir, "ContagemProduto.json").exists()) {
            String json = readAllText(new File(logDir, "ContagemProduto.json"));
            if (json != null && !json.equals(""))
                logsContagemProduto = gson.fromJson(json, new TypeToken<ArrayList<DBLog<ContagemProduto>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "ContagemProdutos Locais Recuperadas");
        }

        if (new File(logDir, "Fornecedor.json").exists()) {
            String json = readAllText(new File(logDir, "Fornecedor.json"));
            if (json != null && !json.equals(""))
                logsFornecedor = gson.fromJson(json, new TypeToken<ArrayList<DBLog<Fornecedor>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Fornecedores Locais Recuperados");
        }

        if (new File(logDir, "Loja.json").exists()) {
            String json = readAllText(new File(logDir, "Loja.json"));
            if (json != null && !json.equals(""))
                logsLoja = gson.fromJson(json, new TypeToken<ArrayList<DBLog<Loja>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Lojas Locais Recuperadas");
        }

        if (new File(logDir, "Marca.json").exists()) {
            String json = readAllText(new File(logDir, "Marca.json"));
            if (json != null && !json.equals(""))
                logsMarca = gson.fromJson(json, new TypeToken<ArrayList<DBLog<Marca>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Marcas Locais Recuperadas");
        }

        if (new File(logDir, "OperadoraCartao.json").exists()) {
            String json = readAllText(new File(logDir, "OperadoraCartao.json"));
            if (json != null && !json.equals(""))
                logsOperadoraCartao = gson.fromJson(json, new TypeToken<ArrayList<DBLog<OperadoraCartao>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Operadoras Locais Recuperadas");
        }

        if (new File(logDir, "Produto.json").exists()) {
            String json = readAllText(new File(logDir, "Produto.json"));
            if (json != null && !json.equals(""))
                logsProduto = gson.fromJson(json, new TypeToken<ArrayList<DBLog<Produto>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Produtos Locais Recuperados");
        }

        if (new File(logDir, "RecebimentoCartao.json").exists()) {
            String json = readAllText(new File(logDir, "RecebimentoCartao.json"));
            if (json != null && !json.equals(""))
                logsRecebimentoCartao = gson.fromJson(json, new TypeToken<ArrayList<DBLog<RecebimentoCartao>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Recebimentos Locais Recuperados");
        }

        if (new File(logDir, "TipoContagem.json").exists()) {
            String json = readAllText(new File(logDir, "TipoContagem.json"));
            if (json != null && !json.equals(""))
                logsTipoContagem = gson.fromJson(json, new TypeToken<ArrayList<DBLog<TipoContagem>>>() {
                }.getType());
            Log.i(ActivityBaseView.LOG, "Tipos Locais Recuperados");
        }

        conectar();
    }

    private void conectar() {
        try {
            Log.i(ActivityBaseView.LOG, "Tentando Conexão Com Servidor");
            //18.229.130.78
            clientSocket = new Socket("18.229.130.78", 3999);
            Log.i(ActivityBaseView.LOG, "Conexão Efetuada Com Sucesso");

            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            if (clientSocket.isConnected()) {
                appOpening();
            }

            while (clientSocket.isConnected() && !exit) {
                String receivedFromServer = bufferedReader.readLine();

                // Socket foi desconectado pelo lado do servidor
                if (receivedFromServer == null)
                    throw new SocketException("Conexão com Servidor Encerrada");

                String[] receivedSplitted = receivedFromServer.split("\\|");
                String receivedId = receivedSplitted[0];

                switch (receivedId) {
                    case "LogResponse":
                        //Quando o servidor termina de enviar seus logs
                        //Aguarda 10 segundos para mandar os logs locais para o servidor
                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                sendLog();
                                t.cancel();
                            }
                        }, 10000);
                        break;
                    case "Log":
                        String tipoEntidade = receivedSplitted[1];
                        String json = receivedSplitted[2];
                        ADAO dao;

                        switch (tipoEntidade) {
                            case "Contagem":
                                dao = new DAOContagem(conexaoBanco);
                                List<DBLog<Contagem>> contagensRecebidas = gson.fromJson(json, new TypeToken<List<DBLog<Contagem>>>() {
                                }.getType());

                                for (DBLog<Contagem> log : contagensRecebidas) {
                                    if (log.getOperacao().equals("INSERT")) {
                                        Contagem contagemLocal = (Contagem) dao.listarPorId(log.getEntidade().getIdentifier());

                                        dao.replicate(log.getEntidade());

                                        if (contagemLocal != null) {
                                            contagemLocal.setData(contagemLocal.getData().plusSeconds(1));
                                            Object c = dao.listarPorId(contagemLocal.getIdentifier());
                                            while (c != null) {
                                                contagemLocal = (Contagem) c;
                                                contagemLocal.setData(contagemLocal.getData().plusSeconds(1));
                                                c = dao.listarPorId(contagemLocal.getIdentifier());
                                            }

                                            dao.inserir(contagemLocal, true);
                                        }
                                    } else if (log.getOperacao().equals("UPDATE")) {
                                        dao.atualizar(log.getEntidade(), false);
                                    } else {
                                        dao.deletar(log.getEntidade(), false);
                                    }
                                }
                                break;
                            case "ContagemProduto":
                                dao = new DAOContagemProduto(conexaoBanco);
                                List<DBLog<ContagemProduto>> contagensProdutoRecebidas = gson.fromJson(json, new TypeToken<List<DBLog<ContagemProduto>>>() {
                                }.getType());

                                for (DBLog<ContagemProduto> log : contagensProdutoRecebidas) {
                                    if (log.getOperacao().equals("INSERT")) {
                                        ContagemProduto contagemProdutoLocal = (ContagemProduto) dao.listarPorId(log.getEntidade().getIdentifier());

                                        dao.replicate(log.getEntidade());

                                        if (contagemProdutoLocal != null) {
                                            contagemProdutoLocal.setId(Instant.now().getEpochSecond());
                                            Object c = dao.listarPorId(contagemProdutoLocal.getIdentifier());
                                            while (c != null) {
                                                contagemProdutoLocal = (ContagemProduto) c;
                                                contagemProdutoLocal.setId(Instant.now().getEpochSecond());
                                                c = dao.listarPorId(contagemProdutoLocal.getIdentifier());
                                            }

                                            dao.inserir(contagemProdutoLocal, true);
                                        }
                                    } else if (log.getOperacao().equals("UPDATE")) {
                                        dao.atualizar(log.getEntidade(), false);
                                    } else {
                                        dao.deletar(log.getEntidade(), false);
                                    }
                                }
                                break;
                            case "Fornecedor":
                                dao = new DAOFornecedor(conexaoBanco);
                                List<DBLog<Fornecedor>> fornecedoresRecebidos = gson.fromJson(json, new TypeToken<List<DBLog<Fornecedor>>>() {
                                }.getType());
                                forEachLog(fornecedoresRecebidos, dao);
                                break;
                            case "Loja":
                                dao = new DAOLoja(conexaoBanco);
                                List<DBLog<Loja>> lojasRecebidas = gson.fromJson(json, new TypeToken<List<DBLog<Loja>>>() {
                                }.getType());
                                forEachLog(lojasRecebidas, dao);
                                break;
                            case "Marca":
                                dao = new DAOMarca(conexaoBanco);
                                List<DBLog<Loja>> marcasRecebidas = gson.fromJson(json, new TypeToken<List<DBLog<Marca>>>() {
                                }.getType());
                                forEachLog(marcasRecebidas, dao);
                                break;
                            case "OperadoraCartao":
                                dao = new DAOOperadoraCartao(conexaoBanco);
                                List<DBLog<OperadoraCartao>> operadorasRecebidas = gson.fromJson(json, new TypeToken<List<DBLog<OperadoraCartao>>>() {
                                }.getType());
                                forEachLog(operadorasRecebidas, dao);
                                break;
                            case "Produto":
                                dao = new DAOProduto(conexaoBanco);
                                List<DBLog<Produto>> produtosRecebidos = gson.fromJson(json, new TypeToken<List<DBLog<Produto>>>() {
                                }.getType());

                                for (DBLog<Produto> log : produtosRecebidos) {
                                    if (log.getOperacao().equals("INSERT")) {
                                        Produto produtoLocal = (Produto) dao.listarPorId(log.getEntidade().getIdentifier());

                                        dao.replicate(log.getEntidade());

                                        if (produtoLocal != null) {
                                            int maxId = dao.getMaxId();
                                            produtoLocal.setCod_barra(String.valueOf(++maxId));
                                            Object c = dao.listarPorId(produtoLocal.getIdentifier());
                                            while (c != null) {
                                                produtoLocal = (Produto) c;
                                                produtoLocal.setCod_barra(String.valueOf(++maxId));
                                                c = dao.listarPorId(produtoLocal.getIdentifier());
                                            }

                                            dao.inserir(produtoLocal, true);
                                        }
                                    } else if (log.getOperacao().equals("UPDATE")) {
                                        dao.atualizar(log.getEntidade(), false);
                                    } else {
                                        dao.deletar(log.getEntidade(), false);
                                    }
                                }
                                break;
                            case "RecebimentoCartao":
                                dao = new DAORecebimentoCartao(conexaoBanco);
                                List<DBLog<RecebimentoCartao>> recebimentosRecebidos = gson.fromJson(json, new TypeToken<List<DBLog<RecebimentoCartao>>>() {
                                }.getType());
                                forEachLog(recebimentosRecebidos, dao);
                                break;
                            case "TipoContagem":
                                dao = new DAOTipoContagem(conexaoBanco);
                                List<DBLog<TipoContagem>> tiposRecebidos = gson.fromJson(json, new TypeToken<List<DBLog<TipoContagem>>>() {
                                }.getType());

                                for (DBLog<TipoContagem> log : tiposRecebidos) {
                                    if (log.getOperacao().equals("INSERT")) {
                                        TipoContagem tipoLocal = (TipoContagem) dao.listarPorId(log.getEntidade().getIdentifier());

                                        dao.replicate(log.getEntidade());

                                        if (tipoLocal != null) {
                                            int maxId = dao.getMaxId();
                                            tipoLocal.setId(++maxId);
                                            Object c = dao.listarPorId(tipoLocal.getIdentifier());
                                            while (c != null) {
                                                tipoLocal = (TipoContagem) c;
                                                tipoLocal.setId(++maxId);
                                                c = dao.listarPorId(tipoLocal.getIdentifier());
                                            }

                                            dao.inserir(tipoLocal, true);
                                        }
                                    } else if (log.getOperacao().equals("UPDATE")) {
                                        dao.atualizar(log.getEntidade(), false);
                                    } else {
                                        dao.deletar(log.getEntidade(), false);
                                    }
                                }
                                break;
                        }
                        break;
                }
            }
        } catch (IOException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
            Log.i(ActivityBaseView.LOG, "Não É Possível Conectar Ao Servidor. Tentando Novamente Em 30 Segundos");
            try {
                Thread.sleep(30000);
                conectar();
            } catch (InterruptedException ex) {
                Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
                conectar();
            }
        }
    }

    private <T extends IModel & Serializable> void forEachLog(List<DBLog<T>> logs, ADAO dao) {
        for (DBLog<T> log : logs) {
            if (log.getOperacao().equals("INSERT")) {
                dao.replicate(log.getEntidade());
            } else if (log.getOperacao().equals("UPDATE")) {
                dao.atualizar(log.getEntidade(), false);
            } else {
                dao.deletar(log.getEntidade(), false);
            }
        }
    }

    /**
     * Executado quando a aplicação é iniciada. Compara os logs locais com os logs remotos e atualiza de acordo
     */
    private void appOpening() throws IOException {
        try {
            Log.i(ActivityBaseView.LOG, "Sincronização Iniciada");
            Log.i(ActivityBaseView.LOG, "Solicitando Logs do Servidor");

            TRANSIENTSENDCONTAGEM.addAll(logsContagem.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDCONTAGEMPRODUTO.addAll(logsContagemProduto.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDFORNECEDOR.addAll(logsFornecedor.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDLOJA.addAll(logsLoja.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDMARCA.addAll(logsMarca.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDOPERADORACARTAO.addAll(logsOperadoraCartao.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDPRODUTO.addAll(logsProduto.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDRECEBIMENTOCARTAO.addAll(logsRecebimentoCartao.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));
            TRANSIENTSENDTIPOCONTAGEM.addAll(logsTipoContagem.stream().filter(f -> f.getModificadoEm().isAfter(lastSync)).collect(Collectors.toList()));

            String logRequest = "LogRequest|" + lastSync.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME) + "\n";
            Log.i(ActivityBaseView.LOG, "Enviado Request de Logs");

            clientSocket.getOutputStream().write(logRequest.getBytes());
        } catch (SocketException se) {
            Log.e(ActivityBaseView.LOG, se.getMessage(), se);
            Log.i(ActivityBaseView.LOG, "Não É Possível Conectar Ao Servidor. Tentando Novamente Em 30 Segundos");
            try {
                Thread.sleep(30000);
                conectar();
            } catch (InterruptedException ex) {
                Log.e(ActivityBaseView.LOG, ex.getMessage(), ex);
                conectar();
            }
        }
    }

    public static <E extends IModel & Serializable> void escreverJson(String nomeClasse, List<DBLog<E>> logs) {
        String json = gson.toJson(logs, new TypeToken<ArrayList<DBLog<E>>>() {
        }.getType());

        File logFile = new File(logDir, nomeClasse + ".json");

        try (FileWriter fileWriter = new FileWriter(logFile)) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdownSocket() {
        try {
            clientSocket.close();
            exit = true;
        } catch (IOException e) {
            android.util.Log.e(ActivityBaseView.LOG, "Erro ao Fechar Socket: " + e.getMessage(), e);
        }
    }

    private ZonedDateTime getLastSync() {
        //Se o arquivo não existe retorna uma data antiga
        if (!lastSyncFile.exists()) {
            LocalDateTime localDateTime = LocalDateTime.of(2018, 1, 1, 0, 0, 0);
            ZonedDateTime now = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
            //Salva data no arquivo
            writeAllText(lastSyncFile, now.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
            return now;
        }

        String dataString = readAllText(lastSyncFile);

        if (dataString != null)
            return ZonedDateTime.parse(dataString, DateTimeFormatter.ISO_OFFSET_DATE_TIME);

        return null;
    }

    public static void writeLog() {
        if (TRANSIENTWRITECONTAGEM.size() > 0) {
            logsContagem.addAll(TRANSIENTWRITECONTAGEM);
            String json = gson.toJson(logsContagem);
            writeAllText(new File(logDir, "Contagem.json"), json);
            TRANSIENTSENDCONTAGEM.addAll(TRANSIENTWRITECONTAGEM);
            TRANSIENTWRITECONTAGEM.clear();
        }

        if (TRANSIENTWRITECONTAGEMPRODUTO.size() > 0) {
            logsContagemProduto.addAll(TRANSIENTWRITECONTAGEMPRODUTO);
            String json = gson.toJson(logsContagemProduto);
            writeAllText(new File(logDir, "ContagemProduto.json"), json);
            TRANSIENTSENDCONTAGEMPRODUTO.addAll(TRANSIENTWRITECONTAGEMPRODUTO);
            TRANSIENTWRITECONTAGEMPRODUTO.clear();
        }

        if (TRANSIENTWRITEFORNECEDOR.size() > 0) {
            logsFornecedor.addAll(TRANSIENTWRITEFORNECEDOR);
            String json = gson.toJson(logsFornecedor);
            writeAllText(new File(logDir, "Fornecedor.json"), json);
            TRANSIENTSENDFORNECEDOR.addAll(TRANSIENTWRITEFORNECEDOR);
            TRANSIENTWRITEFORNECEDOR.clear();
        }

        if (TRANSIENTWRITELOJA.size() > 0) {
            logsLoja.addAll(TRANSIENTWRITELOJA);
            String json = gson.toJson(logsLoja, new TypeToken<ArrayList<DBLog<Loja>>>(){}.getType());
            writeAllText(new File(logDir, "Loja.json"), json);
            TRANSIENTSENDLOJA.addAll(TRANSIENTWRITELOJA);
            TRANSIENTWRITELOJA.clear();
        }

        if (TRANSIENTWRITEMARCA.size() > 0) {
            logsMarca.addAll(TRANSIENTWRITEMARCA);
            String json = gson.toJson(logsMarca);
            writeAllText(new File(logDir, "Marca.json"), json);
            TRANSIENTSENDMARCA.addAll(TRANSIENTWRITEMARCA);
            TRANSIENTWRITEMARCA.clear();
        }

        if (TRANSIENTWRITEOPERADORACARTAO.size() > 0) {
            logsOperadoraCartao.addAll(TRANSIENTWRITEOPERADORACARTAO);
            String json = gson.toJson(logsOperadoraCartao);
            writeAllText(new File(logDir, "OperadoraCartao.json"), json);
            TRANSIENTSENDOPERADORACARTAO.addAll(TRANSIENTWRITEOPERADORACARTAO);
            TRANSIENTWRITEOPERADORACARTAO.clear();
        }

        if (TRANSIENTWRITEPRODUTO.size() > 0) {
            logsProduto.addAll(TRANSIENTWRITEPRODUTO);
            String json = gson.toJson(logsProduto);
            writeAllText(new File(logDir, "Produto.json"), json);
            TRANSIENTSENDPRODUTO.addAll(TRANSIENTWRITEPRODUTO);
            TRANSIENTWRITEPRODUTO.clear();
        }

        if (TRANSIENTWRITERECEBIMENTOCARTAO.size() > 0) {
            logsRecebimentoCartao.addAll(TRANSIENTWRITERECEBIMENTOCARTAO);
            String json = gson.toJson(logsRecebimentoCartao);
            writeAllText(new File(logDir, "RecebimentoCartao.json"), json);
            TRANSIENTSENDRECEBIMENTOCARTAO.addAll(TRANSIENTWRITERECEBIMENTOCARTAO);
            TRANSIENTWRITERECEBIMENTOCARTAO.clear();
        }

        if (TRANSIENTWRITETIPOCONTAGEM.size() > 0) {
            logsTipoContagem.addAll(TRANSIENTWRITETIPOCONTAGEM);
            String json = gson.toJson(logsTipoContagem);
            writeAllText(new File(logDir, "TipoContagem.json"), json);
            TRANSIENTSENDTIPOCONTAGEM.addAll(TRANSIENTWRITETIPOCONTAGEM);
            TRANSIENTWRITETIPOCONTAGEM.clear();
        }
    }

    public static void sendLog() {
        Thread thread = new Thread(() -> {
            try {
                if (TRANSIENTSENDCONTAGEM.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDCONTAGEM);
                    String mensagemAoServidor = "Log|Contagem|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDCONTAGEM.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "CONTAGEM: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDCONTAGEMPRODUTO.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDCONTAGEMPRODUTO);
                    String mensagemAoServidor = "Log|ContagemProduto|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDCONTAGEMPRODUTO.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "CONTAGEMPRODUTO: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDFORNECEDOR.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDFORNECEDOR);
                    String mensagemAoServidor = "Log|Fornecedor|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDFORNECEDOR.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "FORNECEDOR: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDLOJA.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDLOJA);
                    String mensagemAoServidor = "Log|Loja|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDLOJA.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "LOJA: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDMARCA.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDMARCA);
                    String mensagemAoServidor = "Log|Marca|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDMARCA.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "MARCA: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDOPERADORACARTAO.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDOPERADORACARTAO);
                    String mensagemAoServidor = "Log|OperadoraCartao|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDOPERADORACARTAO.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "OPERADORACARTAO: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDPRODUTO.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDPRODUTO);
                    String mensagemAoServidor = "Log|Produto|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDPRODUTO.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "PRODUTO: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDRECEBIMENTOCARTAO.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDRECEBIMENTOCARTAO);
                    String mensagemAoServidor = "Log|RecebimentoCartao|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDRECEBIMENTOCARTAO.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "RECEBIMENTOCARTAO: " + ex.getMessage(), ex);
            }

            try {
                if (TRANSIENTSENDTIPOCONTAGEM.size() > 0) {
                    String json = gson.toJson(TRANSIENTSENDTIPOCONTAGEM);
                    String mensagemAoServidor = "Log|TipoContagem|" + json + "\n";
                    clientSocket.getOutputStream().write(mensagemAoServidor.getBytes());
                    TRANSIENTSENDTIPOCONTAGEM.clear();
                }
            } catch (Exception ex) {
                Log.e(ActivityBaseView.LOG, "TIPOCONTAGEM: " + ex.getMessage(), ex);
            }

            lastSync = Instant.now().atZone(ZoneId.systemDefault());
            writeAllText(lastSyncFile, lastSync.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        });

        thread.start();
    }

    public static void addTransientLog(Object entidade, String operacao) {
        String classe = entidade.getClass().getSimpleName();

        switch (classe) {
            case "Contagem":
                DBLog<Contagem> c = new DBLog<>();
                c.setOperacao(operacao);
                c.setEntidade((Contagem) entidade);
                c.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITECONTAGEM.add(c);
                break;
            case "ContagemProduto":
                DBLog<ContagemProduto> cp = new DBLog<>();
                cp.setOperacao(operacao);
                cp.setEntidade((ContagemProduto) entidade);
                cp.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITECONTAGEMPRODUTO.add(cp);
                break;
            case "Fornecedor":
                DBLog<Fornecedor> f = new DBLog<>();
                f.setOperacao(operacao);
                f.setEntidade((Fornecedor) entidade);
                f.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITEFORNECEDOR.add(f);
                break;
            case "Loja":
                DBLog<Loja> l = new DBLog<>();
                l.setOperacao(operacao);
                l.setEntidade((Loja) entidade);
                l.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITELOJA.add(l);
                break;
            case "Marca":
                DBLog<Marca> m = new DBLog<>();
                m.setOperacao(operacao);
                m.setEntidade((Marca) entidade);
                m.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITEMARCA.add(m);
                break;
            case "OperadoraCartao":
                DBLog<OperadoraCartao> oc = new DBLog<>();
                oc.setOperacao(operacao);
                oc.setEntidade((OperadoraCartao) entidade);
                oc.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITEOPERADORACARTAO.add(oc);
                break;
            case "Produto":
                DBLog<Produto> p = new DBLog<>();
                p.setOperacao(operacao);
                p.setEntidade((Produto) entidade);
                p.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITEPRODUTO.add(p);
                break;
            case "RecebimentoCartao":
                DBLog<RecebimentoCartao> rc = new DBLog<>();
                rc.setOperacao(operacao);
                rc.setEntidade((RecebimentoCartao) entidade);
                rc.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITERECEBIMENTOCARTAO.add(rc);
                break;
            case "TipoContagem":
                DBLog<TipoContagem> tc = new DBLog<>();
                tc.setOperacao(operacao);
                tc.setEntidade((TipoContagem) entidade);
                tc.setModificadoEm(Instant.now().atZone(ZoneId.systemDefault()));
                TRANSIENTWRITETIPOCONTAGEM.add(tc);
                break;
        }
    }

    private static void writeAllText(File file, String text) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(text);
            fileWriter.flush();
        } catch (IOException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        }
    }

    private String readAllText(File file) {
        try (Scanner scanner = new Scanner(file)) {
            scanner.useDelimiter("\\Z");
            return scanner.next();
        } catch (FileNotFoundException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
            return null;
        }
    }
}
