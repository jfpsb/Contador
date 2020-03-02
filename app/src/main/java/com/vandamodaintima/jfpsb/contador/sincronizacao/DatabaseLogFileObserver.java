package com.vandamodaintima.jfpsb.contador.sincronizacao;

import android.os.FileObserver;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.temporal.ChronoUnit;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static org.threeten.bp.ZoneId.systemDefault;

public class DatabaseLogFileObserver extends FileObserver {
    private SocketCliente socketCliente;
    private Gson gson;
    private File dirDatabaseLog;
    private HashMap<String, LocalDateTime> logLastModify;

    DatabaseLogFileObserver(Gson gson, File dirDatabaseLog, SocketCliente socketCliente) {
        super(dirDatabaseLog.getPath(), FileObserver.MODIFY);
        this.socketCliente = socketCliente;
        this.dirDatabaseLog = dirDatabaseLog;
        this.gson = gson;
        logLastModify = new HashMap<>();
    }

    @Override
    public void onEvent(int event, @Nullable String path) {
        if (event == FileObserver.MODIFY) {
            if(path != null) {
                try {
                    File arquivo = new File(dirDatabaseLog, path);

                    if (logLastModify.containsKey(arquivo.getName())) {
                        long it = intervalo(logLastModify.get(arquivo.getName()));
                        if (it < 500)
                            return;
                    }

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

                        logLastModify.put(arquivo.getName(), LocalDateTime.now(systemDefault()));

                        socketCliente.getClientSocket().getOutputStream().write(messageToServer.getBytes());
                    }

                    socketCliente.getFileInfoLogsRecebidos().remove(databaseLogFileInfo);
                } catch (IOException ioe) {
                    Log.e(ActivityBaseView.LOG, ioe.getMessage(), ioe);
                    Log.i(ActivityBaseView.LOG, "Erro ao Enviar Log Em FileObserver");
                }
            }
        }
    }

    private long intervalo(LocalDateTime lastWriteTime) {
        LocalDateTime now = LocalDateTime.now(systemDefault());
        return ChronoUnit.MILLIS.between(lastWriteTime, now);
    }
}
