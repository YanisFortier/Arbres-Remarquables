package com.esaip.arbresremarquables.Formulaires;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Dialogs.DialogArbre;
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
    private Spinner spinnerNomArbre, spinnerEspace;
    private EditText editTextNomPrenom, editTextAdresseMail, editTextPseudo, editTextLatitude, editTextLongitude, editTextAdresseArbre, editTextObservations;
    private LinearLayout layoutNomArbre;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox checkboxVerification;

    //Location
    private LatLng mLatLng;
    private Location mCurrentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setup - FindViewById
        setContentView(R.layout.activity_ajout_arbre);
        spinnerNomArbre = findViewById(R.id.spinnerNomArbreArb);
        spinnerEspace = findViewById(R.id.spinnerEspaceArb);
        layoutNomArbre = findViewById(R.id.layoutNomArbreArb);
        editTextNomPrenom = findViewById(R.id.editTextNomPrenomArb);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMailArb);
        editTextPseudo = findViewById(R.id.editTextPseudoArb);
        editTextLongitude = findViewById(R.id.editTextLongitudeArb);
        editTextLatitude = findViewById(R.id.editTextLatitudeArb);
        radioGroup = findViewById(R.id.RadioGroupRemarquable);
        editTextAdresseArbre = findViewById(R.id.editTextAdresseArbreArb);
        editTextObservations = findViewById(R.id.editTextObservationArb);
        checkboxVerification = findViewById(R.id.checkBoxVerifArb);

        //Détection si le nom de l'arbre est autre
        spinnerNomArbre.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nomArbreSelected = spinnerNomArbre.getSelectedItem().toString();
                if (nomArbreSelected.equals("Autre")) {
                    layoutNomArbre.setVisibility(View.VISIBLE);
                } else {
                    layoutNomArbre.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
        String remarquable = null;
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            remarquable = radioButton.getText().toString();
        }
        String nomPrenom = editTextNomPrenom.getText().toString();
        String pseudo = editTextPseudo.getText().toString();
        String email = editTextAdresseMail.getText().toString();
        String nomArbre = spinnerNomArbre.getSelectedItem().toString();
        String adresseArbre = editTextAdresseArbre.getText().toString();
        String espace = spinnerEspace.getSelectedItem().toString();
        String observations = editTextObservations.getText().toString();

        boolean verification = false;
        if (checkboxVerification.isChecked())
            verification = true;


        DialogArbre dialog = new DialogArbre(nomPrenom, pseudo, email, nomArbre, adresseArbre, espace, remarquable, observations, verification);
        dialog.show(getSupportFragmentManager(), "Dialog AjoutArbre");
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

    /*
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
    }*/

}
