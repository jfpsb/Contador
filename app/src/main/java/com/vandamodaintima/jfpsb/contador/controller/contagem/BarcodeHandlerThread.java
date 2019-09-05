package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.TextureView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.lang.ref.WeakReference;

public class BarcodeHandlerThread extends HandlerThread {
    private Handler handler;

    private WeakReference<TextureView> textureViewWeakReference;
    private BarcodeDetector barcodeDetector;
    private TelaLerCodigoDeBarraController controller;
    private SparseArray<Barcode> barcodeSparseArray = new SparseArray<>();

    private boolean textureViewVisible = true;

    public BarcodeHandlerThread() {
        super("BarcodeHandlerThread");
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (textureViewVisible) {
                            Bitmap bitmap = Bitmap.createBitmap(textureViewWeakReference.get().getWidth(), textureViewWeakReference.get().getHeight(), Bitmap.Config.ARGB_8888);
                            textureViewWeakReference.get().getBitmap(bitmap);
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                            if (barcodeDetector.isOperational()) {
                                barcodeSparseArray = barcodeDetector.detect(frame);
                                if (barcodeSparseArray.size() > 0) {
                                    sendEmptyMessage(2);
                                } else {
                                    sendEmptyMessage(1);
                                }
                            } else {
                                sendEmptyMessage(1);
                            }
                        } else {
                            sendEmptyMessage(1);
                        }
                        break;
                    case 2:
                        for (int i = 0; i < barcodeSparseArray.size(); i++) {
                            Barcode barcode = barcodeSparseArray.valueAt(i);
                            Log.i("Contador", "handleMessage: " + barcode.rawValue);

                            Message message = handler.obtainMessage();
                            message.what = 3;
                            message.obj = barcode.rawValue;

                            sendMessage(message);
                        }

                        barcodeSparseArray.clear();

                        sendEmptyMessageDelayed(1, 2000);
                        break;
                    case 3:
                        controller.pesquisarProduto((String) msg.obj);
                        break;
                }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }

    public void setTextureView(TextureView textureView) {
        textureViewWeakReference = new WeakReference<>(textureView);
    }

    public void setController(TelaLerCodigoDeBarraController controller) {
        this.controller = controller;
    }

    public void setBarcodeDetector(BarcodeDetector barcodeDetector) {
        this.barcodeDetector = barcodeDetector;
    }

    public void setTextureViewVisible(boolean textureViewVisible) {
        this.textureViewVisible = textureViewVisible;
    }
}
