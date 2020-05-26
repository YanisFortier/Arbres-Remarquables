package com.esaip.arbresremarquables.Formulaires;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Dialogs.DialogAlignement;
import com.esaip.arbresremarquables.R;
import com.google.android.gms.location.FusedLocationProviderClient;

public class AjoutAlignement extends AppCompatActivity {

    //Variables pour la sauvegarde utilisateur
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";

    //Variables Layout
    private LinearLayout autre, autreLien;
    private EditText editTextLatitude, editTextLongitude;
    private EditText editTextNomPrenom;
    private EditText editTextPseudo;
    private EditText editTextAdresseMail;
    private EditText editTextAdresseAlignement;
    private Spinner spinnerEspace;
    private Spinner spinnerNombreArbre;
    private Spinner spinnerNombreEspece;
    private CheckBox checkBoxEspeceAutre;
    private EditText editTextAutreEspece;
    private EditText editTextNomBotanique;
    private CheckBox checkBoxLienAutre;
    private EditText editTextAutreLien;
    private Spinner spinnerProtection;
    private EditText editTextObservations;
    private CheckBox checkboxVerification;

    //Location
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_alignement);

        //Setup - FindViewById
        autre = findViewById(R.id.editAutre);
        checkBoxEspeceAutre = findViewById(R.id.checkBoxEspeceAutre);
        autreLien = findViewById(R.id.editAutreLien);
        checkBoxLienAutre = findViewById(R.id.checkBoxLienAutre);

        editTextNomPrenom = findViewById(R.id.editTextNomPrenom);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMail);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        editTextAdresseAlignement = findViewById(R.id.editTextAdresseAlignement);
        spinnerEspace = findViewById(R.id.spinnerEspace);
        spinnerNombreArbre = findViewById(R.id.spinnerNombresArbres);
        spinnerNombreEspece = findViewById(R.id.spinnerNombresEspeces);
        checkBoxEspeceAutre = findViewById(R.id.checkBoxEspeceAutre);
        editTextAutreEspece = findViewById(R.id.editTextAutreEspece);
        editTextNomBotanique = findViewById(R.id.editTextNomBotanique);
        checkBoxLienAutre = findViewById(R.id.checkBoxLienAutre);
        editTextAutreLien = findViewById(R.id.editTextAutreLien);
        spinnerProtection = findViewById(R.id.spinnerProtection);
        editTextObservations = findViewById(R.id.editTextObservation);
        checkboxVerification = findViewById(R.id.checkBoxVerification);

        checkBoxEspeceAutre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    autre.setVisibility(View.VISIBLE);
                } else {
                    autre.setVisibility(View.GONE);
                }
            }
        });

        checkBoxLienAutre.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
    }

    private void openDialog() {
        String nomPrenom = editTextNomPrenom.getText().toString();
        String pseudo = editTextPseudo.getText().toString();
        String email = editTextAdresseMail.getText().toString();
        String adresse = editTextAdresseAlignement.getText().toString();
        String espace = spinnerEspace.getSelectedItem().toString();
        String nombresArbres = spinnerNombreArbre.getSelectedItem().toString();
        String nombresEspeces = spinnerNombreEspece.getSelectedItem().toString();
        String especes = ""; // TODO
        String lien = ""; // TODO
        String protection = spinnerProtection.getSelectedItem().toString();
        String observations = editTextObservations.getText().toString();

        boolean verification = false;
        if (checkboxVerification.isChecked())
            verification = true;


        DialogAlignement dialog = new DialogAlignement(nomPrenom, pseudo, email, adresse, espace, nombresArbres, nombresEspeces, especes, lien, protection, observations, verification);
        dialog.show(getSupportFragmentManager(), "Dialog AjoutAlignement");
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
