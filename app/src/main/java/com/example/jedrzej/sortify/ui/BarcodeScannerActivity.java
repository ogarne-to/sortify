package com.example.jedrzej.sortify.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.jedrzej.sortify.R;

import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

public class BarcodeScannerActivity extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    public static final String TAG = BarcodeScannerActivity.class.getSimpleName();

    private ZBarScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activcity_barcode_scanner);
        scannerView = (ZBarScannerView) findViewById(R.id.zbarScannerView);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.flashFAB);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scannerView.toggleFlash();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result) {
        String code = result.getContents();
        Intent resultIntent = new Intent();
        resultIntent.putExtra("code", code);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }


}
