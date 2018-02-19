package com.vandamodaintima.jfpsb.contador.util;

import android.content.Context;
import android.util.Log;

import com.vandamodaintima.jfpsb.contador.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by jfpsb on 17/02/2018.
 */

public class ManipulaArquivo {
    public static void colocaArquivoProdutosEmFiles(Context context) {
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            File destino = new File(context.getFilesDir(), "produtos.xlsx");

            if(!destino.exists()) {
                inputStream = context.getResources().openRawResource(R.raw.produtos);

                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);

                outputStream = new FileOutputStream(destino);
                outputStream.write(bytes);
            }
        } catch (Exception e) {
            Log.i("Contador", e.getMessage());
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                Log.i("Contador", "Erro ao fechar inputStream. " + e.getMessage());
            }

            try {
                outputStream.close();
            } catch (Exception e) {
                Log.i("Contador", "Erro ao fechar outputStream. " + e.getMessage());
            }
        }
    }
}
