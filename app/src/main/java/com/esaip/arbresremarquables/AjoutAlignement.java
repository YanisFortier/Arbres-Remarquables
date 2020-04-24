package com.esaip.arbresremarquables;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class AjoutAlignement extends AppCompatActivity {

    //Variable
    private LinearLayout Autre,AutreLien;
    private EditText editTextLatitude, editTextLongitude;
    private CheckBox AutreCheckBox;
    private Spinner lienSpinner;

    //Location
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_alignement);
        Autre = findViewById(R.id.editAutre);
        AutreCheckBox = findViewById(R.id.checkAutre);
        AutreLien = findViewById(R.id.editAutreLien);
        lienSpinner = findViewById(R.id.spinnerLien);

        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();

        AutreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Autre.setVisibility(View.VISIBLE);
                }
                else{
                    Autre.setVisibility(View.GONE);
                }
            }
        });

        lienSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String lienSelect = lienSpinner.getSelectedItem().toString();
                if (lienSelect.equals("Autre")){
                    AutreLien.setVisibility(View.VISIBLE);
                }
                else{
                    AutreLien.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fetchLastLocation() {
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Intent intent = new Intent();
                    mCurrentLocation = location;

                    if (intent.getBooleanExtra("geolocalisation", true)) {
                        editTextLatitude = findViewById(R.id.editTextLatitude);
                        editTextLongitude = findViewById(R.id.editTextLongitude);
                        editTextLatitude.setText(String.valueOf(mCurrentLocation.getLatitude()));
                        editTextLongitude.setText(String.valueOf(mCurrentLocation.getLongitude()));
                    } else {
                        Toast.makeText(AjoutAlignement.this, "Là c'est pas encore codé", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
}
