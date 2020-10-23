package com.vandamodaintima.jfpsb.contador.view.produto.grade;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.TextureView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.CameraHandler;

public class TelaLerCodigoBarrasCadastrarProduto extends ActivityBaseView {
    private TextureView textureView;
    private BarcodeHandlerThreadCadastroProduto barcodeHandlerThread;
    private BarcodeDetector barcodeDetector;
    private CameraHandler cameraHandler;

    private static final int PERMISSAO_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stub.setLayoutResource(R.layout.activity_ler_codigo_de_barra_cadastro_produto);
        stub.inflate();

        textureView = findViewById(R.id.textureView);
        barcodeDetector = new BarcodeDetector.Builder(getApplicationContext()).build();
        cameraHandler = new CameraHandler(this, textureView, PERMISSAO_CAMERA);
        iniciaBarcodeHandlerThread();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permissão Concedida para Câmera", Toast.LENGTH_SHORT).show();
                cameraHandler.openCamera();
            }
        }
    }

    @Override
    public void onResume() {
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
            barcodeHandlerThread = new BarcodeHandlerThreadCadastroProduto(this, cameraHandler.getTextureView(), barcodeDetector);
            barcodeHandlerThread.start();
        }
    }
}
