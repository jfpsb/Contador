package com.vandamodaintima.jfpsb.contador.sincronizacao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OperacoesJsonDatabaseLog {
    private Gson gson;
    private File dirDatabaseLog;

    public OperacoesJsonDatabaseLog(Gson gson, File dirDatabaseLog) {
        this.gson = gson;
        this.dirDatabaseLog = dirDatabaseLog;
    }

    public  <E extends IModel> void escreverJson(DatabaseLogFile<E> databaseLogFile) {
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

    public <E extends IModel> void escreverJson(String operacao, E entidade) {
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
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        }
    }

    /**
     * Deserializa lista de DatabaseLogFile
     *
     * @param json Json a ser deserializado
     * @param type Tipo para método do Gson
     * @return DatabaseLogFile deserializado
     */
    public DatabaseLogFile lerJsonDatabaseLogFile(String json, Type type) {
        return gson.fromJson(json, type);
    }

    /**
     * Deserializa lista de DatabaseLogFileInfo
     *
     * @param json Json a ser deserializado
     * @return Lista de DatabaseLogFileInfo
     */
    public List<DatabaseLogFileInfo> lerJsonDatabaseLogFileInfo(String json) {
        Type fileInfoType = new TypeToken<ArrayList<DatabaseLogFileInfo>>() {
        }.getType();
        return gson.fromJson(json, fileInfoType);
    }
}
