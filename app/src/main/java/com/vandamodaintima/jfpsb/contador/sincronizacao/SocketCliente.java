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
import com.vandamodaintima.jfpsb.contador.model.dao.DAOTipoContagem;
import com.vandamodaintima.jfpsb.contador.model.RecebimentoCartao;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.ZonedDateTime;
import org.threeten.bp.format.DateTimeFormatter;

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

public class SocketCliente extends Thread {
    private Context context;
    private static final String diretorio = "DatabaseLog";
    Gson gson = null;
    private Socket clientSocket;
    private List<DatabaseLogFileInfo> fileInfoLogsRecebidos = new ArrayList<>();
    private ConexaoBanco conexaoBanco;

    public SocketCliente(Context context, ConexaoBanco conexaoBanco) {
        this.context = context;
        this.conexaoBanco = conexaoBanco;

        Gson gson = new GsonBuilder()
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

                    // Socket foi desconectado pelo lado do cliente
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

                                File databaseLogFile = new File(diretorio, databaseLogFileInfo.getFileName());
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
                            List<Object> saveUpdateList = new ArrayList<Object>();
                            List<Object> deleteList = new ArrayList<Object>();

                            DAOContagem daoContagem = new DAOContagem(conexaoBanco);
                            DAOContagemProduto daoContagemProduto = new DAOContagemProduto(conexaoBanco);
                            DAOFornecedor daoFornecedor = new DAOFornecedor(conexaoBanco);
                            DAOLoja daoLoja = new DAOLoja(conexaoBanco);
                            DAOMarca daoMarca = new DAOMarca(conexaoBanco);
                            DAOOperadoraCartao daoOperadoraCartao = new DAOOperadoraCartao(conexaoBanco);
                            DAOProduto daoProduto = new DAOProduto(conexaoBanco);
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

                            List<DatabaseLogFile> logs = new ArrayList<>();

                            for (int i = 0; i < fileNamesDatabaseLogFile.size(); i++) {
                                DatabaseLogFile databaseLogFile = gson.fromJson(databaseLogFilesDatabaseLogFile.get(i), new TypeToken<DatabaseLogFile>() {
                                }.getType());

                                DatabaseLogFileInfo databaseLogFileInfo = new DatabaseLogFileInfo();
                                databaseLogFileInfo.setFileName(databaseLogFile.getFileName());
                                fileInfoLogsRecebidos.add(databaseLogFileInfo);
                                logs.add(databaseLogFile);

                                switch (databaseLogFile.getOperacaoMySQL()) {
                                    case "INSERT":
                                        saveUpdateList.add(databaseLogFile.getEntidade());
                                        break;
                                    case "UPDATE":
                                        saveUpdateList.add(databaseLogFile.getEntidade());
                                        break;
                                    case "DELETE":
                                        deleteList.add(databaseLogFile.getEntidade());
                                        break;
                                }
                            }

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
            File dirDatabaseLog = context.getDir(diretorio, Context.MODE_PRIVATE);

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

            String databaseLogFileInfosJson = "DatabaseLogFileInfo|" + gson.toJson(databaseLogFileInfos);
        } catch (IOException ioe) {
            Log.e(ActivityBaseView.LOG, ioe.getMessage(), ioe);
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
