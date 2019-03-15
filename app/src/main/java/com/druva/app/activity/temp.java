package com.druva.app.activity;




import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;

public class temp extends Activity {

    MaterialBarcodeScannerBuilder pj;

    public static final String BARCODE_KEY = "BARCODE";

    MaterialBarcodeScanner pjmat, temp;

    private Barcode barcodeResult;

    private TextView result;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startscan();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScan();
        //finish();
    }

    public void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         *
         */
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                synchronized (temp.this) {
                    final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                            .withActivity(temp.this)
                            .withEnableAutoFocus(true)
                            .withBleepEnabled(false)
                            .withBackfacingCamera()
                            .withCenterTracker()
                            .withText("Scanning...")
                            .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                                @Override
                                public void onResult(Barcode barcode) {
                                    barcodeResult = barcode;
                                    Toast toast = Toast.makeText(getApplicationContext(), barcodeResult.rawValue,
                                            Toast.LENGTH_SHORT);

                                    toast.show();
                                    //result.setText(barcode.rawValue);
                       /* EventBus.getDefault().removeStickyEvent(barcodeResult);
                        EventBus.getDefault().unregister(pjmat);
                        pj=pjmat.getMaterialBarcodeScannerBuilder();
                        pj.clean();
                        startScan();*/
                                    //finish();
                                    //pjmat.onBarcodeScannerResult(MaterialBarcodeScannerActivity.bar);
                                }
                            })
                            .build();
                    materialBarcodeScanner.startScan();
                    pjmat = materialBarcodeScanner;
                }
            }
        });
    }
}

