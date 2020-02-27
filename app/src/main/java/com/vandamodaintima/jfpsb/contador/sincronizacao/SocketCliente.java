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
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOContagemProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOFornecedor;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOLoja;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOMarca;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOOperadoraCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOProduto;
import com.vandamodaintima.jfpsb.contador.model.dao.DAORecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;
import com.vandamodaintima.jfpsb.contador.model.dao.ADAO;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.ZonedDateTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SocketCliente extends Thread {
    private Socket clientSocket;
    private List<DatabaseLogFileInfo> fileInfoLogsRecebidos = new ArrayList<>();
    private ConexaoBanco conexaoBanco;
    private static Gson gson = null;
    private static File dirDatabaseLog = null;
    private OperacoesJsonDatabaseLog operacoesJsonDatabaseLog;

    public SocketCliente(Context context, ConexaoBanco conexaoBanco) {
        this.conexaoBanco = conexaoBanco;
        dirDatabaseLog = context.getDir("DatabaseLog", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeGsonAdapter())
                .create();
        operacoesJsonDatabaseLog = new OperacoesJsonDatabaseLog(gson, dirDatabaseLog);
    }

    @Override
    public void run() {
        conectar();
    }

    private void conectar() {
        try {
            clientSocket = new Socket("192.168.0.15", 3999);

            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            if (clientSocket.isConnected()) {
                applicationOpening();
            }

            DatabaseLogFileObserver databaseLogFileObserver = new DatabaseLogFileObserver(gson, dirDatabaseLog, this);
            databaseLogFileObserver.startWatching();

            while (clientSocket.isConnected()) {
                try {
                    String receivedFromServer = bufferedReader.readLine();

                    // Socket foi desconectado pelo lado do servidor
                    if (receivedFromServer == null)
                        throw new SocketException("Conexão com Servidor Encerrada");

                    String[] receivedSplitted = receivedFromServer.split("\\|");
                    String receivedId = receivedSplitted[0];

                    switch (receivedId) {
                        case "DatabaseLogFileRequest":
                            // Recebe do servidor uma lista com os arquivos DatabaseLogFile que o servidor precisa e envia
                            String dataLogFileRequest = receivedSplitted[1];
                            List<DatabaseLogFileInfo> databaseLogFileInfos = operacoesJsonDatabaseLog.lerJsonDatabaseLogFileInfo(dataLogFileRequest);
                            List<String> fileNamesRequested = new ArrayList<>();
                            List<String> databaseLogFilesRequested = new ArrayList<>();

                            for (DatabaseLogFileInfo databaseLogFileInfo : databaseLogFileInfos) {
                                // Guarda nomes dos arquivos de log
                                fileNamesRequested.add(databaseLogFileInfo.getFileName());
                                // Guarda conteúdo dos arquivos de log
                                File databaseLogFile = new File(dirDatabaseLog, databaseLogFileInfo.getFileName());
                                Scanner scanner = new Scanner(databaseLogFile);
                                scanner.useDelimiter("\\Z");
                                databaseLogFilesRequested.add(scanner.next().replace("\r", "").replace("\n", ""));
                            }

                            // Forma mensagem ao servidor
                            String fileNamesRequestedJson = gson.toJson(fileNamesRequested);
                            String databaseLogFilesRequestedJson = gson.toJson(databaseLogFilesRequested);

                            String messageToServer = "DatabaseLogFile";
                            messageToServer += "|" + fileNamesRequestedJson;
                            messageToServer += "|" + databaseLogFilesRequestedJson;
                            messageToServer += "\n"; // Para que o servidor encontre o fim do texto

                            clientSocket.getOutputStream().write(messageToServer.getBytes());

                            break;
                        case "DatabaseLogFile":
                            // Recebe do servidor uma lista de logs para salvar no cliente
                            String fileNamesJson = receivedSplitted[1];
                            String databaseLogFilesJson = receivedSplitted[2];

                            Type typeStringList = new TypeToken<ArrayList<String>>() {
                            }.getType();

                            // Lista com nomes dos arquivos recebidos
                            List<String> fileNames = gson.fromJson(fileNamesJson, typeStringList);
                            // Lista com conteúdo dos arquivos recebidos
                            List<String> databaseLogFiles = gson.fromJson(databaseLogFilesJson, typeStringList);

                            DAOContagem daoContagem = new DAOContagem(conexaoBanco);
                            DAOContagemProduto daoContagemProduto = new DAOContagemProduto(conexaoBanco);
                            DAOFornecedor daoFornecedor = new DAOFornecedor(conexaoBanco);
                            DAOLoja daoLoja = new DAOLoja(conexaoBanco);
                            DAOMarca daoMarca = new DAOMarca(conexaoBanco);
                            DAOOperadoraCartao daoOperadoraCartao = new DAOOperadoraCartao(conexaoBanco);
                            DAOProduto daoProduto = new DAOProduto(conexaoBanco);
                            DAORecebimentoCartao daoRecebimentoCartao = new DAORecebimentoCartao(conexaoBanco);
                            DAOTipoContagem daoTipoContagem = new DAOTipoContagem(conexaoBanco);

                            List<DatabaseLogFile<Contagem>> logsContagem = new ArrayList<>();
                            List<DatabaseLogFile<ContagemProduto>> logsContagemProduto = new ArrayList<>();
                            List<DatabaseLogFile<Fornecedor>> logsFornecedor = new ArrayList<>();
                            List<DatabaseLogFile<Loja>> logsLoja = new ArrayList<>();
                            List<DatabaseLogFile<Marca>> logsMarca = new ArrayList<>();
                            List<DatabaseLogFile<OperadoraCartao>> logsOperadoraCartao = new ArrayList<>();
                            List<DatabaseLogFile<Produto>> logsProduto = new ArrayList<>();
                            List<DatabaseLogFile<RecebimentoCartao>> logsRecebimentoCartao = new ArrayList<>();
                            List<DatabaseLogFile<TipoContagem>> logsTipoContagem = new ArrayList<>();

                            // Para cada arquivo recebido
                            for (int i = 0; i < fileNames.size(); i++) {
                                DatabaseLogFile databaseLogFile = null;
                                String[] fileNameSplitted = fileNames.get(i).split(" ");
                                String tipoEmString = fileNameSplitted[0];

                                Type tipoDatabaseLogFile = getGsonType(tipoEmString);
                                databaseLogFile = operacoesJsonDatabaseLog.lerJsonDatabaseLogFile(databaseLogFiles.get(i), tipoDatabaseLogFile);

                                switch (tipoEmString) {
                                    case "Contagem":
                                        logsContagem.add((DatabaseLogFile<Contagem>) databaseLogFile);
                                        break;
                                    case "ContagemProduto":
                                        logsContagemProduto.add((DatabaseLogFile<ContagemProduto>) databaseLogFile);
                                        break;
                                    case "Fornecedor":
                                        logsFornecedor.add((DatabaseLogFile<Fornecedor>) databaseLogFile);
                                        break;
                                    case "Loja":
                                        logsLoja.add((DatabaseLogFile<Loja>) databaseLogFile);
                                        break;
                                    case "Marca":
                                        logsMarca.add((DatabaseLogFile<Marca>) databaseLogFile);
                                        break;
                                    case "OperadoraCartao":
                                        logsOperadoraCartao.add((DatabaseLogFile<OperadoraCartao>) databaseLogFile);
                                        break;
                                    case "Produto":
                                        logsProduto.add((DatabaseLogFile<Produto>) databaseLogFile);
                                        break;
                                    case "RecebimentoCartao":
                                        logsRecebimentoCartao.add((DatabaseLogFile<RecebimentoCartao>) databaseLogFile);
                                        break;
                                    case "TipoContagem":
                                        logsTipoContagem.add((DatabaseLogFile<TipoContagem>) databaseLogFile);
                                        break;
                                }

                                DatabaseLogFileInfo databaseLogFileInfo = new DatabaseLogFileInfo();
                                databaseLogFileInfo.setFileName(databaseLogFile.getFileName());
                                fileInfoLogsRecebidos.add(databaseLogFileInfo);
                            }

                            persistLogs(daoContagem, logsContagem);
                            persistLogs(daoContagemProduto, logsContagemProduto);
                            persistLogs(daoFornecedor, logsFornecedor);
                            persistLogs(daoLoja, logsLoja);
                            persistLogs(daoMarca, logsMarca);
                            persistLogs(daoOperadoraCartao, logsOperadoraCartao);
                            persistLogs(daoProduto, logsProduto);
                            persistLogs(daoRecebimentoCartao, logsRecebimentoCartao);
                            persistLogs(daoTipoContagem, logsTipoContagem);

                            break;
                    }
                } catch (SocketException se) {
                    Log.e(ActivityBaseView.LOG, se.getMessage(), se);
                    Log.i(ActivityBaseView.LOG, "Erro Ao Receber LOGS");
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
            }
        }
    }

    /**
     * Executado quando a aplicação é iniciada. Compara os logs locais com os logs remotos e atualiza de acordo
     */
    private void applicationOpening() {
        try {
            // Logs locais
            File[] files = dirDatabaseLog.listFiles();
            List<DatabaseLogFileInfo> databaseLogFileInfos = new ArrayList<>();

            if (files != null) {
                for (File file : files) {
                    DatabaseLogFileInfo databaseLogFileInfo = new DatabaseLogFileInfo();
                    String fileName = file.getName();
                    ZonedDateTime lastModified;

                    Scanner scanner = new Scanner(file);
                    scanner.useDelimiter("\\Z");

                    Type tipoDatabaseLogFile = getGsonType(fileName);
                    DatabaseLogFile databaseLogFile = operacoesJsonDatabaseLog.lerJsonDatabaseLogFile(scanner.next(), tipoDatabaseLogFile);

                    lastModified = databaseLogFile.getLastWriteTime();
                    databaseLogFileInfo.setFileName(fileName);
                    databaseLogFileInfo.setLastModified(lastModified);

                    databaseLogFileInfos.add(databaseLogFileInfo);
                }
            }

            // Envia as informações para o servidor
            String databaseLogFileInfosJson = "DatabaseLogFileInfo|" + gson.toJson(databaseLogFileInfos) + "\n";
            clientSocket.getOutputStream().write(databaseLogFileInfosJson.getBytes());
        } catch (IOException ioe) {
            Log.e(ActivityBaseView.LOG, ioe.getMessage(), ioe);
        }
    }

    /**
     * Insere no banco de dados os logs recebidos
     *
     * @param dao  DAO do tipo dos logs
     * @param logs Lista de logs
     * @param <E>  Tipo da entidade do log
     */
    private <E extends IModel> void persistLogs(ADAO<E> dao, List<DatabaseLogFile<E>> logs) {
        if (logs.size() > 0) {
            List<DatabaseLogFile<E>> saveLogs = logs.stream()
                    .filter(databaseLogFile -> databaseLogFile.getOperacaoMySQL().equals("INSERT"))
                    .collect(Collectors.toList());
            List<DatabaseLogFile<E>> updateLogs = logs.stream()
                    .filter(databaseLogFile -> databaseLogFile.getOperacaoMySQL().equals("UPDATE"))
                    .collect(Collectors.toList());
            List<DatabaseLogFile<E>> deleteLogs = logs.stream()
                    .filter(databaseLogFile -> databaseLogFile.getOperacaoMySQL().equals("DELETE"))
                    .collect(Collectors.toList());

            if (saveLogs.size() > 0) {
                List<E> lista = saveLogs.stream().map(DatabaseLogFile::getEntidade).collect(Collectors.toList());
                if (dao.inserir(lista)) {
                    for (DatabaseLogFile<E> databaseLogFile : saveLogs) {
                        operacoesJsonDatabaseLog.escreverJson(databaseLogFile);
                    }
                }
            }

            if (updateLogs.size() > 0) {
                List<E> lista = updateLogs.stream().map(DatabaseLogFile::getEntidade).collect(Collectors.toList());
                if (dao.inserirOuAtualizar(lista)) {
                    for (DatabaseLogFile<E> databaseLogFile : updateLogs) {
                        operacoesJsonDatabaseLog.escreverJson(databaseLogFile);
                    }
                }
            }

            if (deleteLogs.size() > 0) {
                List<E> lista = deleteLogs.stream().map(DatabaseLogFile::getEntidade).collect(Collectors.toList());
                dao.deletar(lista);
                for (DatabaseLogFile<E> databaseLogFile : deleteLogs) {
                    operacoesJsonDatabaseLog.escreverJson(databaseLogFile);
                }
            }
        }
    }

    /**
     * Retorna o tipo de DatabaseLogFile
     *
     * @param fileName Nome do arquivo de log recebido
     * @return Tipo de DatabaseLogFile
     */
    private Type getGsonType(String fileName) {
        String[] fileNameSplitted = fileName.split(" ");
        String className = fileNameSplitted[0];

        switch (className) {
            case "Contagem":
                return new TypeToken<DatabaseLogFile<Contagem>>() {
                }.getType();
            case "ContagemProduto":
                return new TypeToken<DatabaseLogFile<ContagemProduto>>() {
                }.getType();
            case "Fornecedor":
                return new TypeToken<DatabaseLogFile<Fornecedor>>() {
                }.getType();
            case "Loja":
                return new TypeToken<DatabaseLogFile<Loja>>() {
                }.getType();
            case "Marca":
                return new TypeToken<DatabaseLogFile<Marca>>() {
                }.getType();
            case "OperadoraCartao":
                return new TypeToken<DatabaseLogFile<OperadoraCartao>>() {
                }.getType();
            case "Produto":
                return new TypeToken<DatabaseLogFile<Produto>>() {
                }.getType();
            case "RecebimentoCartao":
                return new TypeToken<DatabaseLogFile<RecebimentoCartao>>() {
                }.getType();
            case "TipoContagem":
                return new TypeToken<DatabaseLogFile<TipoContagem>>() {
                }.getType();
            default:
                return null;
        }
    }

    public List<DatabaseLogFileInfo> getFileInfoLogsRecebidos() {
        return fileInfoLogsRecebidos;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
