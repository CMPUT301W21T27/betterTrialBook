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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.QRDAL;
import com.example.bettertrialbook.models.QRCode;
import com.example.bettertrialbook.models.Trial;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.UUID;
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
    private QRCode qrCode;
    private Trial trialToCopy;
    private Button addTrialButton;


    //https://developer.android.com/training/camerax/preview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        handlePermission();
        setupCamera();
        addTrialButton = findViewById(R.id.add_scanned_trial);
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

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, cameraPreview(), barcodeAnalyzer());
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
                                barcode -> scanQR(barcode.getRawValue())
                        )
                );
        return imageAnalysis;
    }

    private void scanQR(String qrId) {
        new QRDAL().addQRCodeListener(qrId, qrCode -> {
            new ExperimentDAL().addTrialListener(qrCode.getExperimentId(), trials -> {
                for (Trial trial : trials) {
                    if (trial.getTrialID() == qrCode.getTrialId()) {
                        setTrialToCopy(trial);
                        break;
                    }
                }
            });
        }, null);
    }

    private void setTrialToCopy(Trial t) {
        addTrialButton.setClickable(true);
        trialToCopy = t;
        findViewById(R.id.camera_container).setVisibility(View.INVISIBLE);
        t.setExperimenterID(You.getUser().getID());
        Toast toast = new Toast(this);
        toast.setText("QR code recognized");
        toast.show();
    }

    public void addScannedTrial(View v){
        if (trialToCopy == null)
            return;

        trialToCopy.setTrialID(UUID.randomUUID().toString());
        new ExperimentDAL().addTrial(qrCode.getExperimentId(), trialToCopy);
    }

}