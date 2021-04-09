package com.example.bettertrialbook.qr;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.Callback;
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
    private QRCode scannedQRCode;
    private Trial trialToCopy;
    private Button addTrialButton;


    // https://developer.android.com/training/camerax/preview
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        handlePermission();
        setupCamera();
        addTrialButton = findViewById(R.id.add_scanned_trial);
    }

    /**
     * Ensures device permissions are obtained
     */
    private void handlePermission() {
        // https://developer.android.com/training/permissions/requesting
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permission == PackageManager.PERMISSION_GRANTED)
            return;
        requestPermissionLauncher.launch(Manifest.permission.CAMERA);
    }

    /**
     * Adds listener to camera to ensure functionality
     */
    private void setupCamera() {
        addCameraProviderListener(processCameraProvider -> bind(processCameraProvider));
    }

    /**
     * Removes listener from camera
     */
    private void tearDownCamera() {
        addCameraProviderListener(processCameraProvider -> processCameraProvider.unbindAll());
    }
    /**
     * Gets Camera provider and performs the specified callback
     */
    private void addCameraProviderListener(Callback<ProcessCameraProvider> callback) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        Executor cameraExecutor = ContextCompat.getMainExecutor(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                callback.execute(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {
                // No errors need to be handled for this Future.
                // This should never be reached.
            }

        }, cameraExecutor);
    }

    /**
     * Binds the camera and sets it up
     * @param cameraProvider
     */
    void bind(@NonNull ProcessCameraProvider cameraProvider) {
        Log.d(tag, "binding camera");
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner) this, cameraSelector, cameraPreview(), barcodeAnalyzer());
    }

    /**
     * Temporary preview of the camera
     * @return Preview
     */
    private Preview cameraPreview() {
        PreviewView previewView = findViewById(R.id.camera_preview);
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(previewView.createSurfaceProvider());
        return preview;
    }

    /**
     * Analyzes the scanned barcode
     * @return ImageAnalysis
     */
    private ImageAnalysis barcodeAnalyzer() {
        // https://developers.google.com/ml-kit/vision/barcode-scanning/android#java
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

    /**
     * Scans a QR code based on its ID
     * @param qrId
     */
    private void scanQR(String qrId) {
        new QRDAL().addQRCodeListener(qrId, qrCode -> {
            scannedQRCode = qrCode;
            new ExperimentDAL().addTrialListener(qrCode.getExperimentId(), trials -> {
                for (Trial trial : trials) {
                    Log.d("QR", "QRCode: " + qrCode.toString() + "\n Trial: " + trial.getTrialID());
                    if (trial.getTrialID().equals(qrCode.getTrialId())) {
                        setTrialToCopy(trial);
                        tearDownCamera();
                        break;
                    }
                }
            });
        }, null);
    }

    /**
     * called once we find a trial that matches the given qr code.
     * @param t
     */
    private void foundTrial(Trial t){

    }

    /**
     * Lets the trial button be clickable and copies it
     * @param t
     */
    private void setTrialToCopy(Trial t) {
        addTrialButton.setClickable(true);
        trialToCopy = t;
        findViewById(R.id.camera_container).setVisibility(View.INVISIBLE);
        t.setExperimenterID(You.getUser().getID());
        makeToast("QR code recognized");
    }

    /**
     * Adds scanned trial to experiment and database
     * @param v
     */
    public void addScannedTrial(View v) {
        if (trialToCopy == null)
            return;

        trialToCopy.setTrialID(UUID.randomUUID().toString());
        new ExperimentDAL().addTrial(scannedQRCode.getExperimentId(), trialToCopy);
        makeToast("Added trial");
    }

    /**
     * Allows for repeated toast messages
     * @param msg
     */
    private void makeToast(String msg) {
        Toast toast = Toast.makeText(this,msg, Toast.LENGTH_SHORT);
        toast.show();
    }
}
