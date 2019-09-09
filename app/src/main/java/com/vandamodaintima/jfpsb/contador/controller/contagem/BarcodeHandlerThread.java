package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.TextureView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.contagem.MultiploCodigoBarraLido;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BarcodeHandlerThread extends HandlerThread {
    private Handler handler;

    private WeakReference<TextureView> textureViewWeakReference;
    private BarcodeDetector barcodeDetector;
    private TelaLerCodigoDeBarraController controller;
    private ITelaLerCodigoDeBarra view;
    private Contagem contagemModel;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;

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
                        if (textureViewVisible && barcodeDetector.isOperational()) {
                            Bitmap bitmap = Bitmap.createBitmap(textureViewWeakReference.get().getWidth(), textureViewWeakReference.get().getHeight(), Bitmap.Config.ARGB_8888);
                            textureViewWeakReference.get().getBitmap(bitmap);
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                            SparseArray<Barcode> barcodeSparseArray = barcodeDetector.detect(frame);

                            if (barcodeSparseArray.size() == 0) {
                                sendEmptyMessage(1);
                            } else if (barcodeSparseArray.size() == 1) {
                                Log.i("Contador", "Um Código de Barras Encontrado");
                                Message message = handler.obtainMessage();
                                message.what = 2;
                                message.obj = barcodeSparseArray;
                                sendMessage(message);
                            } else {
                                Log.i("Contador", "Vários Códigos de Barras Encontrados. Nº: " + barcodeSparseArray.size());
                                Intent intent = new Intent(view.getContext(), MultiploCodigoBarraLido.class);
                                Bundle bundle = new Bundle();
                                bundle.putSparseParcelableArray("barcodes", barcodeSparseArray);
                                bundle.putString("loja", contagemModel.getLoja().getCnpj());
                                bundle.putString("data", contagemModel.getDataParaSQLite());
                                intent.putExtras(bundle);
                                view.getContext().startActivity(intent);
                            }
                        } else {
                            sendEmptyMessage(1);
                        }
                        break;
                    case 2:
                        SparseArray<Barcode> sparseArray = (SparseArray<Barcode>) msg.obj;

                        Barcode barcode = sparseArray.valueAt(0);

                        Log.i("Contador", "Código Lido: " + barcode.rawValue);

                        Message message = handler.obtainMessage();
                        message.what = 3;
                        message.obj = barcode.rawValue;

                        sendMessage(message);

                        break;
                    case 3:
                        String codigo = (String) msg.obj;
                        ArrayList<Produto> produtos = controller.pesquisarProduto(codigo);

                        if (produtos.size() == 0) {
                            view.abrirProdutoNaoEncontradoDialog(codigo);
                        } else if (produtos.size() == 1) {
                            controller.carregaProduto(produtos.get(0));
                            controller.cadastrar();
                            view.playBarcodeBeep();
                        } else {
                            contagemProdutoDialogArrayAdapter.clear();
                            contagemProdutoDialogArrayAdapter.addAll(produtos);
                            contagemProdutoDialogArrayAdapter.notifyDataSetChanged();
                            view.abrirTelaEscolhaProdutoDialog(contagemProdutoDialogArrayAdapter);
                        }

                        sendEmptyMessageDelayed(1, 2000);

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

    public void setView(ITelaLerCodigoDeBarra view) {
        this.view = view;
        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(view.getContext(), R.layout.item_contagem_produto_dialog, new ArrayList<Produto>());
    }

    public void setContagem(Contagem contagemModel) {
        this.contagemModel = contagemModel;
    }
}
