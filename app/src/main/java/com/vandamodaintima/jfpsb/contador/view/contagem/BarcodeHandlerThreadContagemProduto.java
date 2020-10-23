package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.ContagemProdutoDialogArrayAdapter;
import com.vandamodaintima.jfpsb.contador.controller.contagem.TelaLerCodigoDeBarraController;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class BarcodeHandlerThreadContagemProduto extends HandlerThread {
    private Handler handler;

    private WeakReference<TextureView> textureView;
    private BarcodeDetector barcodeDetector;
    private TelaLerCodigoDeBarraController controller;
    private ITelaLerCodigoDeBarra view;
    private Contagem contagemModel;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;
    private boolean isCampoQuantChecked = false;

    public BarcodeHandlerThreadContagemProduto(ITelaLerCodigoDeBarra view, TextureView textureView, TelaLerCodigoDeBarraController controller, BarcodeDetector barcodeDetector) {
        super("BarcodeHandlerThreadContagemProduto");

        this.view = view;
        this.textureView = new WeakReference<>(textureView);
        this.controller = controller;
        this.barcodeDetector = barcodeDetector;
        contagemModel = controller.getContagem();

        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(view.getContext(), R.layout.item_contagem_produto_dialog, new ArrayList<>());
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (view.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED) && barcodeDetector.isOperational()) {
                            Bitmap bitmap = Bitmap.createBitmap(textureView.get().getWidth(), textureView.get().getHeight(), Bitmap.Config.ARGB_8888);
                            textureView.get().getBitmap(bitmap);
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                            SparseArray<Barcode> barcodeSparseArray = barcodeDetector.detect(frame);

                            if (barcodeSparseArray.size() == 0) {
                                sendEmptyMessageDelayed(1, 250);
                            } else if (barcodeSparseArray.size() == 1) {
                                Log.i(ActivityBaseView.LOG, "Um Código de Barras Encontrado");
                                Message message = handler.obtainMessage();
                                message.what = 2;
                                message.obj = barcodeSparseArray;
                                sendMessage(message);
                            } else {
                                Log.i(ActivityBaseView.LOG, "Vários Códigos de Barras Encontrados. Quant.: " + barcodeSparseArray.size());
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
                        removeCallbacksAndMessages(null);

                        SparseArray<Barcode> sparseArray = (SparseArray<Barcode>) msg.obj;
                        Barcode barcode = sparseArray.valueAt(0);
                        String codigo = barcode.rawValue;

                        Log.i("Contador", "Código Lido: " + barcode.rawValue);

                        ArrayList<Produto> produtos = controller.pesquisarProduto(codigo);

                        if (produtos.size() == 0) {
                            view.abrirProdutoNaoEncontradoDialog(codigo);
                        } else if (produtos.size() == 1) {
                            controller.carregaProduto(produtos.get(0));

                            if (isCampoQuantChecked) {
                                showAlertaQuantidadeProduto();
                            } else {
                                controller.cadastrar();
                                view.playBarcodeBeep();
                                sendEmptyMessageDelayed(1, 1500);
                            }
                        } else {
                            contagemProdutoDialogArrayAdapter.clear();
                            contagemProdutoDialogArrayAdapter.addAll(produtos);
                            contagemProdutoDialogArrayAdapter.notifyDataSetChanged();
                            view.abrirTelaEscolhaProdutoDialog(contagemProdutoDialogArrayAdapter, codigo);
                        }

                        break;
                }
            }
        };

        getHandler().sendEmptyMessage(1);
    }

    @Override
    public synchronized void start() {
        super.start();
    }

    private void showAlertaQuantidadeProduto() {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.fragment_quantidade_produto_contagem_dialog, null, false);

        final EditText txtQuantidade = v.findViewById(R.id.txtQuantidade);
        txtQuantidade.setSelectAllOnFocus(true);
        txtQuantidade.requestFocus();

        AlertDialog.Builder alertaQuantidadeProduto = new AlertDialog.Builder(v.getContext());
        alertaQuantidadeProduto.setView(v);
        alertaQuantidadeProduto.setTitle("Informe a Quantidade");

        alertaQuantidadeProduto.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String txtQuant = txtQuantidade.getText().toString();
            int quantidade = 1;

            if (!txtQuant.isEmpty()) {
                quantidade = Integer.parseInt(txtQuant);
                if (quantidade < 1) {
                    view.mensagemAoUsuario("Informe Uma Quantidade Válida");
                    return;
                }
            }

            controller.cadastrar(quantidade);
            view.playBarcodeBeep();
            getHandler().sendEmptyMessageDelayed(1, 1500);
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", (dialogInterface, i) -> {
            view.mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada");
            getHandler().sendEmptyMessageDelayed(1, 1500);
        });

        AlertDialog dialog = alertaQuantidadeProduto.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    public Handler getHandler() {
        return handler;
    }

    public void setCampoQuantChecked(boolean campoQuantChecked) {
        isCampoQuantChecked = campoQuantChecked;
    }
}