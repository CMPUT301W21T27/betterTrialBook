package com.example.bettertrialbook.qr;

import android.Manifest;
import android.content.Intent;
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

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.You;
import com.example.bettertrialbook.dal.Callback;
import com.example.bettertrialbook.dal.ExperimentDAL;
import com.example.bettertrialbook.dal.QRDAL;
import com.example.bettertrialbook.models.QRCode;
import com.example.bettertrialbook.models.Trial;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

/**
 * Uses device camera to scan qr/bar codes.
 * A preview of the camera's output is displayed on the screen.
 * If the return scanned flag is passed in, this activity won't bother finding the qr code in
 * firestore and will just return the first barcode string that has been recognized
 */
public class ScanQRActivity extends AppCompatActivity {
    private static final String tag = "QR Scanner";

    public static final String RETURN_SCANNED_FLAG = "return scanned";
    public static final int SCANNED_OK = 1;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                Log.d(tag, "Permission granted");
                setupCamera();
            });

    private boolean justReturnScanned;

    private QRCode scannedQRCode;
    private Trial trialToCopy;
    private Button addTrialButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        handlePermission();
        setupCamera();

        Bundle extras =getIntent().getExtras();
        justReturnScanned = extras !=null && extras.containsKey(RETURN_SCANNED_FLAG);

        addTrialButton = findViewById(R.id.add_scanned_trial);
        addTrialButton.setVisibility(View.GONE);
    }

    /**
     * Ensures device permissions are obtained
     */
    private void handlePermission() {
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
     *
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
     *
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
     *
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
                                // bar code strings shouldn't have / in them so as not to confuse Firestore
                                barcode -> scanQR(barcode.getRawValue().replace('/', 'a'))
                        )
                );
        return imageAnalysis;
    }

    /**
     * Scans a QR code based on its ID
     *
     * @param qrId
     */
    private void scanQR(String qrId) {
        if (justReturnScanned) {
            Log.d("QR", "QRCode: " + qrId);
            returnBarcodeText(qrId);
            return;
        }
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
        }, () -> {
            makeToast("QR Code has not been registered");
        });
    }

    /**
     * Lets the trial button be clickable and copies it
     *
     * @param t
     */
    private void setTrialToCopy(Trial t) {
        addTrialButton.setVisibility(View.VISIBLE);
        trialToCopy = t;
        findViewById(R.id.camera_container).setVisibility(View.INVISIBLE);
        t.setExperimenterID(You.getUser().getID());
        makeToast("QR code recognized");
    }

    /**
     * Adds scanned trial to experiment and database
     *
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
     *
     * @param msg
     */
    private void makeToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.show();
    }

    private void returnBarcodeText(String qrId) {
        Intent result = new Intent();
        result.putExtra(Extras.QR_CODE_ID, qrId);
        setResult(SCANNED_OK, result);
        finish();
    }
}
