package com.esaip.arbresremarquables;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import static android.widget.AdapterView.OnItemSelectedListener;

public class AjoutArbre extends AppCompatActivity {
    //Variables
    private Spinner spinnerNomArbre;
    private EditText editTextNomArbre;
    private EditText editTextLatitude;
    private EditText editTextLongitude;

    //Location
    private LatLng mLatLng;
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup - FindViewById
        setContentView(R.layout.activity_ajout_arbre);
        spinnerNomArbre = findViewById(R.id.spinnerNomArbre);
        editTextNomArbre = findViewById(R.id.editTextNomArbre);


        //Location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLastLocation();


        //Détection si le nom de l'arbre est autre
        spinnerNomArbre.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nomArbreSelected = spinnerNomArbre.getSelectedItem().toString();
                if (nomArbreSelected.equals("Autre")) {
                    editTextNomArbre.setVisibility(View.VISIBLE);
                } else {
                    editTextNomArbre.setVisibility(View.GONE);
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
                        Toast.makeText(AjoutArbre.this, "Là c'est pas encore codé", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
