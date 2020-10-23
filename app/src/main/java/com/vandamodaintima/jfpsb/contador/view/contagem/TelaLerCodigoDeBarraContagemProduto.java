package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.TelaLerCodigoDeBarraController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.CameraHandler;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;

public class TelaLerCodigoDeBarraContagemProduto extends Fragment implements ITelaLerCodigoDeBarra {
    private TextureView textureView;
    private BarcodeHandlerThreadContagemProduto barcodeHandlerThread;
    private BarcodeDetector barcodeDetector;
    private AlertDialog.Builder escolhaProdutoDialog;
    private AlertDialog.Builder produtoNaoEncontradoDialog;
    private MediaPlayer erroMediaPlayer;
    private MediaPlayer codigoLidoMediaPlayer;
    private String codigo_lido;
    private TelaLerCodigoDeBarraController controller;
    private CameraHandler cameraHandler;

    private boolean isCampoQuantidadeMarcado;

    private static final int PERMISSAO_CAMERA = 1;
    private static final int TELA_SELECIONAR_PRODUTO = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ler_codigo_de_barra_contagem_produto, container, false);

        Button btnInserirManualmente = view.findViewById(R.id.btnInserirManualmente);
        IAdicionarContagemProduto ownerActivity = (IAdicionarContagemProduto) getActivity();
        textureView = view.findViewById(R.id.textureView);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).build();
        erroMediaPlayer = MediaPlayer.create(getContext(), R.raw.erro_buzzer);
        codigoLidoMediaPlayer = MediaPlayer.create(getContext(), R.raw.barcode_beep);

        controller = new TelaLerCodigoDeBarraController(this, ownerActivity.getConexaoBanco());

        Bundle bundle = getArguments();
        String loja = bundle.getString("loja");
        String data = bundle.getString("data");
        controller.carregaContagem(loja, data);

        btnInserirManualmente.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), TelaProdutoForContagemForResult.class);
            startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
        });

        cameraHandler = new CameraHandler(getActivity(), textureView, PERMISSAO_CAMERA);
        iniciaBarcodeHandlerThread();

        setEscolhaProdutoDialog();
        setProdutoNaoEncontradoDialog();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TELA_SELECIONAR_PRODUTO) {
            if (resultCode == Activity.RESULT_OK) {
                Produto produto = (Produto) data.getSerializableExtra("produto");
                controller.carregaProduto(produto);
                int quantidade = data.getIntExtra("quantidade", 1);
                controller.cadastrar(quantidade);
            }
        }
    }

    @Override
    public void playBarcodeBeep() {
        codigoLidoMediaPlayer.start();
    }

    @Override
    public void mensagemAoUsuario(String mensagem) {
        Toast.makeText(getContext(), mensagem, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void abrirProdutoNaoEncontradoDialog(String codigo) {
        erroMediaPlayer.start();
        this.codigo_lido = codigo;
        produtoNaoEncontradoDialog.show();
    }

    public void abrirTelaEscolhaProdutoDialog(ListAdapter adapter, String codigo) {
        erroMediaPlayer.start();
        codigo_lido = codigo;
        escolhaProdutoDialog.setSingleChoiceItems(adapter, 0, null);
        escolhaProdutoDialog.show();
    }

    private void setEscolhaProdutoDialog() {
        escolhaProdutoDialog = new AlertDialog.Builder(getContext());
        escolhaProdutoDialog.setTitle("Selecione Abaixo");

        escolhaProdutoDialog.setNegativeButton("O Produto Não Está Lista", (dialogInterface, i) -> {
            Intent intent = new Intent(getContext(), TelaProdutoForContagemForResult.class);
            intent.putExtra("codigo", codigo_lido);
            startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
        });

        escolhaProdutoDialog.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            ListView lw = ((AlertDialog) dialogInterface).getListView();
            Object model = lw.getAdapter().getItem(lw.getCheckedItemPosition());
            controller.carregaProduto(model);
            controller.cadastrar();
            barcodeHandlerThread.getHandler().sendEmptyMessageDelayed(1, 1500);
        });

        escolhaProdutoDialog.setNeutralButton("Cancelar", (dialogInterface, i) -> {
            mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
            barcodeHandlerThread.getHandler().sendEmptyMessageDelayed(1, 1500);
        });
    }

    private void setProdutoNaoEncontradoDialog() {
        produtoNaoEncontradoDialog = new AlertDialog.Builder(getContext());
        produtoNaoEncontradoDialog.setTitle("Produto Não Encontrado");
        produtoNaoEncontradoDialog.setMessage("Nenhum Produto Foi Encontrado Com o Código Informado. Deseja Abrir a Tela de Pesquisa/Cadastro de Produtos?");

        produtoNaoEncontradoDialog.setPositiveButton("Sim", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent intent = new Intent(getContext(), TelaProdutoForContagemForResult.class);
            intent.putExtra("codigo", codigo_lido);
            startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
        });

        produtoNaoEncontradoDialog.setNegativeButton("Não", (dialogInterface, i) -> {
            mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
            dialogInterface.dismiss();
            barcodeHandlerThread.getHandler().sendEmptyMessageDelayed(1, 1000);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permissão Concedida para Câmera", Toast.LENGTH_SHORT).show();
                cameraHandler.openCamera();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        super.onResume();

        if (cameraHandler.getTextureView().isAvailable()) {
            cameraHandler.openBackgroundThread();
            cameraHandler.setUpCamera();
            cameraHandler.openCamera();
            iniciaBarcodeHandlerThread();
        } else {
            cameraHandler.getTextureView().setSurfaceTextureListener(cameraHandler.getSurfaceTextureListener());
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraHandler.closeBackgroudThread();
        cameraHandler.closeCamera();
        barcodeHandlerThread.getLooper().quit();
        barcodeHandlerThread.quit();
        barcodeHandlerThread = null;
    }

    private void iniciaBarcodeHandlerThread() {
        if (barcodeHandlerThread == null) {
            barcodeHandlerThread = new BarcodeHandlerThreadContagemProduto(TelaLerCodigoDeBarraContagemProduto.this, cameraHandler.getTextureView(), controller, barcodeDetector);
            barcodeHandlerThread.start();
            barcodeHandlerThread.setCampoQuantChecked(isCampoQuantidadeMarcado);
        }
    }

    public void setCampoQuantidadeMarcado(boolean campoQuantidadeMarcado) {
        isCampoQuantidadeMarcado = campoQuantidadeMarcado;
        if (barcodeHandlerThread != null)
            barcodeHandlerThread.setCampoQuantChecked(isCampoQuantidadeMarcado);
    }
}