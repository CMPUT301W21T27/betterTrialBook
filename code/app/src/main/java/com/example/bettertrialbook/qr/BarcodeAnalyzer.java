package com.example.bettertrialbook.qr;

import android.annotation.SuppressLint;
import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;

import com.example.bettertrialbook.dal.Callback;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

//https://developers.google.com/ml-kit/vision/barcode-scanning/android#java

/**
 * Scans images that might contain QR/Barcodes.
 */
public class BarcodeAnalyzer implements ImageAnalysis.Analyzer {
    private static final String tag = "QR";
    Callback<Barcode> barcodeCallback;

    public BarcodeAnalyzer(Callback<Barcode> barcodeCallback) {
        this.barcodeCallback = barcodeCallback;
    }

    @Override
    public void analyze(@NonNull ImageProxy imageProxy) {
        @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();
        if (mediaImage == null) {
            return;
        }
        InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
        BarcodeScanner scanner = BarcodeScanning.getClient();
        Task<List<Barcode>> result = scanner
                .process(image)
                .addOnSuccessListener(barcodes -> {
                    Log.d(tag, "Scanned successfully " + barcodes.size());
                    for (Barcode barcode : barcodes) {
                        barcodeCallback.execute(barcode);
                    }
                }).addOnFailureListener(e -> Log.d(tag, "Failed scanning " + e.toString()))
                .addOnCompleteListener(command -> imageProxy.close());
    }
}
