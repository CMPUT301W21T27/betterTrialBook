package com.example.bettertrialbook.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.bettertrialbook.R;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class CreateQRActivity extends AppCompatActivity {
    private static final String TAG = "Create QR Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_qr);
//        https://github.com/androidmads/QRGenerator
        QRGEncoder qrgEncoder = new QRGEncoder("string2", null, QRGContents.Type.TEXT, 500);

        ImageView imageView = findViewById(R.id.generated_qr_code);
        Bitmap bitmap = qrgEncoder.getBitmap();
        imageView.setImageBitmap(bitmap);
    }
}