package com.druva.app.utils;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import com.druva.app.activity.MainActivity;

import org.json.JSONObject;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BeamDataToServer {
    private Context context;
    private String url = "";
    private String fileName = "";
    private String filePath = "";
    private final String TAG = BeamDataToServer.class.getSimpleName();

    public BeamDataToServer(Context context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public BeamDataToServer setUrl(String url) {
        this.url = url;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public BeamDataToServer setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public BeamDataToServer setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    // Images
    public void post(MultipartFile file) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getOriginalFilename(),
                        RequestBody.create(MediaType.parse("image/png"), file.getBytes()))
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(getUrl())
                .post(requestBody)
                .build();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Do something when request failed
                e.printStackTrace();
                Log.d(TAG, "Request Failed.");
                //Utils.showDialog(e.getMessage(), context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Response returned.");
            }
        });

    }
// Barcode
    public void post(JSONObject data) throws IOException {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .writeTimeout(3, TimeUnit.SECONDS)
                .build();


        okhttp3.RequestBody body = RequestBody.create(JSON, data.toString());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Do something when request failed
                e.printStackTrace();
                Log.d(TAG, "Request Failed.");
                //Utils.showDialog(e.getMessage(), context);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d(TAG, "Response returned.");
            }
        });
    }
}
