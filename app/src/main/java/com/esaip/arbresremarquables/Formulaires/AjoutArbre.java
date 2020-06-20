package com.esaip.arbresremarquables.Formulaires;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Dialogs.DialogArbre;
import com.esaip.arbresremarquables.Models.Arbre;
import com.esaip.arbresremarquables.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static android.widget.AdapterView.OnItemSelectedListener;

public class AjoutArbre extends AppCompatActivity {

    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";

    //Variables
    private Spinner spinnerNomArbre, spinnerEspace;
    private EditText editTextNomPrenom, editTextAdresseMail, editTextPseudo, editTextLatitude, editTextLongitude, editTextAdresseArbre, editTextObservations, editTextAutreArbre, editTextNomBotanique;
    private LinearLayout layoutNomArbre;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private CheckBox checkboxVerification;
    private Button buttonValid, buttonDialog;
    private String stringTextNomPrenom, stringTextPseudo, stringTextObservations, stringTextMail, stringTextAdresse,stringLatitude, stringLongitude, stringAutreArbre = "*Sans réponse*", stringNomBotanique = "*Sans réponse*",stringPhoto,ZipName;
    private String nomArbre = null, verification = "non", remarquable = null;
    private DialogArbre dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_arbre);

        //Setup - FindViewById
        setContentView(R.layout.activity_ajout_arbre);
        buttonValid = findViewById(R.id.buttonValiderArb);
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
        editTextAutreArbre = findViewById(R.id.editTextAutreArbreArb);
        editTextNomBotanique = findViewById(R.id.editTextNomBotaniqueArb);
        checkboxVerification = findViewById(R.id.checkBoxVerifArb);

        //Ajout de la géolocalisation
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Récupération des coordonnées depuis le bundle
            Double latitude_arbre = bundle.getDouble("latitude_arbre");
            Double longitude_arbre = bundle.getDouble("longitude_arbre");
            //Format à 7 décimales
            String latitude = String.format("%.7f", latitude_arbre).replace(",",".");;
            String longitude = String.format("%.7f", longitude_arbre).replace(",",".");

            //Ouput
            Log.e("Latitude arbre", String.valueOf(latitude_arbre));
            Log.e("Longitude arbre", String.valueOf(longitude_arbre));
            editTextLatitude.setText(latitude);
            editTextLongitude.setText(longitude);
        }

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

        buttonValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringTextNomPrenom = editTextNomPrenom.getText().toString().trim();
                stringTextPseudo = editTextPseudo.getText().toString().trim();
                stringTextMail = editTextAdresseMail.getText().toString().trim();
                stringTextAdresse = editTextAdresseArbre.getText().toString().trim();
                stringTextObservations = editTextObservations.getText().toString().trim();
                stringLatitude = editTextLatitude.getText().toString();
                stringLongitude = editTextLongitude.getText().toString();
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

                if (!stringTextObservations.isEmpty() && !checkPatternObervations(stringTextObservations)) {
                    editTextObservations.setError("Commentaires non valide");
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

                if(spinnerNomArbre.getSelectedItem().toString().equals("Autre")){
                    stringAutreArbre = editTextAutreArbre.getText().toString().trim();
                    stringNomBotanique = editTextNomBotanique.getText().toString().trim();
                    if (!stringNomBotanique.isEmpty() && !checkPatternGeneral(stringNomBotanique)) {
                        editTextNomBotanique.setError("Nom non valide");
                    } else {
                        count += 1;
                    }

                    if (stringAutreArbre.isEmpty()) {
                        editTextAutreArbre.setError("Ce champ est obligatoire");
                    } else if (!checkPatternGeneral(stringAutreArbre)) {
                        editTextAutreArbre.setError("Nom non valide");
                    } else {
                        count += 1;
                        stringAutreArbre = editTextAutreArbre.toString();
                        nomArbre = "autre";
                    }
                }else{
                    nomArbre = spinnerNomArbre.getSelectedItem().toString();
                }

                if ((count == 7 && !spinnerNomArbre.getSelectedItem().toString().equals("Autre")) || (count == 9 && spinnerNomArbre.getSelectedItem().toString().equals("Autre"))) {
                    saveData();
                    //finish();

                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                        radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
                        remarquable = radioButton.getText().toString();
                    }

                    if (checkboxVerification.isChecked()) verification = "oui";

                    Intent intent = getIntent();
                    stringPhoto = intent.getStringExtra("photo1");
                    String paths = intent.getStringExtra("path");

                    //Stocker les données dans la classe Arbre
                    Arbre arbre = new Arbre(
                            stringTextNomPrenom,
                            stringTextPseudo,
                            stringTextMail,
                            editTextLatitude.getText().toString().trim(),
                            editTextLongitude.getText().toString().trim(),
                            stringTextAdresse,
                            stringPhoto,
                            stringTextObservations,
                            nomArbre,
                            stringAutreArbre,
                            stringNomBotanique,
                            getEspace(),
                            remarquable,
                            verification);

                    //Créer le fichier CSV
                    arbre.CreateCsv(paths);

                    //Zip le CSV + Photo
                    ZipName = paths+ "reponse_appli_arbreIsol_" + stringPhoto.replace("JPEG_","").replace(".jpg","")+".zip";
                    String []s=new String[2];
                    s[0]=paths+stringPhoto;
                    s[1]=paths+"reponse_"+ stringPhoto.replace("JPEG_","").replace(".jpg","")+".csv";
                    zip(s,ZipName);

                    Toast.makeText(AjoutArbre.this, "Correct", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(AjoutArbre.this, "Champs incorrects ou manquants, veuillez remplir toutes les informations nécessaires", Toast.LENGTH_LONG).show();
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
        String remarquable = null;
        if (radioGroup.getCheckedRadioButtonId() != -1) {
            radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
            remarquable = radioButton.getText().toString();
        }

        String nomArbre = spinnerNomArbre.getSelectedItem().toString();
        String espace = spinnerEspace.getSelectedItem().toString();

        boolean verification = false;
        if (checkboxVerification.isChecked())
            verification = true;


        dialog = new DialogArbre(stringTextNomPrenom, stringTextPseudo, stringTextMail, nomArbre, stringTextAdresse, espace, remarquable, stringTextObservations, verification);
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

    private String getEspace(){
        String txt = "";
        switch (spinnerEspace.getSelectedItem().toString())
        {
            case "Un espace public":
                txt = "Public";
                break;
            case "Un espace privé":
                txt = "Privé";
                break;
            case "Je ne sais pas":
                txt = "Inconnu";
                break;
        }
        return txt;
    }

    //Fonction pour Zip les fichiers
    public void zip(String[] files, String zipFile) {
        String[] _files = files;
        String _zipFile = zipFile;

        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(_zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte[] data = new byte[1024];

            for (int i = 0; i < _files.length; i++) {
                Log.d("add:", _files[i]);
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, 1024);
                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, 1024)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Fonctions de vérification des données avec Regex
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
        Pattern LONGITUDE = Pattern.compile("^(\\+|-)?(?:180(?:(?:\\.0{1,8})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,8})?))$");
        return LONGITUDE.matcher(txt).matches();
    }

}
