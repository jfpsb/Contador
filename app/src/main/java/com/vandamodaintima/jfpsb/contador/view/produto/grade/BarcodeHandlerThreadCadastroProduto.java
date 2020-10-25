package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.TextureView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.lang.ref.WeakReference;

public class BarcodeHandlerThreadCadastroProduto extends HandlerThread {
    private Handler handler;
    private WeakReference<TextureView> textureView;
    private BarcodeDetector barcodeDetector;
    private Activity view;

    public BarcodeHandlerThreadCadastroProduto(Activity view, TextureView textureView, BarcodeDetector barcodeDetector) {
        super("BarcodeHandlerThreadCadastroProduto");
        this.view = view;
        this.textureView = new WeakReference<>(textureView);
        this.barcodeDetector = barcodeDetector;
    }

    @Override
    protected void onLooperPrepared() {
        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    if (barcodeDetector.isOperational() && textureView.get().isAvailable()) {
                        Bitmap bitmap = Bitmap.createBitmap(textureView.get().getWidth(), textureView.get().getHeight(), Bitmap.Config.ARGB_8888);
                        textureView.get().getBitmap(bitmap);
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                        SparseArray<Barcode> barcodeSparseArray = barcodeDetector.detect(frame);

                        if (barcodeSparseArray.size() == 0) {
                            sendEmptyMessageDelayed(1, 250);
                        } else if (barcodeSparseArray.size() == 1) {
                            Log.i(ActivityBaseView.LOG, "Um Código de Barras Encontrado");
                            Intent intent = new Intent();
                            String codigo_lido = barcodeSparseArray.valueAt(0).rawValue;
                            intent.putExtra("codigo_lido", codigo_lido);
                            view.setResult(Activity.RESULT_OK, intent);
                            view.finish();
                        } else {
                            Log.i(ActivityBaseView.LOG, "Vários Códigos de Barras Encontrados. Quant.: " + barcodeSparseArray.size());
                            AlertDialog.Builder dialog = new AlertDialog.Builder(view);
                            dialog.setTitle("Vários Códigos Foram Lidos");

                            String[] codigos = new String[barcodeSparseArray.size()];

                            for (int i = 0; i < barcodeSparseArray.size(); i++) {
                                codigos[i] = barcodeSparseArray.valueAt(i).rawValue;
                            }

                            dialog.setSingleChoiceItems(codigos, 0, null);

                            dialog.setPositiveButton("Confirmar", (dialog1, which) -> {
                                ListView lw = ((AlertDialog) dialog1).getListView();
                                String codigo_escolhido = lw.getAdapter().getItem(lw.getCheckedItemPosition()).toString();
                                Intent intent = new Intent();
                                intent.putExtra("codigo_lido", codigo_escolhido);
                                view.setResult(Activity.RESULT_OK, intent);
                                view.finish();
                            });

                            dialog.setNegativeButton("Cancelar", (dialog12, which) -> getHandler().sendEmptyMessageDelayed(1, 1000));

                            dialog.show();
                        }
                    } else {
                        sendEmptyMessage(1);
                    }
                }
            }
        };

        getHandler().sendEmptyMessage(1);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    public Handler getHandler() {
        return handler;
    }
}
