package com.esaip.arbresremarquables;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
    }

    public void retour() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
