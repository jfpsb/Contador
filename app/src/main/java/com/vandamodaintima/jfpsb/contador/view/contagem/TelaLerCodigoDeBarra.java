package com.vandamodaintima.jfpsb.contador.view.contagem;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
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
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.vandamodaintima.jfpsb.contador.R;
import com.vandamodaintima.jfpsb.contador.controller.contagem.BarcodeHandlerThread;
import com.vandamodaintima.jfpsb.contador.controller.contagem.TelaLerCodigoDeBarraController;
import com.vandamodaintima.jfpsb.contador.view.TabLayoutBaseView;
import com.vandamodaintima.jfpsb.contador.view.interfaces.IAdicionarContagemProduto;
import com.vandamodaintima.jfpsb.contador.view.interfaces.ITelaLerCodigoDeBarra;

import java.util.Collections;

public class TelaLerCodigoDeBarra extends Fragment implements ITelaLerCodigoDeBarra {
    private TextureView textureView;
    private Button btnInserirManualmente;
    private String cameraId;
    private Size previewSize;
    private CameraDevice.StateCallback stateCallback;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;

    private Handler cameraBackgroundHandler;
    private HandlerThread cameraBackgroundThread;

    private BarcodeHandlerThread barcodeHandlerThread;

    private BarcodeDetector barcodeDetector;

    private AlertDialog.Builder escolhaProdutoDialog;
    private AlertDialog.Builder produtoNaoEncontradoDialog;
    private MediaPlayer erroMediaPlayer;
    private MediaPlayer codigoLidoMediaPlayer;

    private IAdicionarContagemProduto owner;
    private String codigo_pesquisado;

    private TelaLerCodigoDeBarraController controller;

    private static final int PERMISSAO_CAMERA = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ler_codigo_de_barra, container, false);

        btnInserirManualmente = view.findViewById(R.id.btnInserirManualemente);
        owner = (IAdicionarContagemProduto) getActivity();
        textureView = view.findViewById(R.id.textureView);
        barcodeDetector = new BarcodeDetector.Builder(getContext()).build();
        erroMediaPlayer = MediaPlayer.create(getContext(), R.raw.erro_buzzer);
        codigoLidoMediaPlayer = MediaPlayer.create(getContext(), R.raw.barcode_beep);

        controller = new TelaLerCodigoDeBarraController(this, owner.getConexaoBanco());

        Bundle bundle = getArguments();
        String loja = bundle.getString("loja");
        String data = bundle.getString("data");
        controller.carregaContagem(loja, data);

        stateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                TelaLerCodigoDeBarra.this.cameraDevice = camera;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                camera.close();
                TelaLerCodigoDeBarra.this.cameraDevice = null;
            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {
                onDisconnected(camera);
            }
        };

        cameraManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);

        surfaceTextureListener = new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                setUpCamera();
                openCamera();
                //Inicia fila de mensagens de thread que detecta códigos da câmera
                barcodeHandlerThread.getHandler().removeCallbacksAndMessages(null);
                barcodeHandlerThread.getHandler().sendEmptyMessage(1);
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

        ((TabLayoutBaseView) getActivity()).getTabLayout().addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //Guarda se a fragment que contém a textureview está visível ou não
                if (tab.getPosition() == 0) {
                    barcodeHandlerThread.setTextureViewVisible(true);
                } else {
                    barcodeHandlerThread.setTextureViewVisible(false);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        setEscolhaProdutoDialog();
        setProdutoNaoEncontradoDialog();

        openBackgroundThread();
        iniciaBarcodeThread();

        return view;
    }

    private void openBackgroundThread() {
        cameraBackgroundThread = new HandlerThread("camera_background_thread");
        cameraBackgroundThread.start();
        cameraBackgroundHandler = new Handler(cameraBackgroundThread.getLooper());
    }

    private void iniciaBarcodeThread() {
        barcodeHandlerThread = new BarcodeHandlerThread();
        barcodeHandlerThread.setBarcodeDetector(barcodeDetector);
        barcodeHandlerThread.setTextureView(textureView);
        barcodeHandlerThread.setController(controller);
        barcodeHandlerThread.setView(this);
        barcodeHandlerThread.setContagem(controller.getContagemManager());
        barcodeHandlerThread.start();
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

    public void abrirTelaEscolhaProdutoDialog(ListAdapter adapter) {
        erroMediaPlayer.start();
        escolhaProdutoDialog.setSingleChoiceItems(adapter, 0, null);
        escolhaProdutoDialog.show();
    }

    @Override
    public void realizarPesquisa() {
        //Realiza a pesquisa na thread principal (UI thread)
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                TabLayoutBaseView tabLayoutBaseView = (TabLayoutBaseView) getActivity();
                TelaVerProdutoContado telaVerProdutoContado = (TelaVerProdutoContado) tabLayoutBaseView.getPagerAdapter().getItem(1);
                telaVerProdutoContado.realizarPesquisa();
            }
        });
    }

    private void setEscolhaProdutoDialog() {
        escolhaProdutoDialog = new AlertDialog.Builder(getContext());
        escolhaProdutoDialog.setTitle("Selecione Abaixo");

        escolhaProdutoDialog.setNegativeButton("O Produto Não Está Lista", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                owner.abrirTelaProdutoForResult(codigo_pesquisado);
            }
        });

        escolhaProdutoDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ListView lw = ((AlertDialog) dialogInterface).getListView();
                Object model = lw.getAdapter().getItem(lw.getCheckedItemPosition());
                controller.carregaProduto(model);
                controller.cadastrar();
            }
        });

        escolhaProdutoDialog.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
            }
        });
    }

    private void setProdutoNaoEncontradoDialog() {
        produtoNaoEncontradoDialog = new AlertDialog.Builder(getContext());
        produtoNaoEncontradoDialog.setTitle("Produto Não Encontrado");
        produtoNaoEncontradoDialog.setMessage("Nenhum Produto Foi Encontrado Com o Código Informado. Deseja Abrir a Tela de Pesquisa/Cadastro de Produtos?");

        produtoNaoEncontradoDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                owner.abrirTelaProdutoForResult(codigo_pesquisado);
            }
        });

        produtoNaoEncontradoDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mensagemAoUsuario("Nenhuma Contagem De Produto Foi Adicionada");
                dialogInterface.dismiss();
            }
        });
    }

    private void setUpCamera() {
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);

                if (cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_BACK) {
                    StreamConfigurationMap streamConfigurationMap = cameraCharacteristics.get(
                            CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
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
                cameraManager.openCamera(cameraId, stateCallback, cameraBackgroundHandler);
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
        openBackgroundThread();
        iniciaBarcodeThread();

        if (textureView.isAvailable()) {
            setUpCamera();
            openCamera();
            barcodeHandlerThread.getHandler().removeCallbacksAndMessages(null);
            barcodeHandlerThread.getHandler().sendEmptyMessageDelayed(1, 2000);
            realizarPesquisa();
        } else {
            textureView.setSurfaceTextureListener(surfaceTextureListener);
        }
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

        if (barcodeHandlerThread != null) {
            barcodeHandlerThread.quit();
            barcodeHandlerThread = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        closeCamera();
        closeBackgroudThread();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeCamera();
        closeBackgroudThread();
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
                        public void onConfigured(CameraCaptureSession cameraCaptureSession) {
                            if (cameraDevice == null) {
                                return;
                            }

                            try {
                                captureRequest = captureRequestBuilder.build();
                                TelaLerCodigoDeBarra.this.cameraCaptureSession = cameraCaptureSession;
                                TelaLerCodigoDeBarra.this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, cameraBackgroundHandler);
                            } catch (CameraAccessException e) {
                                Log.e("Contador", e.getMessage(), e);
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession cameraCaptureSession) {

                        }
                    }, cameraBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
}