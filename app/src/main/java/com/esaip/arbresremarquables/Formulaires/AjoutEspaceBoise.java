package com.esaip.arbresremarquables.Formulaires;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.R;

public class AjoutEspaceBoise extends AppCompatActivity {
    //Variables pour la sauvegarde utilisateur
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";
    private EditText editTextNomPrenom, editTextAdresseMail, editTextPseudo,editTextLatitude, editTextLongitude;
    private LinearLayout autre;
    private CheckBox autreCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_espace_boise);

        //Setup - FindViewById
        editTextNomPrenom = findViewById(R.id.editTextNomPrenom);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMail);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        autre = findViewById(R.id.editAutre);
        autreCheckBox = findViewById(R.id.checkautrebox);

        //Location
        double lat=0;
        double lon=0;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            lat = bundle.getDouble("latitude");
            lon = bundle.getDouble("longitude");

            editTextLatitude = findViewById(R.id.editTextLatitude);
            editTextLongitude = findViewById(R.id.editTextLongitude);
            editTextLatitude.setText(String.valueOf(lat));
            editTextLongitude.setText(String.valueOf(lon));

        }

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
    }

    private void openDialog() {
        //DialogArbre dialog = new DialogArbre(editTextNomPrenom.getText().toString(), editTextPseudo.getText().toString(), editTextAdresseMail.getText().toString());
        //dialog.show(getSupportFragmentManager(), "example dialog");
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
