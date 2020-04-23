package com.esaip.arbresremarquables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class AjoutArbre extends AppCompatActivity {

    boolean res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_arbre);

        Intent inf = getIntent();
        res = inf.getBooleanExtra("geolocalisation",false);

    }


}
