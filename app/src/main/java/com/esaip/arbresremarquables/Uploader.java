package com.esaip.arbresremarquables;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Uploader {
    
    private OkHttpClient client = new OkHttpClient();
    
    public Uploader() {
    }
    
    public void uploadFile(String serverUrl, File fileCSV) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file", fileCSV.getName(),
                        RequestBody.create(MediaType.parse("text/csv"), fileCSV))
                .build();
        
        Request request = new Request.Builder()
                .url(serverUrl)
                .post(requestBody)
                .build();
        
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.cancel();
            }
    
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(!response.isSuccessful()) {
                    throw new IOException("Unexepected code " + response);
                }
    
                Log.d("TAG", response.body().string());
            }
        });
    }
    
}
