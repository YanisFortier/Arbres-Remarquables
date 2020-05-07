package com.esaip.arbresremarquables.Activités;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Model.Arbre;
import com.esaip.arbresremarquables.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

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


    }

    public void saveData(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT_NOM_PRENOM, editTextNomPrenomArb.getText().toString());
        editor.putString(TEXT_ADRESSE_MAIL, editTextAdresseMailArb.getText().toString());
        editor.putString(TEXT_PSEUDO, editTextPseudoArb.getText().toString());

        editor.apply();

        Intent i = new Intent(this, MapsActivity.class);
        Toast.makeText(this, "Arbre enregistré !", Toast.LENGTH_LONG).show();
        finish();
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

}
