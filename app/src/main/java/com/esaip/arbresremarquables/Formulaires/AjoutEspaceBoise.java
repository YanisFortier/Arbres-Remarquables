package com.esaip.arbresremarquables.Formulaires;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.R;

import java.util.regex.Pattern;

public class AjoutEspaceBoise extends AppCompatActivity {
    //Variables pour la sauvegarde utilisateur
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";
    private EditText editTextNomPrenom, editTextAdresseMail, editTextPseudo,editTextLatitude, editTextLongitude, editTextAdresseArbre, editTextObservations ;
    private LinearLayout autre;
    private CheckBox autreCheckBox,checkBoxArbre,checkBoxArbuste,checkBoxHerbe,checkBoxEcureuil,checkBoxChauve,checkBoxCapricorne,checkBoxChouette,checkBoxPic,checkBoxRefuge,checkBoxIlot,checkBoxPaysager;
    private Spinner spinnerTypeEspace, spinnerNbArbres, spinnerNbEspeces, spinnerEau, spinnerAbris, spinnerEclairage, spinnerOmbre, spinnerEntretien;
    private Button buttonValider;
    private String stringTextNomPrenom, stringTextPseudo, stringTextObservations, stringTextMail, stringTextAdresse,stringLatitude, stringLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_espace_boise);

        //Setup - FindViewById
        editTextNomPrenom = findViewById(R.id.editTextNomPrenomEsp);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMailEsp);
        editTextPseudo = findViewById(R.id.editTextPseudoEsp);
        editTextAdresseArbre = findViewById(R.id.editTextAdresseEsp);
        editTextObservations = findViewById(R.id.editTextObservationEsp);
        autre = findViewById(R.id.editAutre);
        autreCheckBox = findViewById(R.id.checkautrebox);
        checkBoxArbre = findViewById(R.id.checkBoxArbre);
        checkBoxArbuste = findViewById(R.id.checkBoxArbuste);
        checkBoxHerbe = findViewById(R.id.checkBoxHerbe);
        checkBoxEcureuil = findViewById(R.id.checkBoxEcureuil);
        checkBoxChauve = findViewById(R.id.checkBoxChauve);
        checkBoxCapricorne = findViewById(R.id.checkBoxCapricorne);
        checkBoxChouette = findViewById(R.id.checkBoxChouette);
        checkBoxPic = findViewById(R.id.checkBoxPic);
        checkBoxRefuge = findViewById(R.id.checkBoxRefuge);
        checkBoxIlot = findViewById(R.id.checkBoxIlot);
        checkBoxPaysager = findViewById(R.id.checkBoxPaysager);
        spinnerTypeEspace = findViewById(R.id.spinnerEspaceEsp);
        spinnerNbArbres = findViewById(R.id.spinnerNbArbresEsp);
        spinnerNbEspeces = findViewById(R.id.spinnerNbEsp);
        spinnerEau = findViewById(R.id.spinnerPtEau);
        spinnerAbris = findViewById(R.id.spinnerAbris);
        spinnerEclairage = findViewById(R.id.spinnerEclairage);
        spinnerOmbre = findViewById(R.id.spinnerOmbre);
        spinnerEntretien = findViewById(R.id.spinnerEntretien);
        buttonValider = findViewById(R.id.buttonValiderEsp);

        //Location
        double lat=0;
        double lon=0;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            lat = bundle.getDouble("latitude");
            lon = bundle.getDouble("longitude");
            editTextLatitude = findViewById(R.id.editTextLatitudeEsp);
            editTextLongitude = findViewById(R.id.editTextLongitudeEsp);
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

        buttonValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringTextNomPrenom = editTextNomPrenom.getText().toString().trim();
                stringTextPseudo = editTextPseudo.getText().toString().trim();
                stringTextMail = editTextAdresseMail.getText().toString().trim();
                stringTextAdresse = editTextAdresseArbre.getText().toString().trim();
                stringTextObservations = editTextObservations.getText().toString().trim();
                stringLatitude = editTextLatitude.getText().toString().trim();
                stringLongitude = editTextLongitude.getText().toString().trim();
                int count = 0;

                if (!stringTextMail.isEmpty() && !checkPatternMail(stringTextMail)) {
                    editTextAdresseMail.setError("Adresse mail non valide");
                } else {
                    count += 1;
                }

                if (stringTextNomPrenom.isEmpty()) {
                    editTextNomPrenom.setError("Ce champ est obligatoire");
                } else if (!checkPatternGeneral(stringTextNomPrenom)) {
                    editTextNomPrenom.setError("Nom et prénom non valide");
                } else {
                    count += 1;
                }

                if (stringTextPseudo.isEmpty()) {
                    editTextPseudo.setError("Ce champ est obligatoire");
                } else if (!checkPatternPseudo(stringTextPseudo)) {
                    editTextPseudo.setError("Pseudonyme non valide");
                } else {
                    count += 1;
                }

                if (stringTextAdresse.isEmpty()) {
                    editTextAdresseArbre.setError("Ce champ est obligatoire");
                } else if (!checkPatternAdresse(stringTextAdresse)) {
                    editTextAdresseArbre.setError("Adresse non valide");
                } else {
                    count += 1;
                }

                if (stringLongitude.isEmpty()) {
                    editTextLongitude.setError("Ce champ est obligatoire");
                } else if (!checkLongitude(stringLongitude)) {
                    editTextLongitude.setError("Longitude non valide");
                } else {
                    count += 1;
                }

                if (stringLatitude.isEmpty()) {
                    editTextLatitude.setError("Ce champ est obligatoire");
                } else if (!checkLatitude(stringLatitude)) {
                    editTextLatitude.setError("Latitude non valide");
                } else {
                    count += 1;
                }

                if (!stringTextObservations.isEmpty() && !checkPatternObervations(stringTextObservations)) {
                    editTextObservations.setError("Commentaires non valide");
                } else {
                    count += 1;
                }

                if (count == 7) {
                    saveData();
                    //finish();
                    Toast.makeText(AjoutEspaceBoise.this, "Correct", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AjoutEspaceBoise.this, "Champs incorrects ou manquants, veuillez remplir toutes les informations nécessaires", Toast.LENGTH_LONG).show();
                }
            }
        });

        loadData();
    }

    public void saveData() {
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

    private Boolean checkPatternMail(String txt){
        Pattern MAIL = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
        return MAIL.matcher(txt).matches();
    }

    private Boolean checkPatternGeneral(String txt){
        Pattern REG1 = Pattern.compile("^([A-Za-zâäèéêëîïôöûüñç ]+)(\\-?[A-Za-zâäèéêëîïôöûüñç ]+)*$");
        return REG1.matcher(txt).matches();
    }

    private Boolean checkPatternPseudo(String txt){
        Pattern PSEUDO = Pattern.compile("^([A-zâäèéêëîïôöûüñç\\-\\d ])+$");
        return PSEUDO.matcher(txt).matches();
    }

    private Boolean checkPatternAdresse(String txt){
        Pattern ADRESSE = Pattern.compile("^([A-Za-zâäèéêëîïôöûüñç\\-\\d ,])+[']?([A-Za-zâäèéêëîïôöûüñç\\-\\d ,])*$");
        return ADRESSE.matcher(txt).matches();
    }

    private Boolean checkPatternObervations(String txt){
        Pattern OBSERVATIONS = Pattern.compile("^(([A-Za-zâäàèéêëîïôöûüùñç\\-\\d ])+[']?([A-Za-zâäàèéêëîïôöûüùñç\\-\\d ])*([,\\.;/!:?()\\[\\]])*)+$");
        return OBSERVATIONS.matcher(txt).matches();
    }

    private Boolean checkLatitude(String txt){
        Pattern LATITUDE = Pattern.compile("^(\\+|-)?(?:90(?:(?:\\.0{1,8})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,8})?))$");
        return LATITUDE.matcher(txt).matches();
    }

    private Boolean checkLongitude(String txt){
        Pattern LONGITUDE = Pattern.compile("^(\\+|-)?(?:180(?:(?:\\.0{1,6})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,6})?))$");
        return LONGITUDE.matcher(txt).matches();
    }
}
