package com.esaip.arbresremarquables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";

    //Variables
    private Spinner spinnerNomArbre;
    private EditText editTextLatitude, editTextLongitude, editTextNomPrenom, editTextAdresseMail, editTextPseudo;
    private LinearLayout editTextNomArbre;

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
        editTextNomPrenom = findViewById(R.id.editTextNomPrenom);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMail);
        editTextPseudo = findViewById(R.id.editTextPseudo);

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

    public void saveData(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT_NOM_PRENOM, editTextNomPrenom.getText().toString());
        editor.putString(TEXT_ADRESSE_MAIL, editTextAdresseMail.getText().toString());
        editor.putString(TEXT_PSEUDO, editTextPseudo.getText().toString());

        editor.apply();

        Intent i = new Intent(this, MapsActivity.class);
        Toast.makeText(this, "Alignement d'arbres enregistré !", Toast.LENGTH_LONG).show();
        finish();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String textNomPrenom = sharedPreferences.getString(TEXT_NOM_PRENOM, "");
        String textAdresseMail = sharedPreferences.getString(TEXT_ADRESSE_MAIL, "");
        String textPseudo = sharedPreferences.getString(TEXT_PSEUDO, "");

        editTextNomPrenom.setText(textNomPrenom);
        editTextAdresseMail.setText(textAdresseMail);
        editTextPseudo.setText(textPseudo);
    }

}
