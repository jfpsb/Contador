package com.vandamodaintima.jfpsb.contador.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;
import java.util.Collections;

public class CameraHandler {
    private Activity activity;
    private WeakReference<TextureView> textureView;
    private String cameraId;
    private Size previewSize;
    private CameraDevice.StateCallback cameraStateCallback;
    private TextureView.SurfaceTextureListener surfaceTextureListener;
    private CameraManager cameraManager;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private CaptureRequest captureRequest;
    private Handler cameraBackgroundHandler;
    private HandlerThread cameraBackgroundThread;

    private int PERMISSAO_CAMERA;

    public CameraHandler(Activity activity, TextureView textureView, int PERMISSAO_CAMERA) {
        this.activity = activity;
        this.PERMISSAO_CAMERA = PERMISSAO_CAMERA;
        this.textureView = new WeakReference<>(textureView);

        cameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);

        cameraStateCallback = new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                cameraDevice = camera;
                createPreviewSession();
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {
                cameraDevice = camera;
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

        this.textureView.get().setSurfaceTextureListener(surfaceTextureListener);
    }

    public void openBackgroundThread() {
        if (cameraBackgroundThread == null && cameraBackgroundHandler == null) {
            cameraBackgroundThread = new HandlerThread("camera_background_thread");
            cameraBackgroundThread.start();
            cameraBackgroundHandler = new Handler(cameraBackgroundThread.getLooper());
        }
    }

    public void setUpCamera() {
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

    public void openCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(activity.getApplicationContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, PERMISSAO_CAMERA);
            } else {
                cameraManager.openCamera(cameraId, cameraStateCallback, cameraBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            Log.e(ActivityBaseView.LOG, e.getMessage(), e);
        }
    }

    public void closeCamera() {
        if (cameraCaptureSession != null) {
            cameraCaptureSession.close();
            cameraCaptureSession = null;
        }

        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    public void closeBackgroudThread() {
        if (cameraBackgroundHandler != null) {
            cameraBackgroundThread.quitSafely();
            cameraBackgroundThread = null;
            cameraBackgroundHandler = null;
        }
    }

    private void createPreviewSession() {
        try {
            SurfaceTexture surfaceTexture = textureView.get().getSurfaceTexture();
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
                                CameraHandler.this.cameraCaptureSession = cameraCaptureSession;
                                CameraHandler.this.cameraCaptureSession.setRepeatingRequest(captureRequest, null, cameraBackgroundHandler);
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

    public TextureView getTextureView() {
        return textureView.get();
    }

    public void setTextureView(TextureView textureView) {
        this.textureView = new WeakReference<>(textureView);
    }

    public String getCameraId() {
        return cameraId;
    }

    public void setCameraId(String cameraId) {
        this.cameraId = cameraId;
    }

    public Size getPreviewSize() {
        return previewSize;
    }

    public void setPreviewSize(Size previewSize) {
        this.previewSize = previewSize;
    }

    public CameraDevice.StateCallback getCameraStateCallback() {
        return cameraStateCallback;
    }

    public void setCameraStateCallback(CameraDevice.StateCallback cameraStateCallback) {
        this.cameraStateCallback = cameraStateCallback;
    }

    public TextureView.SurfaceTextureListener getSurfaceTextureListener() {
        return surfaceTextureListener;
    }

    public void setSurfaceTextureListener(TextureView.SurfaceTextureListener surfaceTextureListener) {
        this.surfaceTextureListener = surfaceTextureListener;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void setCameraManager(CameraManager cameraManager) {
        this.cameraManager = cameraManager;
    }

    public CameraDevice getCameraDevice() {
        return cameraDevice;
    }

    public void setCameraDevice(CameraDevice cameraDevice) {
        this.cameraDevice = cameraDevice;
    }

    public CameraCaptureSession getCameraCaptureSession() {
        return cameraCaptureSession;
    }

    public void setCameraCaptureSession(CameraCaptureSession cameraCaptureSession) {
        this.cameraCaptureSession = cameraCaptureSession;
    }

    public CaptureRequest.Builder getCaptureRequestBuilder() {
        return captureRequestBuilder;
    }

    public void setCaptureRequestBuilder(CaptureRequest.Builder captureRequestBuilder) {
        this.captureRequestBuilder = captureRequestBuilder;
    }

    public CaptureRequest getCaptureRequest() {
        return captureRequest;
    }

    public void setCaptureRequest(CaptureRequest captureRequest) {
        this.captureRequest = captureRequest;
    }

    public Handler getCameraBackgroundHandler() {
        return cameraBackgroundHandler;
    }

    public void setCameraBackgroundHandler(Handler cameraBackgroundHandler) {
        this.cameraBackgroundHandler = cameraBackgroundHandler;
    }

    public HandlerThread getCameraBackgroundThread() {
        return cameraBackgroundThread;
    }

    public void setCameraBackgroundThread(HandlerThread cameraBackgroundThread) {
        this.cameraBackgroundThread = cameraBackgroundThread;
    }
}
