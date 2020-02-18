package com.vandamodaintima.jfpsb.contador.util.sincronizacao;

import android.content.Context;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.model.IModel;
import com.vandamodaintima.jfpsb.contador.util.sincronizacao.entidademysql.EntidadeMySQL;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ArquivoEntidade<E extends IModel> {
    private final String diretorio;
    private Context context;
    private Class<E> entidadeTipo;

    ArquivoEntidade(Context context, Class<E> entidadeTipo) {
        this.context = context;
        this.entidadeTipo = entidadeTipo;
        this.diretorio = "EntidadesSalvas";
    }

    public void escreverXml(EntidadeMySQL entidadeMySQL) {
        try {
            /*File dir = context.getDir(diretorio, Context.MODE_PRIVATE);
            File subDir = new File(dir, entidadeTipo.getSimpleName());
            if (!subDir.isDirectory())
                subDir.mkdir();
            xmlStrategy.write(subDir.getPath(), serializer, entidadeMySQL);*/
        } catch (Exception e) {
            Log.e("Contador", e.getMessage(), e);
        }
    }

    ArrayList<EntidadeMySQL> lerXmlLocal(Date lastUpdate) throws Exception {
        ArrayList<EntidadeMySQL> lista = new ArrayList<>();

        /*File dir = context.getDir(diretorio, Context.MODE_PRIVATE);
        File subDir = new File(dir, entidadeTipo.getSimpleName());

        if (subDir.isDirectory()) {
            Serializer serializer = new Persister();
            File[] arquivos = subDir.listFiles();

            for (File arquivo : arquivos) {
                Date lastModified = new Date(arquivo.lastModified());

                if (lastModified.after(lastUpdate) || lastModified.equals(lastUpdate)) {
                    EntidadeMySQL entidadeMySQL = xmlStrategy.read(serializer, arquivo);
                    lista.add(entidadeMySQL);
                }
            }
        }*/

        return lista;
    }

    public ArrayList<EntidadeMySQL> lerXmlRemoto(Date lastUpdate) throws Exception {
        ArrayList<EntidadeMySQL> lista = new ArrayList<>();
        File dir = context.getDir(diretorio, Context.MODE_PRIVATE);
        File caminhoRemoto = new File(diretorio + "/" + entidadeTipo.getSimpleName());
        File caminhoLocal = new File(dir, entidadeTipo.getSimpleName());

        if (!caminhoLocal.isDirectory())
            caminhoLocal.mkdir();

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("ftp.vandamodaintima.com.br");
        ftpClient.login("syncftp@vandamodaintima.com.br", "Jfpsb5982jf");
        // Vai para o diretório remoto se ele existir, se não, cria
        boolean dirResult = ftpClient.changeWorkingDirectory(caminhoRemoto.getPath());

        if (!dirResult) {
            ftpClient.makeDirectory(caminhoRemoto.getPath());
            ftpClient.changeWorkingDirectory(caminhoRemoto.getPath());
        }

        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        FTPFile[] arquivosRemotos = ftpClient.listFiles();
        File[] arquivosLocais = caminhoLocal.listFiles();

        HashMap<String, File> arquivosLocaisMap = new HashMap<>();
        for (File arquivoLocal : arquivosLocais) {
            arquivosLocaisMap.put(arquivoLocal.getName(), arquivoLocal);
        }

        for (FTPFile arquivoRemoto : arquivosRemotos) {
            if (arquivoRemoto.getName().length() >= 5 && arquivoRemoto.getName().endsWith(".xml")) {
                boolean isOutDated = true;
                File arquivoLocal = new File(caminhoLocal + "/" + arquivoRemoto.getName());
                Calendar modificadoRemoto = arquivoRemoto.getTimestamp();

                if (arquivosLocaisMap.containsKey(arquivoRemoto.getName())) {
                    arquivoLocal = arquivosLocaisMap.get(arquivoRemoto.getName());
                    Calendar modificadoLocal = Calendar.getInstance();
                    modificadoLocal.setTimeInMillis(arquivoLocal.lastModified());

                    if (modificadoRemoto.after(modificadoLocal)) {
                        isOutDated = false;
                    }
                }

                if (isOutDated) {
                    OutputStream outputStream = new FileOutputStream(arquivoLocal);

                    boolean result = ftpClient.retrieveFile(arquivoRemoto.getName(), outputStream);

                    if (result) {
                        arquivoLocal.setLastModified(modificadoRemoto.getTimeInMillis());
                    }

                    outputStream.close();
                }
            }
        }

        arquivosLocais = caminhoLocal.listFiles();

        /*Serializer serializer = new Persister();

        for (File arquivoLocal : arquivosLocais) {
            if (arquivoLocal.getName().endsWith(".xml")) {
                Date localModificado = new Date(arquivoLocal.lastModified());
                if (localModificado.after(lastUpdate) || localModificado.equals(lastUpdate)) {
                    EntidadeMySQL entidadeMySQL = xmlStrategy.read(serializer, arquivoLocal);
                    lista.add(entidadeMySQL);
                }
            }
        }*/

        return lista;
    }

    public void enviaXmlRemoto(Date lastUpdate) throws Exception {
        File dir = context.getDir(diretorio, Context.MODE_PRIVATE);
        File caminhoRemoto = new File(diretorio + "/" + entidadeTipo.getSimpleName());
        File caminhoLocal = new File(dir, entidadeTipo.getSimpleName());

        FTPClient ftpClient = new FTPClient();
        ftpClient.connect("ftp.vandamodaintima.com.br");
        ftpClient.login("syncftp@vandamodaintima.com.br", "Jfpsb5982jf");
        ftpClient.changeWorkingDirectory(caminhoRemoto.getPath());
        ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

        if (!caminhoLocal.isDirectory())
            caminhoLocal.mkdir();

        FTPFile[] arquivosRemotos = ftpClient.listFiles();
        File[] arquivosLocais = caminhoLocal.listFiles();

        HashMap<String, FTPFile> arquivosRemotosMap = new HashMap<>();
        for (FTPFile arquivoRemoto : arquivosRemotos) {
            arquivosRemotosMap.put(arquivoRemoto.getName(), arquivoRemoto);
        }

        for (File arquivoLocal : arquivosLocais) {
            if (arquivoLocal.getName().length() >= 5) {
                boolean isOutDated = false;
                Calendar modificadoLocal = Calendar.getInstance();
                modificadoLocal.setTimeInMillis(arquivoLocal.lastModified());

                if (arquivosRemotosMap.containsKey(arquivoLocal.getName())) {
                    FTPFile arquivoRemoto = arquivosRemotosMap.get(arquivoLocal.getName());
                    Calendar modificadoRemoto = arquivoRemoto.getTimestamp();

                    if (modificadoLocal.after(modificadoRemoto)) {
                        isOutDated = true;
                    }
                }

                if (isOutDated) {
                    FileInputStream inputStream = new FileInputStream(arquivoLocal);
                    ftpClient.storeFile(arquivoLocal.getName(), inputStream);
                    inputStream.close();
                }
            }
        }
    }
}
