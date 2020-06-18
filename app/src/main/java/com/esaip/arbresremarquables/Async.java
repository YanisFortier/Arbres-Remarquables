package com.esaip.arbresremarquables;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Async extends AsyncTask<String, Void, Drawable> {
    @Override
    protected Drawable doInBackground(String... params) {
        String[] split = params[0].split(";");
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) new URL(split[0]).openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        connection.setRequestProperty("User-agent", "Mozilla/4.0");

        try {
            connection.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream input = null;
        try {
            input = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(input);
        return new BitmapDrawable(Resources.getSystem(), bitmap);
    }
}