package com.example.bettertrialbook.qr;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.bettertrialbook.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

public class ScanQRActivity extends AppCompatActivity {
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d("QR", "Permission granted");
                setupCamera();
            });

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
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }
        }, ContextCompat.getMainExecutor(this));
    }

    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Log.d("QR", "binding camera");
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        PreviewView previewView = findViewById(R.id.camera_preview);
        preview.setSurfaceProvider(previewView.createSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, preview);
    }
}