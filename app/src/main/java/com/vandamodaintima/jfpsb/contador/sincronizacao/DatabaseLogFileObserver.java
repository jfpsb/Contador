package com.vandamodaintima.jfpsb.contador.sincronizacao;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DatabaseLogFileObserver extends FileObserver {
    private SocketCliente socketCliente;
    private Gson gson;

    DatabaseLogFileObserver(Gson gson, File dirDatabaseLog, SocketCliente socketCliente) {
        super(dirDatabaseLog.getPath(), FileObserver.MODIFY);
        this.socketCliente = socketCliente;
        this.gson = gson;
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        if (event == FileObserver.MODIFY) {
            try {
                File arquivo = new File(path);

                Log.i(ActivityBaseView.LOG, "Arquivo Editado: " + arquivo.getName());

                DatabaseLogFileInfo databaseLogFileInfo = new DatabaseLogFileInfo();
                databaseLogFileInfo.setFileName(arquivo.getName());

                if (!socketCliente.getFileInfoLogsRecebidos().contains(databaseLogFileInfo)) {
                    List<String> fileNames = new ArrayList<>();
                    List<String> databaseLogFiles = new ArrayList<>();
                    fileNames.add(databaseLogFileInfo.getFileName());

                    Scanner scanner = new Scanner(arquivo);
                    scanner.useDelimiter("\\Z");

                    databaseLogFiles.add(scanner.next().replace("\r", "").replace("\n", ""));

                    String fileNameJson = gson.toJson(fileNames);
                    String databaseLogFilesJson = gson.toJson(databaseLogFiles);

                    String messageToServer = "DatabaseLogFile";
                    messageToServer += "|" + fileNameJson;
                    messageToServer += "|" + databaseLogFilesJson;
                    messageToServer += "\n"; // Para que o servidor encontre o fim do texto

                    socketCliente.getClientSocket().getOutputStream().write(messageToServer.getBytes());
                }

                socketCliente.getFileInfoLogsRecebidos(). remove(databaseLogFileInfo);
            } catch (IOException ioe) {
                Log.e(ActivityBaseView.LOG, ioe.getMessage(), ioe);
                Log.i(ActivityBaseView.LOG, "Erro ao Enviar Log Em FileObserver");
            }
        }
    }
}
