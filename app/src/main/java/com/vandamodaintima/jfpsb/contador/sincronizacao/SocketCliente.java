package com.vandamodaintima.jfpsb.contador.sincronizacao;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
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
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.model.dao.IDAO;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SocketCliente extends Thread {
    private Context context;
    private static File dirDatabaseLog = null;
    Gson gson = null;
    private Socket clientSocket;
    private List<DatabaseLogFileInfo> fileInfoLogsRecebidos = new ArrayList<>();
    private ConexaoBanco conexaoBanco;

    public SocketCliente(Context context, ConexaoBanco conexaoBanco) {
        this.context = context;
        this.conexaoBanco = conexaoBanco;
        dirDatabaseLog = context.getDir("DatabaseLog", Context.MODE_PRIVATE);

        gson = new GsonBuilder()
                .registerTypeAdapter(ZonedDateTime.class, new JsonDeserializer<ZonedDateTime>() {
                    @Override
                    public ZonedDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                        String data = json.getAsJsonPrimitive().getAsString();
                        return ZonedDateTime.parse(data, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                    }
                })
                .registerTypeAdapter(ZonedDateTime.class, new JsonSerializer<ZonedDateTime>() {
                    @Override
                    public JsonElement serialize(ZonedDateTime zonedDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
                        return new JsonPrimitive(zonedDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
                    }
                })
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssz")
                .create();
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
                            String dataLogFileRequest = receivedSplitted[1];
                            List<DatabaseLogFileInfo> databaseLogFileInfos = gson.fromJson(dataLogFileRequest, new TypeToken<ArrayList<DatabaseLogFileInfo>>() {
                            }.getType());
                            List<String> fileNamesRequested = new ArrayList<>();
                            List<String> databaseLogFilesRequested = new ArrayList<>();

                            for (DatabaseLogFileInfo databaseLogFileInfo : databaseLogFileInfos) {
                                fileNamesRequested.add(databaseLogFileInfo.getFileName());

                                File databaseLogFile = new File(dirDatabaseLog.getPath(), databaseLogFileInfo.getFileName());
                                Scanner scanner = new Scanner(databaseLogFile);
                                scanner.useDelimiter("\\Z");
                                databaseLogFilesRequested.add(scanner.next());
                            }

                            String fileNamesRequestedJson = gson.toJson(fileNamesRequested);
                            String databaseLogFilesRequestedJson = gson.toJson(databaseLogFilesRequested);

                            String messageToServer = "DatabaseLogFile";
                            messageToServer += "|" + fileNamesRequestedJson;
                            messageToServer += "|" + databaseLogFilesRequestedJson;
                            messageToServer += "\n"; // Para que o servidor encontre o fim do texto

                            clientSocket.getOutputStream().write(messageToServer.getBytes());

                            break;
                        case "DatabaseLogFile":
                            String fileNamesDatabaseLogFileJson = receivedSplitted[1];
                            String databaseLogFilesDatabaseLogFileJson = receivedSplitted[2];

                            List<String> fileNamesDatabaseLogFile = gson.fromJson(fileNamesDatabaseLogFileJson, new TypeToken<ArrayList<String>>() {
                            }.getType());
                            List<String> databaseLogFilesDatabaseLogFile = gson.fromJson(databaseLogFilesDatabaseLogFileJson, new TypeToken<ArrayList<String>>() {
                            }.getType());

                            DAOContagem daoContagem = new DAOContagem(conexaoBanco);
                            DAOContagemProduto daoContagemProduto = new DAOContagemProduto(conexaoBanco);
                            DAOFornecedor daoFornecedor = new DAOFornecedor(conexaoBanco);
                            DAOLoja daoLoja = new DAOLoja(conexaoBanco);
                            DAOMarca daoMarca = new DAOMarca(conexaoBanco);
                            DAOOperadoraCartao daoOperadoraCartao = new DAOOperadoraCartao(conexaoBanco);
                            DAOProduto daoProduto = new DAOProduto(conexaoBanco);
                            DAORecebimentoCartao daoRecebimentoCartao = new DAORecebimentoCartao(conexaoBanco);
                            DAOTipoContagem daoTipoContagem = new DAOTipoContagem(conexaoBanco);

                            List<DatabaseLogFile<Contagem>> logsContagem = new ArrayList<DatabaseLogFile<Contagem>>();
                            List<DatabaseLogFile<ContagemProduto>> logsContagemProduto = new ArrayList<DatabaseLogFile<ContagemProduto>>();
                            List<DatabaseLogFile<Fornecedor>> logsFornecedor = new ArrayList<DatabaseLogFile<Fornecedor>>();
                            List<DatabaseLogFile<Loja>> logsLoja = new ArrayList<DatabaseLogFile<Loja>>();
                            List<DatabaseLogFile<Marca>> logsMarca = new ArrayList<DatabaseLogFile<Marca>>();
                            List<DatabaseLogFile<OperadoraCartao>> logsOperadoraCartao = new ArrayList<DatabaseLogFile<OperadoraCartao>>();
                            List<DatabaseLogFile<Produto>> logsProduto = new ArrayList<DatabaseLogFile<Produto>>();
                            List<DatabaseLogFile<RecebimentoCartao>> logsRecebimentoCartao = new ArrayList<DatabaseLogFile<RecebimentoCartao>>();
                            List<DatabaseLogFile<TipoContagem>> logsTipoContagem = new ArrayList<DatabaseLogFile<TipoContagem>>();

                            for (int i = 0; i < fileNamesDatabaseLogFile.size(); i++) {
                                Type tipoDatabaseLogFile = getGsonType(fileNamesDatabaseLogFile.get(i));
                                DatabaseLogFile databaseLogFile = gson.fromJson(databaseLogFilesDatabaseLogFile.get(i), tipoDatabaseLogFile);

                                if (databaseLogFile.getFileName().startsWith("Contagem")) {
                                    logsContagem.add((DatabaseLogFile<Contagem>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("ContagemProduto")) {
                                    logsContagemProduto.add((DatabaseLogFile<ContagemProduto>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("Fornecedor")) {
                                    logsFornecedor.add((DatabaseLogFile<Fornecedor>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("Loja")) {
                                    logsLoja.add((DatabaseLogFile<Loja>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("Marca")) {
                                    logsMarca.add((DatabaseLogFile<Marca>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("OperadoraCartao")) {
                                    logsOperadoraCartao.add((DatabaseLogFile<OperadoraCartao>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("Produto")) {
                                    logsProduto.add((DatabaseLogFile<Produto>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("RecebimentoCartao")) {
                                    logsRecebimentoCartao.add((DatabaseLogFile<RecebimentoCartao>) databaseLogFile);
                                } else if (databaseLogFile.getFileName().startsWith("TipoContagem")) {
                                    logsTipoContagem.add((DatabaseLogFile<TipoContagem>) databaseLogFile);
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
                    System.out.println(se.getMessage());
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
                ex.printStackTrace();
            }
        }
    }

    private void applicationOpening() {
        try {
            File[] files = dirDatabaseLog.listFiles();
            List<DatabaseLogFileInfo> databaseLogFileInfos = new ArrayList<>();

            for (File file : files) {
                DatabaseLogFileInfo databaseLogFileInfo = new DatabaseLogFileInfo();
                String fileName = file.getName();
                ZonedDateTime lastModified;

                Scanner scanner = new Scanner(file);
                scanner.useDelimiter("\\Z");

                Type tipoDatabaseLogFile = getGsonType(fileName);

                DatabaseLogFile databaseLogFile = gson.fromJson(scanner.next(), tipoDatabaseLogFile);
                lastModified = databaseLogFile.getLastWriteTime();

                databaseLogFileInfo.setFileName(fileName);
                databaseLogFileInfo.setLastModified(lastModified);

                databaseLogFileInfos.add(databaseLogFileInfo);
            }

            String databaseLogFileInfosJson = "DatabaseLogFileInfo|" + gson.toJson(databaseLogFileInfos) + "\n";
            clientSocket.getOutputStream().write(databaseLogFileInfosJson.getBytes());
        } catch (IOException ioe) {
            Log.e(ActivityBaseView.LOG, ioe.getMessage(), ioe);
        }
    }

    private <E extends IModel> void persistLogs(IDAO<E> dao, List<DatabaseLogFile<E>> logs) {
        if (logs.size() > 0) {
            List<E> saveList = new ArrayList<>();
            List<E> updateList = new ArrayList<>();
            List<E> deleteList = new ArrayList<>();
            for (DatabaseLogFile<E> databaseLogFile : logs) {
                switch (databaseLogFile.getOperacaoMySQL()) {
                    case "INSERT":
                        saveList.add(databaseLogFile.getEntidade());
                        break;
                    case "UPDATE":
                        updateList.add(databaseLogFile.getEntidade());
                        break;
                    case "DELETE":
                        deleteList.add(databaseLogFile.getEntidade());
                        break;
                }
            }

            if (saveList.size() > 0) {
                if (dao.inserir(saveList)) {
                    for (E e : saveList) {
                        escreverJson("INSERT", e);
                    }
                }
            }

            if (updateList.size() > 0) {
                if (dao.inserirOuAtualizar(updateList)) {
                    for (E e : updateList) {
                        escreverJson("UPDATE", e);
                    }
                }
            }

            if (deleteList.size() > 0) {
                dao.deletar(deleteList);
                for (E e : deleteList) {
                    escreverJson("DELETE", e);
                }
            }
        }
    }

    private <E extends IModel> void escreverJson(String operacao, E entidade) {
        ZonedDateTime lastWriteTime = Instant.now().atZone(ZoneId.systemDefault());
        DatabaseLogFile<E> databaseLogFile = new DatabaseLogFile<>();
        databaseLogFile.setLastWriteTime(lastWriteTime);
        databaseLogFile.setOperacaoMySQL(operacao);
        databaseLogFile.setEntidade(entidade);

        String json = gson.toJson(databaseLogFile, new TypeToken<DatabaseLogFile<E>>() {
        }.getType());
        File logFile = new File(dirDatabaseLog, databaseLogFile.getFileName());

        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(logFile);
            fileWriter.write(json);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Type getGsonType(String fileName) {
        Type tipoDatabaseLogFile = null;

        if (fileName.startsWith("Contagem")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<Contagem>>() {
            }.getType();
        } else if (fileName.startsWith("ContagemProduto")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<ContagemProduto>>() {
            }.getType();
        } else if (fileName.startsWith("Fornecedor")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<Fornecedor>>() {
            }.getType();
        } else if (fileName.startsWith("Loja")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<Loja>>() {
            }.getType();
        } else if (fileName.startsWith("Marca")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<Marca>>() {
            }.getType();
        } else if (fileName.startsWith("OperadoraCartao")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<OperadoraCartao>>() {
            }.getType();
        } else if (fileName.startsWith("Produto")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<Produto>>() {
            }.getType();
        } else if (fileName.startsWith("RecebimentoCartao")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<RecebimentoCartao>>() {
            }.getType();
        } else if (fileName.startsWith("TipoContagem")) {
            tipoDatabaseLogFile = new TypeToken<DatabaseLogFile<TipoContagem>>() {
            }.getType();
        }

        return tipoDatabaseLogFile;
    }

}
