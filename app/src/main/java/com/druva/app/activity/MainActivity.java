package com.druva.app.activity;

import com.google.android.gms.vision.Tracker;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import android.database.sqlite.SQLiteOpenHelper;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerActivity;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.nextinnovation.pt.barcodescanner.R;
import com.druva.app.fragment.BarcodeFragment;
import com.druva.app.fragment.CameraFragment;
import com.druva.app.utils.BeamDataToServer;
import com.druva.app.utils.ImageSaver;
import com.druva.app.utils.Utils;

import org.apache.commons.io.IOUtils;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements BarcodeFragment.ScanRequest, CameraFragment.CaptureRequest {


    MainActivity ma=this;
    Barcode bar;
    int count=0;
    MaterialBarcodeScannerBuilder pj;
  //  MaterialBarcodeScanner pjmat;
    private Context context;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static final String BARCODE_KEY = "BARCODE";
    private Barcode barcodeResult;
    private final String TAG = MainActivity.class.getSimpleName();
    private final int MY_PERMISSION_REQUEST_CAMERA = 1001;
    private final int MY_PERMISSION_REQUEST_STORAGE = 101;
    private ItemScanned itemScanned;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};

    private final String URL = "http://10.152.56.134:8080/api/media/validate?app=DCS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databasehelper db =new databasehelper(this);
        String s;
        s=db.getProduct();
        if(s.equals("") || s=="false") {
            startSettingsActivity();
        }
        setContentView(R.layout.activity_main);
        context = this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        checkPermissions();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BarcodeFragment(), "Barcode Scanner");
        adapter.addFragment(new CameraFragment(), "Camera Capture");
        viewPager.setAdapter(adapter);
    }

    public String getScanTime() {
        DateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(new Date());
    }

    public String getScanDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    @Override
    public void scanBarcode() {
        /** This method will listen the button clicked passed form the fragment **/

                checkBarcodePermission();


    }

    public void onNewItem(int id, Barcode item) {
        bar=item;
    }

  /*  public void onResult(Barcode barcode) {
        barcodeResult = barcode;
        try {
            sendBarCodeData(barcodeResult.rawValue);
        } catch (JSONException e) {
            Utils.showDialog(e.getMessage(), MainActivity.this);
        }
    }*/
    @Override
    public void captureRequest() {

        captureActivities();

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }


        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.item_share) {
            //setContentView(R.layout.menu_click);
            startSettingsActivity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        MainActivity.this.recreate();
    }

    private void checkBarcodePermission() {
        startScanningBarcode();
    }

    private void captureActivities() {
        setContentView(R.layout.activity_camera);
    }

    public void onCaptureClick(View view) {

        Intent intent = BaseCameraActivity.createCameraIntent(MainActivity.this);
        startActivityForResult(intent, MY_PERMISSION_REQUEST_CAMERA);
    }
    private void startScanningBarcode() {
        /**
         * Build a new MaterialBarcodeScanner
         */
/*            MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                    .withActivity(MainActivity.this)
                    .withEnableAutoFocus(true)
                    .withBleepEnabled(false)
                    .withBackfacingCamera()
                    .withCenterTracker()
                    .withText("Scanning...")
                    .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                        @Override
                        public void onResult(Barcode barcode) {
                            barcodeResult = barcode;
                            try {
                                sendBarCodeData(barcodeResult.rawValue);

                            } catch (JSONException e) {
                                Utils.showDialog(e.getMessage(), MainActivity.this);
                            }
                            checkBarcodePermission();
                            //pjmat.onBarcodeScannerResult(barcode);
                        }
                    })
                    .build();
            materialBarcodeScanner.startScan();
            pjmat = materialBarcodeScanner;
            return pjmat;*/
        startTempActivity();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS:{
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
                    //updateViews();
                }
                return;
            }
        }
    }


    public interface ItemScanned {
        void itemUpdated();
    }


    private void sendBarCodeData(String data) throws JSONException {
        JSONObject json = new JSONObject();
        String[] arr = data.split("\\*");

        for(String pair:arr){

            String[] keyvalue = pair.split(":");
            json.put(keyvalue[0].toLowerCase() , keyvalue[1]);
        }
        //System.out.println(json.toJSONString());

        try {
            new BeamDataToServer(this).
                    setUrl("http://10.152.50.167:8080/api/media/barcodedata?app=DCS").
                    post(json);
        } catch (IOException e) {
            Utils.showDialog(e.getMessage(), MainActivity.this);
        }
        //write code for continuing here.
       /* try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
        */
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode ==  MY_PERMISSION_REQUEST_CAMERA && resultCode == RESULT_OK) {
            ImageView imageView = findViewById(R.id.image_photo);
            byte[] bytes = data.getByteArrayExtra(BaseCameraActivity.KEY_IMAGE);

            cameraActivities(bytes);
        }
    }
    private void cameraActivities(byte[] bytes) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(bos);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateandTime = sdf.format(new Date());

        String savedImagePath = new ImageSaver(this).
                setFileName(currentDateandTime + "_DRuVa.jpg").
                setDirectoryName("images").
                setExternal(true).
                save(bytes);

        try {
            File file = new File(savedImagePath);
            FileInputStream input = new FileInputStream(file);

            MultipartFile multipartFile = new MockMultipartFile("file",
                    file.getName(), "image/jpeg", IOUtils.toByteArray(input));


            new BeamDataToServer(this).
                    setUrl(URL).
                    setFilePath(savedImagePath).
                    post(multipartFile);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            Utils.showDialog(e.getMessage(), MainActivity.this);
        }
    }
    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(this,p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }
    //onclick listener for menu..
    public void goBack(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void startSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

    }
    public void startTempActivity(){
        Intent intent=new Intent(ma,temp.class);
        startActivity(intent);
    }


}
