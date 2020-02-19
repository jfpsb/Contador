package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.ContagemProdutoDialogArrayAdapter;
import com.vandamodaintima.jfpsb.contador.controller.contagem.TelaLerCodigoDeBarraController;
import com.vandamodaintima.jfpsb.contador.model.Contagem;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.util.ArrayList;

public class TextureViewCodBarraLido extends TextureView {
    private BarcodeDetector barcodeDetector;
    private TelaLerCodigoDeBarraController controller;
    private ITelaLerCodigoDeBarra view;
    private Contagem contagemModel;
    private ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter;
    private boolean isCampoQuantChecked = false;

    public TextureViewCodBarraLido(Context context) {
        super(context);
        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(context, R.layout.item_contagem_produto_dialog, new ArrayList<Produto>());
    }

    public TextureViewCodBarraLido(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        contagemProdutoDialogArrayAdapter = new ContagemProdutoDialogArrayAdapter(context, R.layout.item_contagem_produto_dialog, new ArrayList<Produto>());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
        }

        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();

        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        getBitmap(bitmap);
        Frame frame = new Frame.Builder().setBitmap(bitmap).build();

        SparseArray<Barcode> barcodeSparseArray = barcodeDetector.detect(frame);

        if (barcodeSparseArray.size() == 0) {
            Toast.makeText(getContext(), "Nenhum Código Foi Encontrado", Toast.LENGTH_SHORT).show();
        } else if (barcodeSparseArray.size() == 1) {
            Barcode barcode = barcodeSparseArray.valueAt(0);
            String codigo = barcode.rawValue;

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
                }
            } else {
                contagemProdutoDialogArrayAdapter.clear();
                contagemProdutoDialogArrayAdapter.addAll(produtos);
                contagemProdutoDialogArrayAdapter.notifyDataSetChanged();
                view.abrirTelaEscolhaProdutoDialog(contagemProdutoDialogArrayAdapter, codigo);
            }
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

        return true;
    }

    public BarcodeDetector getBarcodeDetector() {
        return barcodeDetector;
    }

    public void setBarcodeDetector(BarcodeDetector barcodeDetector) {
        this.barcodeDetector = barcodeDetector;
    }

    public TelaLerCodigoDeBarraController getController() {
        return controller;
    }

    public void setController(TelaLerCodigoDeBarraController controller) {
        this.controller = controller;
        contagemModel = controller.getContagemManager();
    }

    public ITelaLerCodigoDeBarra getView() {
        return view;
    }

    public void setView(ITelaLerCodigoDeBarra view) {
        this.view = view;
    }

    public ContagemProdutoDialogArrayAdapter getContagemProdutoDialogArrayAdapter() {
        return contagemProdutoDialogArrayAdapter;
    }

    public void setContagemProdutoDialogArrayAdapter(ContagemProdutoDialogArrayAdapter contagemProdutoDialogArrayAdapter) {
        this.contagemProdutoDialogArrayAdapter = contagemProdutoDialogArrayAdapter;
    }

    public boolean isCampoQuantChecked() {
        return isCampoQuantChecked;
    }

    public void setCampoQuantChecked(boolean campoQuantChecked) {
        isCampoQuantChecked = campoQuantChecked;
    }

    private void showAlertaQuantidadeProduto() {
        LayoutInflater layoutInflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.fragment_quantidade_produto_contagem_dialog, null, false);

        final EditText txtQuantidade = v.findViewById(R.id.txtQuantidade);

        AlertDialog.Builder alertaQuantidadeProduto = new AlertDialog.Builder(v.getContext());
        alertaQuantidadeProduto.setView(v);
        alertaQuantidadeProduto.setTitle("Informe a Quantidade");

        alertaQuantidadeProduto.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String txtQuant = txtQuantidade.getText().toString();

            if (!txtQuant.isEmpty()) {
                int quantidade = Integer.parseInt(txtQuant);

                if (quantidade < 1) {
                    view.mensagemAoUsuario("Informe Uma Quantidade Válida");
                    return;
                }

                controller.cadastrar(quantidade);
                view.playBarcodeBeep();
            }
        });

        alertaQuantidadeProduto.setNegativeButton("Cancelar", (dialogInterface, i) -> view.mensagemAoUsuario("A Quantidade Não Foi Informada. A Contagem Não Foi Adicionada"));

        alertaQuantidadeProduto.show();
    }
}
