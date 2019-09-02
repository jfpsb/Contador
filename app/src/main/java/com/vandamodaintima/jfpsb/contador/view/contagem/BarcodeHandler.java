package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import android.view.TextureView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.lang.ref.WeakReference;

public class BarcodeHandler extends Handler {
    private WeakReference<TextureView> textureViewWeakReference;
    private WeakReference<BarcodeDetector> barcodeDetectorWeakReference;
    private String result = null;

    BarcodeHandler(Looper looper, TextureView textureView, BarcodeDetector barcodeDetector) {
        super(looper);
        textureViewWeakReference = new WeakReference<>(textureView);
        barcodeDetectorWeakReference = new WeakReference<>(barcodeDetector);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 1) {
            Bitmap bitmap = Bitmap.createBitmap(textureViewWeakReference.get().getWidth(), textureViewWeakReference.get().getHeight(), Bitmap.Config.ARGB_8888);
            textureViewWeakReference.get().getBitmap(bitmap);

            if (barcodeDetectorWeakReference.get().isOperational()) {
                Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                SparseArray<Barcode> barcodeSparseArray = barcodeDetectorWeakReference.get().detect(frame);

                for (int i = 0; i < barcodeSparseArray.size(); i++) {
                    Barcode barcode = barcodeSparseArray.valueAt(i);
                    result = barcode.rawValue;
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public String getResult() {
        return result;
    }

    public void setResult(String s) {
        result = s;
    }
}
