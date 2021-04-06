package com.example.bettertrialbook.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bettertrialbook.R;

public class ScanQRActivity extends AppCompatActivity {

//    https://developer.android.com/training/camera/cameradirect
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
    }
}