package com.esaip.arbresremarquables;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Dialogs.DialogArbre;
import com.google.android.gms.location.FusedLocationProviderClient;

public class AjoutAlignement extends AppCompatActivity {

    //Variables pour la sauvegarde utilisateur
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";
    private EditText editTextNomPrenom, editTextAdresseMail, editTextPseudo;


    //Variable
    private LinearLayout autre, autreLien;
    private EditText editTextLatitude, editTextLongitude;
    private CheckBox liencheckBox, autreCheckBox;

    //Location
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_alignement);

        //Setup - FindViewById
        autre = findViewById(R.id.editAutre);
        autreCheckBox = findViewById(R.id.checkAutre);
        autreLien = findViewById(R.id.editAutreLien);
        liencheckBox = findViewById(R.id.liencheckbox);
        editTextNomPrenom = findViewById(R.id.editTextNomPrenom);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMail);
        editTextPseudo = findViewById(R.id.editTextPseudo);

        autreCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autre.setVisibility(View.VISIBLE);
                } else {
                    autre.setVisibility(View.GONE);
                }
            }
        });

        liencheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autreLien.setVisibility(View.VISIBLE);
                } else {
                    autreLien.setVisibility(View.GONE);
                }
            }
        });



        loadData();
    }



    public void saveData(View view) {
        openDialog();

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT_NOM_PRENOM, editTextNomPrenom.getText().toString());
        editor.putString(TEXT_ADRESSE_MAIL, editTextAdresseMail.getText().toString());
        editor.putString(TEXT_PSEUDO, editTextPseudo.getText().toString());
        editor.apply();

        /*
        new Intent(this, MapsActivity.class);
        Toast.makeText(this, "Alignement d'arbres enregistré !", Toast.LENGTH_LONG).show();
        finish();
         */
    }

    private void openDialog() {
        DialogArbre dialog = new DialogArbre(editTextNomPrenom.getText().toString(), editTextPseudo.getText().toString(), editTextAdresseMail.getText().toString());
        dialog.show(getSupportFragmentManager(), "example dialog");
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
