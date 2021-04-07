package com.example.bettertrialbook.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.bettertrialbook.Extras;
import com.example.bettertrialbook.R;
import com.example.bettertrialbook.dal.QRDAL;
import com.example.bettertrialbook.models.QRCode;
import com.example.bettertrialbook.models.Trial;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CreateQRActivity extends AppCompatActivity {
    private static final String TAG = "Create QR Activity";
    private QRDAL qrdal = new QRDAL();
    private String qrCodeId;
    private String experimentId;
    private String trialId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
        extractExtras();

        //https://github.com/androidmads/QRGenerator
        qrCodeId = qrdal.getUnusedId();
        QRGEncoder qrgEncoder = new QRGEncoder(qrCodeId, null, QRGContents.Type.TEXT, 500);
        ImageView imageView = findViewById(R.id.generated_qr_code);
        Bitmap bitmap = qrgEncoder.getBitmap();
        imageView.setImageBitmap(bitmap);
    }

    private void extractExtras(){
        Intent intent = getIntent();
        experimentId = intent.getStringExtra(Extras.EXPERIMENT_ID);
        trialId = intent.getStringExtra(Extras.TRIAL_ID);
    }

    public void onConfirm(View v){
        QRCode qrCode = new QRCode(experimentId, trialId, qrCodeId);
        qrdal.registerQRCode(qrCode, this::finish);
    }
}