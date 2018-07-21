package com.vandamodaintima.jfpsb.contador.arquivo;

import android.content.ContentResolver;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Arquivo {
    private static final String app = "Contador";

    public static InputStream getInputStreamFromUri(ContentResolver contentResolver, Uri uri) {
        try {
            return contentResolver.openInputStream(uri);
        } catch (FileNotFoundException e) {
            Log.i(app, e.getMessage());
            return null;
        }
    }
}
