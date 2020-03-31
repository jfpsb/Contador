package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.TelaLerCodigoDeBarraController;
import com.vandamodaintima.jfpsb.contador.model.Produto;
import com.vandamodaintima.jfpsb.contador.view.ActivityBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;
import com.vandamodaintima.jfpsb.contador.view.produto.TelaProdutoForContagemForResult;

import java.util.Collections;

public class TelaLerCodigoDeBarra extends Fragment implements ITelaLerCodigoDeBarra {
    private TextureView textureView;
    private String cameraId;
    private Size previewSize;
    private CameraDevice.StateCallback cameraStateCallback;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;
    private BarcodeHandlerThread barcodeHandlerThread;
    private Handler cameraBackgroundHandler;
    private HandlerThread cameraBackgroundThread;
    private BarcodeDetector barcodeDetector;
    private AlertDialog.Builder escolhaProdutoDialog;
    private AlertDialog.Builder produtoNaoEncontradoDialog;
    private MediaPlayer erroMediaPlayer;
    private MediaPlayer codigoLidoMediaPlayer;
    private String codigo_pesquisado;
    private TelaLerCodigoDeBarraController controller;

    private boolean isCampoQuantidadeMarcado;

    private static final int PERMISSAO_CAMERA = 1;
    private static final int TELA_SELECIONAR_PRODUTO = 2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ler_codigo_de_barra, container, false);

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

        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        cameraStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                TelaLerCodigoDeBarra.this.cameraDevice = camera;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                TelaLerCodigoDeBarra.this.cameraDevice = camera;
                closeCamera();
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                onDisconnected(camera);
            }
        };

        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                openBackgroundThread();
                setUpCamera();
                openCamera();
                iniciaBarcodeHandlerThread();
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        };

        textureView.setSurfaceTextureListener(surfaceTextureListener);

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

    private void openBackgroundThread() {
        if (cameraBackgroundThread == null && cameraBackgroundHandler == null) {
            cameraBackgroundThread = new HandlerThread("camera_background_thread");
            cameraBackgroundThread.start();
            cameraBackgroundHandler = new Handler(cameraBackgroundThread.getLooper());
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
        this.codigo_pesquisado = codigo;
        produtoNaoEncontradoDialog.show();
    }

    public void abrirTelaEscolhaProdutoDialog(ListAdapter adapter, String codigo) {
        erroMediaPlayer.start();
        codigo_pesquisado = codigo;
        escolhaProdutoDialog.setSingleChoiceItems(adapter, 0, null);
        escolhaProdutoDialog.show();
    }

    private void setEscolhaProdutoDialog() {
        escolhaProdutoDialog = new AlertDialog.Builder(getContext());
        escolhaProdutoDialog.setTitle("Selecione Abaixo");

        escolhaProdutoDialog.setNegativeButton("O Produto Não Está Lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getContext(), TelaProdutoForContagemForResult.class);
                intent.putExtra("codigo", codigo_pesquisado);
                startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
            }
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
            intent.putExtra("codigo", codigo_pesquisado);
            startActivityForResult(intent, TELA_SELECIONAR_PRODUTO);
        });

        produtoNaoEncontradoDialog.setNegativeButton("Não", (dialogInterface, i) -> {
            mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
            dialogInterface.dismiss();
            barcodeHandlerThread.getHandler().sendEmptyMessageDelayed(1, 1000);
        });
    }

    private void setUpCamera() {
        if (previewSize != null)
            return;

        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);

                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                    previewSize = streamConfigurationMap.getOutputSizes(SurfaceTexture.class)[0];
                    cameraId = id;
                    break;
                }
            }
        } catch (CameraAccessException e) {
            Log.e("Contador", e.getMessage(), e);
        }
    }

    private void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, PERMISSAO_CAMERA);
            } else {
                cameraManager.openCamera(cameraId, cameraStateCallback, cameraBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            Log.e("Contador", e.getMessage(), e);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_CAMERA) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permissão Concedida para Câmera", Toast.LENGTH_SHORT).show();
                openCamera();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (textureView.isAvailable()) {
            openBackgroundThread();
            setUpCamera();
            openCamera();
            iniciaBarcodeHandlerThread();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        closeBackgroudThread();
        closeCamera();
        barcodeHandlerThread.getLooper().quit();
        barcodeHandlerThread.quit();
        barcodeHandlerThread = null;
        //textureView.setSurfaceTextureListener(null);
    }

    private void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    private void closeBackgroudThread() {
        if (cameraBackgroundHandler != null) {
            cameraBackgroundThread.quitSafely();
            cameraBackgroundThread = null;
            cameraBackgroundHandler = null;
        }
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
            surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
            Surface previewSurface = new Surface(surfaceTexture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                TelaLerCodigoDeBarra.this.cameraCaptureSession = cameraCaptureSession;
                                TelaLerCodigoDeBarra.this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, cameraBackgroundHandler);
                            } catch (CameraAccessException e) {
                                Log.e(ActivityBaseView.LOG, e.getMessage(), e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        }
    }

    private void iniciaBarcodeHandlerThread() {
        if (barcodeHandlerThread == null) {
            barcodeHandlerThread = new BarcodeHandlerThread(TelaLerCodigoDeBarra.this, textureView, controller, barcodeDetector);
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