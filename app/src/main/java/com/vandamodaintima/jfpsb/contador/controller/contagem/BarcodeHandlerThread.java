package com.vandamodaintima.jfpsb.contador.controller.contagem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;

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
    private AlertDialog.Builder alertaQuantidadeProduto;

    private WeakReference<TextureView> textureViewWeakReference;
    private BarcodeDetector barcodeDetector;
    private TelaLerCodigoDeBarraController controller;
    private ITelaLerCodigoDeBarra view;
    private Contagem contagemModel;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;
    private boolean isCampoQuantChecked = false;
    private boolean textureViewVisible = true;

    public BarcodeHandlerThread(BarcodeDetector barcodeDetector, TextureView textureView, TelaLerCodigoDeBarraController controller, ITelaLerCodigoDeBarra view, Contagem contagemModel) {
        super("BarcodeHandlerThread");
        this.barcodeDetector = barcodeDetector;
        textureViewWeakReference = new WeakReference<>(textureView);
        this.controller = controller;
        this.view = view;
        this.contagemModel = contagemModel;

        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(view.getContext(), R.layout.item_contagem_produto_dialog, new ArrayList<Produto>());
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

                            if (isCampoQuantChecked) {
                                showAlertaQuantidadeProduto(this);
                            } else {
                                controller.cadastrar();
                                view.playBarcodeBeep();
                                sendEmptyMessageDelayed(1, 1800);
                            }
                        } else {
                            contagemProdutoDialogArrayAdapter.clear();
                            contagemProdutoDialogArrayAdapter.addAll(produtos);
                            contagemProdutoDialogArrayAdapter.notifyDataSetChanged();
                            view.abrirTelaEscolhaProdutoDialog(contagemProdutoDialogArrayAdapter, codigo);
                            sendEmptyMessageDelayed(1, 1800);
                        }
                        break;
                }
            }
        };
    }

    public Handler getHandler() {
        return handler;
    }

    public void setTextureViewVisible(boolean textureViewVisible) {
        this.textureViewVisible = textureViewVisible;
    }

    private void showAlertaQuantidadeProduto(final Handler handler) {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.fragment_quantidade_produto_contagem_dialog, null, false);

        final EditText txtQuantidade = v.findViewById(R.id.txtQuantidade);

        alertaQuantidadeProduto = new AlertDialog.Builder(v.getContext());
        alertaQuantidadeProduto.setView(v);
        alertaQuantidadeProduto.setTitle("Informe a Quantidade");

        alertaQuantidadeProduto.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String txtQuant = txtQuantidade.getText().toString();

                if (!txtQuant.isEmpty()) {
                    int quantidade = Integer.parseInt(txtQuant);

                    if (quantidade < 1) {
                        view.mensagemAoUsuario("Informe Uma Quantidade Válida");
                        return;
                    }

                    controller.cadastrar(quantidade);
                    view.playBarcodeBeep();

                    handler.sendEmptyMessageDelayed(1, 1500);
                }
            }
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                view.mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada");
                handler.sendEmptyMessage(1);
            }
        });

        alertaQuantidadeProduto.show();
    }

    public void setCampoQuantChecked(boolean b) {
        isCampoQuantChecked = b;
    }
}
