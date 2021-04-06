package com.example.bettertrialbook.qr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.bettertrialbook.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Uses device camera to scan qr/bar codes.
 * A preview of the camera's output is displayed on the screen.
 */
public class ScanQRActivity extends AppCompatActivity {
    private static final String tag = "QR Scanner";
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d(tag, "Permission granted");
                setupCamera();
            });

    private Camera camera;

    //https://developer.android.com/training/camerax/preview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        handlePermission();
        setupCamera();
    }

    private void handlePermission() {
//        https://developer.android.com/training/permissions/requesting
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_GRANTED)
            return;
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }


    private void setupCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        Executor cameraExecutor = ContextCompat.getMainExecutor(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bind(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }

        }, cameraExecutor);
    }

    void bind(@NonNull ProcessCameraProvider cameraProvider) {
        Log.d(tag, "binding camera");
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, cameraPreview(), barcodeAnalyzer());
    }

    private Preview cameraPreview() {
        PreviewView previewView = findViewById(R.id.camera_preview);
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        return preview;
    }

    private ImageAnalysis barcodeAnalyzer() {
        //    https://developers.google.com/ml-kit/vision/barcode-scanning/android#java
        ImageAnalysis imageAnalysis = new ImageAnalysis
                .Builder()
                .build();
        imageAnalysis
                .setAnalyzer(
                        ContextCompat.getMainExecutor(this),
                        new BarcodeAnalyzer(
                                barcode -> Log.d(tag, "Scanned the following barcode: " + barcode.getRawValue())
                        )
                );
        return imageAnalysis;
    }

}