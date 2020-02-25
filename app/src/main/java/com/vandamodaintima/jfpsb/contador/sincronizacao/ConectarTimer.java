package com.vandamodaintima.jfpsb.contador.sincronizacao;

import android.util.Log;

import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.io.IOException;
import java.net.Socket;
import java.util.TimerTask;

public class ConectarTimer extends TimerTask {
    private Socket socket = null;

    @Override
    public void run() {
        try {
            socket = new Socket("192.168.0.15", 3999);
        } catch (IOException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        }
    }

    public Socket getSocket() {
        return socket;
    }
}
