package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.Lifecycle;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.InserirContagemProdutoController;
import com.vandamodaintima.jfpsb.contador.model.ProdutoGrade;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;

import java.lang.ref.WeakReference;
import java.util.List;

public class BarcodeHandlerThreadContagemProduto extends HandlerThread {
    private Handler handler;

    private final WeakReference<TextureView> textureView;
    private final BarcodeDetector barcodeDetector;
    private final InserirContagemProdutoController controller;
    private final TelaLerCodigoDeBarraContagemProduto view;
    private boolean isCampoQuantChecked = false;

    public BarcodeHandlerThreadContagemProduto(TelaLerCodigoDeBarraContagemProduto view, TextureView textureView, InserirContagemProdutoController controller) {
        super("BarcodeHandlerThreadContagemProduto");

        barcodeDetector = new BarcodeDetector.Builder(view.getContext()).build();
        this.view = view;
        this.textureView = new WeakReference<>(textureView);
        this.controller = controller;
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        handler = new Handler(getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        if (view.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.RESUMED) && barcodeDetector.isOperational() && textureView.get().isAvailable()) {
                            Bitmap bitmap = Bitmap.createBitmap(textureView.get().getWidth(), textureView.get().getHeight(), Bitmap.Config.ARGB_8888);
                            textureView.get().getBitmap(bitmap);
                            Frame frame = new Frame.Builder().setBitmap(bitmap).build();

                            SparseArray<Barcode> barcodeSparseArray = barcodeDetector.detect(frame);

                            if (barcodeSparseArray.size() == 0) {
                                sendEmptyMessageDelayed(1, 200);
                            } else if (barcodeSparseArray.size() == 1) {
                                Log.i(ActivityBaseView.LOG, "Um Código de Barras Encontrado");
                                Message message = handler.obtainMessage();
                                message.what = 2;
                                message.obj = barcodeSparseArray;
                                sendMessage(message);
                            } else {
                                view.mensagemAoUsuario("Mais De Um Código Lido. Leia Somente Um Código De Cada Vez");
                                Log.i(ActivityBaseView.LOG, "Vários Códigos de Barras Encontrados. Quant.: " + barcodeSparseArray.size());
                                sendEmptyMessageDelayed(1, 500);
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

                        List<ProdutoGrade> produtoGrades = controller.pesquisarProdutoGrade(codigo);

                        if (produtoGrades.size() == 0) {
                            //TODO: Criar tela de pesquisa/cadastro de produto que mostre as grades do produto ao selecionar na lista. Lista deve ter opção de adicionar grade se não existir no produto e opção "sem grade"
                            Toast.makeText(view.getContext(), "Produto Não Encontrado. Cadastre Na Tela de Produto e Retorne", Toast.LENGTH_SHORT).show();
                            break;
                            //view.abrirProdutoNaoEncontradoDialog(codigo);
                        } else if (produtoGrades.size() == 1) {
                            controller.carregaProdutoGrade(produtoGrades.get(0));

                            if (isCampoQuantChecked) {
                                showAlertaQuantidadeProduto();
                            } else {
                                controller.cadastrar();
                                view.playBarcodeBeep();
                                sendEmptyMessageDelayed(1, 1200);
                            }
                        } else {
                            view.mensagemAoUsuario("Mais De Uma Grade Foi Encontrada. Selecione Na Lista");
                            view.abrirVisualizarProdutoGradeContagem(codigo);
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