package com.esaip.arbresremarquables.Formulaires;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Dialogs.DialogEspaceBoise;
import com.esaip.arbresremarquables.Models.EspaceBoise;
import com.esaip.arbresremarquables.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AjoutEspaceBoise extends AppCompatActivity {
    //Variables pour la sauvegarde utilisateur
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";

    //Variables
    private EditText editTextNomPrenom, editTextAdresseMail, editTextPseudo, editTextLatitude, editTextLongitude, editTextAdresseEsp, editTextObservations, editTextAutreBiodiv;
    private LinearLayout autre;
    private TextView niveau, global;
    private CheckBox autreCheckBox, checkBoxArbre, checkBoxArbuste, checkBoxHerbe, checkBoxEcureuil, checkBoxChauve, checkBoxCapricorne, checkBoxChouette, checkBoxPic, checkBoxRefuge, checkBoxIlot, checkBoxPaysager;
    private Spinner spinnerTypeEspace, spinnerNbArbres, spinnerNbEspeces, spinnerEau, spinnerAbris, spinnerEclairage, spinnerOmbre, spinnerEntretien;
    private Button buttonValider;
    private String stringTextNomPrenom, stringTextPseudo, stringTextObservations, stringTextMail, stringTextAdresse, stringLatitude, stringLongitude, stringAutreBiodiversite, stringEspace, stringNombresArbres, stringNombresEspeces;
    private String stringNiveau, stringAbris, stringEau, stringEclairage, stringBiodiv, stringOmbre, stringEntretien, stringGlobal, stringPhoto, ZipName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_espace_boise);

        //Setup - FindViewById
        editTextNomPrenom = findViewById(R.id.editTextNomPrenomEsp);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMailEsp);
        editTextPseudo = findViewById(R.id.editTextPseudoEsp);
        editTextAdresseEsp = findViewById(R.id.editTextAdresseEsp);
        editTextObservations = findViewById(R.id.editTextObservationEsp);
        editTextAutreBiodiv = findViewById(R.id.editTextAutreBiodiv);
        editTextLatitude = findViewById(R.id.editTextLatitudeEsp);
        editTextLongitude = findViewById(R.id.editTextLongitudeEsp);
        autre = findViewById(R.id.editAutre);
        niveau = findViewById(R.id.errorNiveau);
        global = findViewById(R.id.errorGlobal);
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

        //Ajout de la géolocalisation
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Récupération des coordonnées depuis le bundle
            Double latitude_arbre = bundle.getDouble("latitude_arbre");
            Double longitude_arbre = bundle.getDouble("longitude_arbre");
            //Format à 7 décimales
            String latitude = String.format("%.7f", latitude_arbre).replace(",",".");
            String longitude = String.format("%.7f", longitude_arbre).replace(",",".");
            //Ouput
            editTextLatitude.setText(latitude);
            editTextLongitude.setText(longitude);
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
                stringTextAdresse = editTextAdresseEsp.getText().toString().trim();
                stringTextObservations = editTextObservations.getText().toString().trim();
                stringLatitude = editTextLatitude.getText().toString().trim();
                stringLongitude = editTextLongitude.getText().toString().trim();
                boolean checkNiveau = (checkBoxArbuste.isChecked() || checkBoxArbre.isChecked() || checkBoxHerbe.isChecked());
                boolean checkBiodiversité = (checkBoxEcureuil.isChecked() || checkBoxChauve.isChecked() || checkBoxCapricorne.isChecked() || checkBoxPic.isChecked() || checkBoxChouette.isChecked() || autreCheckBox.isChecked());
                boolean checkGlobal = (checkBoxIlot.isChecked() || checkBoxRefuge.isChecked() || checkBoxPaysager.isChecked());
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
                    editTextAdresseEsp.setError("Ce champ est obligatoire");
                } else if (!checkPatternAdresse(stringTextAdresse)) {
                    editTextAdresseEsp.setError("Adresse non valide");
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

                if(autreCheckBox.isChecked()){
                    stringAutreBiodiversite = editTextAutreBiodiv.getText().toString().trim();
                    if (stringAutreBiodiversite.isEmpty()) {
                        editTextAutreBiodiv.setError("Ce champ est obligatoire");
                    } else if (!checkPatternGeneral(stringAutreBiodiversite)) {
                        editTextAutreBiodiv.setError("Nom non valide");
                    } else {
                        count += 1;
                    }
                }

                if (!stringTextObservations.isEmpty() && !checkPatternObervations(stringTextObservations)) {
                    editTextObservations.setError("Commentaires non valide");
                } else {
                    count += 1;
                }

                if(checkNiveau){
                    count += 1;
                    niveau.setVisibility(View.GONE);
                }
                else{
                    niveau.setVisibility(View.VISIBLE);
                }

                if(checkGlobal){
                    count += 1;
                    global.setVisibility(View.GONE);
                }
                else{
                    global.setVisibility(View.VISIBLE);
                }

                if ((count == 9 && !autreCheckBox.isChecked()) || (count == 10 && autreCheckBox.isChecked())) {
                    saveData();
                    //finish();

                    Intent intent = getIntent();
                    stringPhoto = intent.getStringExtra("photo1");
                    String paths = intent.getStringExtra("path");

                    EspaceBoise espaceBoise = new EspaceBoise(
                            stringTextNomPrenom,
                            stringTextPseudo,
                            stringTextMail,
                            editTextLatitude.getText().toString().trim(),
                            editTextLongitude.getText().toString().trim(),
                            stringTextAdresse,
                            stringPhoto,
                            getEspace(),
                            stringTextObservations,
                            getArbresNb(),
                            getAnswer(spinnerNbEspeces),
                            getNiveau(),
                            getAnswer(spinnerEau),
                            getAnswer(spinnerAbris),
                            getAnswer(spinnerEclairage),
                            getBiodiv(),
                            stringAutreBiodiversite,
                            getAnswer(spinnerOmbre),
                            getEntretien(),
                            getGlobal());

                    espaceBoise.CreateCsv(paths);

                    //Zip le CSV + Photo
                    ZipName = paths + "reponse_appli_EB_" + stringPhoto.replace("JPEG_","").replace(".jpg","")+".zip";
                    String []s=new String[2];
                    s[0]=paths+stringPhoto;
                    s[1]=paths+"reponse_"+ stringPhoto.replace("JPEG_","").replace(".jpg","")+".csv";
                    zip(s,ZipName);

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
        stringTextNomPrenom = editTextNomPrenom.getText().toString();
        stringTextPseudo = editTextPseudo.getText().toString();
        stringTextMail = editTextAdresseMail.getText().toString();
        stringTextAdresse = editTextAdresseEsp.getText().toString();
        stringEspace = spinnerTypeEspace.getSelectedItem().toString();
        stringNombresArbres = spinnerNbArbres.getSelectedItem().toString();
        stringNombresEspeces = spinnerNbEspeces.getSelectedItem().toString();
        stringNiveau = getNiveau();
        stringBiodiv = getBiodiv();
        stringGlobal = getGlobal();
        stringEau = spinnerEau.getSelectedItem().toString();
        stringAbris = spinnerAbris.getSelectedItem().toString();
        stringEclairage = spinnerEclairage.getSelectedItem().toString();
        stringOmbre = spinnerOmbre.getSelectedItem().toString();
        stringEntretien = spinnerEntretien.getSelectedItem().toString();
        stringTextObservations = editTextObservations.getText().toString();

        DialogEspaceBoise dialog = new DialogEspaceBoise(stringTextNomPrenom, stringTextPseudo, stringTextMail, stringTextAdresse ,stringEspace, stringNombresArbres, stringNombresEspeces,stringNiveau, stringEau, stringAbris, stringEclairage, stringBiodiv, stringOmbre, stringEntretien, stringGlobal, stringTextObservations);
        dialog.show(getSupportFragmentManager(), "Dialog AjoutEspaceBoise");
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

    private String getNiveau(){
        String txt = "* ";
        if(checkBoxArbre.isChecked()){
            txt += "arbres *";
        }
        if(checkBoxArbuste.isChecked()){
            txt += "arbustes *";
        }
        if(checkBoxHerbe.isChecked()){
            txt += "herbes *";
        }
        return txt.substring(0,txt.lastIndexOf(" *"));
    }

    private String getBiodiv(){
        String txt = "* ";
        if(checkBoxEcureuil.isChecked()){
            txt += "écureuil *";
        }
        if(checkBoxChauve.isChecked()){
            txt += "chauve-souris *";
        }
        if(checkBoxCapricorne.isChecked()){
            txt += "capricorne *";
        }
        if(checkBoxChouette.isChecked()){
            txt += "chouette *";
        }
        if(checkBoxPic.isChecked()){
            txt += "pic *";
        }
        if(autreCheckBox.isChecked()){
            txt += "autre *";
        }
        return txt.substring(0,txt.lastIndexOf(" *"));
    }

    private String getGlobal(){
        String txt = "* ";
        if(checkBoxRefuge.isChecked()){
            txt += "biodiv *";
        }
        if(checkBoxIlot.isChecked()){
            txt += "fraicheur *";
        }
        if(checkBoxPaysager.isChecked()){
            txt += "paysager *";
        }
        return txt.substring(0,txt.lastIndexOf(" *"));
    }

    private String getEspace(){
        String txt = "";
        switch (spinnerTypeEspace.getSelectedItem().toString())
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

    private String getArbresNb(){
        String txt = "";
        switch (spinnerNbArbres.getSelectedItem().toString())
        {
            case "Entre 2 et 10":
                txt = "2";
                break;
            case "Entre 10 et 50":
                txt = "10";
                break;
            case "Plus de 50":
                txt = "50";
                break;
        }
        return txt;
    }

    private String getAnswer(Spinner spinner){
        String txt = "";
        switch (spinner.getSelectedItem().toString())
        {
            case "Oui":
                txt = "oui";
                break;
            case "Non":
                txt = "non";
                break;
            case "Je ne sais pas":
                txt = "Inconnu";
                break;
        }
        return txt;
    }

    private String getEntretien(){
        String txt = "";
        switch (spinnerEntretien.getSelectedItem().toString())
        {
            case "Très entretenu (tontes fréquentes, massifs entretenus…)":
                txt = "beaucoup";
                break;
            case "Entretenu mais de manière plus douce (zones non fauchées…)":
                txt = "moyen";
                break;
            case "Très peu ou pas du tout entretenu (pas d’entretien mécanisé, herbes hautes…) ":
                txt = "peu";
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
