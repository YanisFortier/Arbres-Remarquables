package com.esaip.arbresremarquables;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Models.Arbre;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.regex.Pattern;

import static android.widget.AdapterView.OnItemSelectedListener;

public class AjoutArbre extends AppCompatActivity {

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";

    //Variables
    private Spinner spinnerNomArbreArb, spinnerEspaceArb;
    private EditText editTextLatitudeArb, editTextLongitudeArb, editTextNomPrenomArb, editTextAdresseMailArb, editTextPseudoArb,editTextObservationsArb, editTextAdresseArbreArb;
    private LinearLayout layoutNomArbreArb;
    private CheckBox checkBoxRemArb1,checkBoxRemArb2,checkBoxRemArb3,checkBoxVerifArb;
    private Button buttonValid;
    private String stringTextNomPrenom, stringTextPseudo, stringTextObservations, stringTextMail, stringTextAdresse;

    //Location
    private LatLng mLatLng;
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup - FindViewById
        setContentView(R.layout.activity_ajout_arbre);
        spinnerNomArbreArb = findViewById(R.id.spinnerNomArbreArb);
        spinnerEspaceArb = findViewById(R.id.spinnerEspaceArb);
        layoutNomArbreArb = findViewById(R.id.layoutNomArbreArb);
        editTextNomPrenomArb = findViewById(R.id.editTextNomPrenomArb);
        editTextAdresseMailArb = findViewById(R.id.editTextAdresseMailArb);
        editTextPseudoArb = findViewById(R.id.editTextPseudoArb);
        editTextLongitudeArb = findViewById(R.id.editTextLongitudeArb);
        editTextLatitudeArb = findViewById(R.id.editTextLatitudeArb);
        editTextAdresseArbreArb = findViewById(R.id.editTextAdresseArbreArb);
        editTextObservationsArb = findViewById(R.id.editTextObservationArb);
        checkBoxRemArb1 = findViewById(R.id.checkBoxRemArb1);
        checkBoxRemArb2 = findViewById(R.id.checkBoxRemArb2);
        checkBoxRemArb3 = findViewById(R.id.checkBoxRemArb3);
        checkBoxVerifArb = findViewById(R.id.checkBoxVerifArb);
        buttonValid = findViewById(R.id.buttonValiderArb);

        //Détection si le nom de l'arbre est autre
        spinnerNomArbreArb.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nomArbreSelected = spinnerNomArbreArb.getSelectedItem().toString();
                if (nomArbreSelected.equals("Autre")) {
                    layoutNomArbreArb.setVisibility(View.VISIBLE);
                } else {
                    layoutNomArbreArb.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringTextNomPrenom = editTextNomPrenomArb.getText().toString().trim();
                stringTextPseudo = editTextPseudoArb.getText().toString().trim();
                stringTextMail = editTextAdresseMailArb.getText().toString().trim();
                stringTextAdresse = editTextAdresseArbreArb.getText().toString().trim();
                stringTextObservations = editTextObservationsArb.getText().toString().trim();
                int count = 0;

                if(!checkPatternMail(stringTextMail)){
                    editTextAdresseMailArb.setError("Adresse mail non valide");
                }else {
                    count+=1;
                }

                if (stringTextNomPrenom.isEmpty()){
                    editTextNomPrenomArb.setError("Ce champ est obligatoire");
                }else if(!checkPatternGeneral(stringTextNomPrenom)){
                    editTextNomPrenomArb.setError("Nom et prénom non valide");
                }else {
                    count+=1;
                }

                if (stringTextPseudo.isEmpty()){
                    editTextPseudoArb.setError("Ce champ est obligatoire");
                }else if(!checkPatternPseudo(stringTextPseudo)){
                    editTextPseudoArb.setError("Pseudonyme non valide");
                }else {
                    count+=1;
                }

                if (stringTextAdresse.isEmpty()){
                    editTextAdresseArbreArb.setError("Ce champ est obligatoire");
                }else if(!checkPatternAdresse(stringTextAdresse)){
                    editTextAdresseArbreArb.setError("Adresse non valide");
                }else {
                    count+=1;
                }

                if(!checkPatternObervations(stringTextObservations)){
                    editTextObservationsArb.setError("Commentaires non valide");
                }else {
                    count+=1;
                }

                if (count==5){
                    saveData();
                    finish();
                    Toast.makeText(AjoutArbre.this,"Correct",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(AjoutArbre.this,"Champs incorrects ou manquants, veuillez remplir toutes les informations nécessaires",Toast.LENGTH_LONG).show();
                }
            }
        });

        loadData();


    }

    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT_NOM_PRENOM, editTextNomPrenomArb.getText().toString());
        editor.putString(TEXT_ADRESSE_MAIL, editTextAdresseMailArb.getText().toString());
        editor.putString(TEXT_PSEUDO, editTextPseudoArb.getText().toString());

        editor.apply();

        Intent i = new Intent(this, MapsActivity.class);
        //Toast.makeText(this, "Arbre enregistré !", Toast.LENGTH_LONG).show();
    }

    public void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        String textNomPrenom = sharedPreferences.getString(TEXT_NOM_PRENOM, "");
        String textAdresseMail = sharedPreferences.getString(TEXT_ADRESSE_MAIL, "");
        String textPseudo = sharedPreferences.getString(TEXT_PSEUDO, "");

        editTextNomPrenomArb.setText(textNomPrenom);
        editTextAdresseMailArb.setText(textAdresseMail);
        editTextPseudoArb.setText(textPseudo);
    }

    private Arbre getInfos(EditText nomPrenom, EditText pseudo, EditText adresseMail, EditText longitude, EditText latitude, EditText adresseArbre, EditText observation, Spinner nomArbre, Spinner espace, CheckBox rem1, CheckBox rem2, CheckBox rem3, CheckBox verif){
        Arbre arbre = new Arbre();
        arbre.setNomPrenom(nomPrenom.getText().toString());
        arbre.setPseudo(pseudo.getText().toString());
        arbre.setMail(adresseMail.getText().toString());
        arbre.setLongitude(longitude.getText().toString());
        arbre.setLatitude(latitude.getText().toString());
        arbre.setAdresseArbre(adresseArbre.getText().toString());
        arbre.setObservations(observation.getText().toString());
        String txtCheckBoxRem = "";
        if(checkBoxRemArb1.isChecked()){
            txtCheckBoxRem+="* Très remarquable ou exceptionnel";
        }
        if (checkBoxRemArb2.isChecked()){
            txtCheckBoxRem+="* Remarquable ou majestueux";
        }
        if(checkBoxRemArb3.isChecked()){
            txtCheckBoxRem+="* Notoire";
        }
        arbre.setRemarquable(txtCheckBoxRem);
        if (checkBoxVerifArb.isChecked()){
            arbre.setVerification("oui");
        }
        else{
            arbre.setVerification("non");
        }

        if (spinnerEspaceArb.getSelectedItem().toString().equals("Un espace public")){
            arbre.setEspace("Public");
        }
        else if(spinnerEspaceArb.getSelectedItem().toString().equals("Un espace privé")){
            arbre.setEspace("Privé");
        }
        else if(spinnerEspaceArb.getSelectedItem().toString().equals("Je ne sais pas")){
            arbre.setEspace("Inconnu");
        }
        return arbre;
    }

    private Boolean checkPatternMail(String txt){
        Pattern MAIL = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
        return MAIL.matcher(txt).matches();
    }

    private Boolean checkPatternGeneral(String txt){
        Pattern REG1 = Pattern.compile("^([A-Z][a-zâäèéêëîïôöûüñç ]+)(\\-?[A-Z][a-zâäèéêëîïôöûüñç ]+)*$");
        return REG1.matcher(txt).matches();
    }

    private Boolean checkPatternPseudo(String txt){
        Pattern PSEUDO = Pattern.compile("^([A-zâäèéêëîïôöûüñç\\-\\d ])+$");
        return PSEUDO.matcher(txt).matches();
    }

    private Boolean checkPatternAdresse(String txt){
        Pattern ADRESSE = Pattern.compile("^([A-Za-zâäèéêëîïôöûüñç\\-\\d ])+[']?([A-Za-zâäèéêëîïôöûüñç\\-\\d ])*$");
        return ADRESSE.matcher(txt).matches();
    }

    private Boolean checkPatternObervations(String txt){
        Pattern OBSERVATIONS = Pattern.compile("^(([A-Za-zâäàèéêëîïôöûüùñç\\-\\d ])+[']?([A-Za-zâäàèéêëîïôöûüùñç\\-\\d ])*([,\\.;/!:?()\\[\\]])*)+$");
        return OBSERVATIONS.matcher(txt).matches();
    }

}
