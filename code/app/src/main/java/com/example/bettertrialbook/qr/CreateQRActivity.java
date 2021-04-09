package com.example.bettertrialbook.qr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.QRDAL;
import com.example.bettertrialbook.models.QRCode;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * Activity used to generate QR Codes
 */
public class CreateQRActivity extends AppCompatActivity {
    private static final String TAG = "Create QR Activity";
    private static final int SCAN_QR_REQUEST_CODE = 2;
    private QRDAL qrdal = new QRDAL();
    private String qrCodeId;
    private String experimentId;
    private String trialId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        extractExtras();

        // https://github.com/androidmads/QRGenerator
        qrCodeId = qrdal.getUnusedId();
        QRGEncoder qrgEncoder = new QRGEncoder(qrCodeId, null, QRGContents.Type.TEXT, 500);
        ImageView imageView = findViewById(R.id.generated_qr_code);
        Bitmap bitmap = qrgEncoder.getBitmap();
        imageView.setImageBitmap(bitmap);
    }

    /**
     * Obtains the extra information passed through the intent from the previous activity
     */
    private void extractExtras(){
        Intent intent = getIntent();
        experimentId = intent.getStringExtra(Extras.EXPERIMENT_ID);
        trialId = intent.getStringExtra(Extras.TRIAL_ID);
    }

    /**
     * Registers the QR Code upon confirmation
     * @param v
     */
    public void onConfirm(View v){
        QRCode qrCode = new QRCode(experimentId, trialId, qrCodeId);
        qrdal.registerQRCode(qrCode, this::finish);
    }

    /**
     * Lets user scan existing barcode to use as qr
     * @param v
     */
    public void scanExistingBarcode(View v){
        Intent intent = new Intent(this, ScanQRActivity.class);
        intent.putExtra(ScanQRActivity.RETURN_SCANNED_FLAG, "yes");
        startActivityForResult(intent, SCAN_QR_REQUEST_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("QR", requestCode +" " + resultCode);
        if (requestCode == SCAN_QR_REQUEST_CODE ) {
            if (resultCode == ScanQRActivity.SCANNED_OK){
                qrCodeId = data.getStringExtra(Extras.QR_CODE_ID);
                Log.w("QR", "Got qr back" + qrCodeId);
                Toast.makeText(this,"Registering scanned barcode", Toast.LENGTH_SHORT).show();
                onConfirm(null);
            }
        }
    }
}