package com.esaip.arbresremarquables.Formulaires;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.esaip.arbresremarquables.Dialogs.DialogAlignement;
import com.esaip.arbresremarquables.Models.Alignement;
import com.esaip.arbresremarquables.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class AjoutAlignement extends AppCompatActivity {

    //Variables pour la sauvegarde utilisateur
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TEXT_NOM_PRENOM = "NOM_PRENOM";
    public static final String TEXT_ADRESSE_MAIL = "ADRESSE_MAIL";
    public static final String TEXT_PSEUDO = "PSEUDO";

    //Variables Layout
    private LinearLayout autre, autreLien;
    private TextView errorEspece, errorLien;
    private EditText editTextLatitude, editTextLongitude, editTextNomPrenom, editTextPseudo, editTextAdresseMail, editTextAdresseAlignement, editTextAutreEspece, editTextNomBotanique, editTextObservations, editTextAutreLien;
    private Spinner spinnerEspace, spinnerNombreArbre, spinnerNombreEspece, spinnerProtection;
    private CheckBox checkBoxEspeceAutre, checkBoxLienAutre, checkboxVerification, checkBoxChene, checkBoxFrene, checkBoxPeuplier, checkBoxPin, checkBoxCedre, checkBoxErable, checkBoxSequoia, checkBoxPlatane, checkBoxMarronnier, checkBoxChataignier, checkBoxHetre, checkBoxMagnolia, checkBoxTilleul, checkBoxEspaceBoise, checkBoxParc, checkBoxAutreAli;
    private String stringTextNomPrenom, stringTextPseudo, stringTextObservations, stringTextMail, stringTextAdresse, stringLatitude, stringLongitude, stringNomBotanique, stringPhoto;
    private String stringAutreEspece = "*Sans réponse*", stringAutreLien = "*Sans réponse*";
    private String zipPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout_alignement);

        //Setup - FindViewById
        autre = findViewById(R.id.editAutre);
        autreLien = findViewById(R.id.editAutreLien);
        editTextNomPrenom = findViewById(R.id.editTextNomPrenom);
        editTextAdresseMail = findViewById(R.id.editTextAdresseMail);
        editTextPseudo = findViewById(R.id.editTextPseudo);
        editTextAdresseAlignement = findViewById(R.id.editTextAdresseAlignement);
        editTextAutreEspece = findViewById(R.id.editTextAutreEspece);
        editTextNomBotanique = findViewById(R.id.editTextNomBotanique);
        editTextAutreLien = findViewById(R.id.editTextAutreLien);
        editTextObservations = findViewById(R.id.editTextObservation);
        editTextLongitude = findViewById(R.id.editTextLongitudeAli);
        editTextLatitude = findViewById(R.id.editTextLatitudeAli);
        spinnerEspace = findViewById(R.id.spinnerEspace);
        spinnerNombreArbre = findViewById(R.id.spinnerNombresArbres);
        spinnerNombreEspece = findViewById(R.id.spinnerNombresEspeces);
        spinnerProtection = findViewById(R.id.spinnerProtection);
        checkBoxEspeceAutre = findViewById(R.id.checkBoxEspeceAutre);
        checkBoxLienAutre = findViewById(R.id.checkBoxLienAutre);
        checkboxVerification = findViewById(R.id.checkBoxVerification);
        checkBoxChene = findViewById(R.id.checkBoxChene);
        checkBoxFrene = findViewById(R.id.checkBoxFrene);
        checkBoxPeuplier = findViewById(R.id.checkBoxPeuplier);
        checkBoxPin = findViewById(R.id.checkBoxPin);
        checkBoxCedre = findViewById(R.id.checkBoxCèdre);
        checkBoxErable = findViewById(R.id.checkBoxErable);
        checkBoxSequoia = findViewById(R.id.checkBoxSéquoia);
        checkBoxPlatane = findViewById(R.id.checkBoxPlatane);
        checkBoxMarronnier = findViewById(R.id.checkBoxMarronnier);
        checkBoxChataignier = findViewById(R.id.checkBoxChataignier);
        checkBoxHetre = findViewById(R.id.checkBoxHetre);
        checkBoxMagnolia = findViewById(R.id.checkBoxMagniolia);
        checkBoxTilleul = findViewById(R.id.checkBoxTilleul);
        checkBoxEspaceBoise = findViewById(R.id.checkBoxEspaceBoise);
        checkBoxParc = findViewById(R.id.checkBoxParcs);
        checkBoxAutreAli = findViewById(R.id.checkBoxAutresAlignement);
        errorEspece = findViewById(R.id.errorEspece);
        errorLien = findViewById(R.id.errorLien);
        Button buttonValid = findViewById(R.id.buttonValiderAli);

        //Ajout de la géolocalisation
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            //Récupération des coordonnées depuis le bundle
            Double latitude_arbre = bundle.getDouble("latitude_arbre");
            Double longitude_arbre = bundle.getDouble("longitude_arbre");
            //Format à 7 décimales
            @SuppressLint("DefaultLocale") String latitude = String.format("%.7f", latitude_arbre).replace(",", ".");
            @SuppressLint("DefaultLocale") String longitude = String.format("%.7f", longitude_arbre).replace(",", ".");
            //Ouput
            editTextLatitude.setText(latitude);
            editTextLongitude.setText(longitude);
        }

        checkBoxEspeceAutre.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                autre.setVisibility(View.VISIBLE);
                editTextAutreEspece.setText("");
                editTextNomBotanique.setText("");
            } else {
                autre.setVisibility(View.GONE);
            }
        });

        checkBoxLienAutre.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                autreLien.setVisibility(View.VISIBLE);
                editTextAutreLien.setText("");
            } else {
                autreLien.setVisibility(View.GONE);
            }
        });

        buttonValid.setOnClickListener(v -> {
            stringTextNomPrenom = editTextNomPrenom.getText().toString().trim();
            stringTextPseudo = editTextPseudo.getText().toString().trim();
            stringTextMail = editTextAdresseMail.getText().toString().trim();
            stringTextAdresse = editTextAdresseAlignement.getText().toString().trim();
            stringTextObservations = editTextObservations.getText().toString().trim();
            stringLongitude = editTextLongitude.getText().toString();
            stringLatitude = editTextLatitude.getText().toString();
            boolean checkEspece = (checkBoxChene.isChecked() || checkBoxFrene.isChecked() || checkBoxPeuplier.isChecked() || checkBoxPin.isChecked() || checkBoxCedre.isChecked() || checkBoxErable.isChecked() || checkBoxSequoia.isChecked() || checkBoxPlatane.isChecked() || checkBoxMarronnier.isChecked() || checkBoxChataignier.isChecked() || checkBoxHetre.isChecked() || checkBoxMagnolia.isChecked() || checkBoxTilleul.isChecked() || checkBoxEspeceAutre.isChecked());
            boolean checkLien = (checkBoxEspaceBoise.isChecked() || checkBoxParc.isChecked() || checkBoxAutreAli.isChecked() || checkBoxLienAutre.isChecked());
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
                editTextAdresseAlignement.setError("Ce champ est obligatoire");
            } else if (!checkPatternAdresse(stringTextAdresse)) {
                editTextAdresseAlignement.setError("Adresse non valide");
            } else {
                count += 1;
            }

            if (checkBoxEspeceAutre.isChecked()) {
                stringAutreEspece = editTextAutreEspece.getText().toString().trim();
                stringNomBotanique = editTextNomBotanique.getText().toString().trim();
                if (!stringNomBotanique.isEmpty() && !checkPatternGeneral(stringNomBotanique)) {
                    editTextNomBotanique.setError("Nom non valide");
                } else {
                    count += 1;
                }

                if (stringAutreEspece.isEmpty()) {
                    editTextAutreEspece.setError("Ce champ est obligatoire");
                } else if (!checkPatternGeneral(stringAutreEspece)) {
                    editTextAutreEspece.setError("Nom non valide");
                } else {
                    count += 1;
                }
            } else stringNomBotanique = "";

            if (checkBoxLienAutre.isChecked()) {
                stringAutreLien = editTextAutreLien.getText().toString().trim();
                if (stringAutreLien.isEmpty()) {
                    editTextAutreLien.setError("Ce champ est obligatoire");
                } else if (!checkPatternGeneral(stringAutreLien)) {
                    editTextAutreLien.setError("Nom non valide");
                } else {
                    count += 1;
                }
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

            if (checkEspece) {
                count += 1;
                errorEspece.setVisibility(View.GONE);
            } else {
                errorEspece.setVisibility(View.VISIBLE);
            }

            if (checkLien) {
                count += 1;
                errorLien.setVisibility(View.GONE);
            } else {
                errorLien.setVisibility(View.VISIBLE);
            }


            if ((count == 9 && !checkBoxEspeceAutre.isChecked() && !checkBoxLienAutre.isChecked()) || (count == 10 && !checkBoxEspeceAutre.isChecked() && checkBoxLienAutre.isChecked()) || (count == 11 && checkBoxEspeceAutre.isChecked() && !checkBoxLienAutre.isChecked()) || (count == 12 && checkBoxEspeceAutre.isChecked() && checkBoxLienAutre.isChecked())) {

                //finish();

                Intent intent = getIntent();
                stringPhoto = intent.getStringExtra("photo1");
                String paths = intent.getStringExtra("path");

                boolean verification = false;
                if (checkboxVerification.isChecked()) verification = true;

                String espece = getCheckBoxs();
                String lien = getLien();

                Alignement alignement = new Alignement(
                        stringTextNomPrenom,
                        stringTextPseudo,
                        stringTextMail,
                        editTextLatitude.getText().toString().trim(),
                        editTextLongitude.getText().toString().trim(),
                        getEspace(),
                        stringTextAdresse,
                        stringPhoto,
                        stringTextObservations,
                        getArbresNb(),
                        getEspecesNb(),
                        espece,
                        stringAutreEspece,
                        stringNomBotanique,
                        lien,
                        stringAutreLien,
                        getProtection(),
                        verification);

                alignement.CreateCsv(paths);

                //Zip le CSV + Photo
                zipPath = paths + "reponse_appli_AL_" + stringPhoto.replace("JPEG_", "").replace(".jpg", "") + ".zip";
                String[] s = new String[2];
                s[0] = paths + stringPhoto;
                s[1] = paths + "reponse_" + stringPhoto.replace("JPEG_", "").replace(".jpg", "") + ".csv";
                zip(s, zipPath);

                saveData();
            } else {
                Toast.makeText(AjoutAlignement.this, "Champs incorrects ou manquants, veuillez remplir toutes les informations nécessaires", Toast.LENGTH_LONG).show();
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
        stringTextAdresse = editTextAdresseAlignement.getText().toString();
        String espace = spinnerEspace.getSelectedItem().toString();
        String nombresArbres = spinnerNombreArbre.getSelectedItem().toString();
        String nombresEspeces = spinnerNombreEspece.getSelectedItem().toString();
        String especes;
        String lien;
        String protection = spinnerProtection.getSelectedItem().toString();
        stringTextObservations = editTextObservations.getText().toString();

        boolean verification = false;
        if (checkboxVerification.isChecked())
            verification = true;

        especes = getCheckBoxs();

        lien = getLien();

        DialogAlignement dialog = new DialogAlignement(stringTextNomPrenom, stringTextPseudo, stringTextMail, stringTextAdresse, espace, nombresArbres, nombresEspeces, especes, lien, protection, stringTextObservations, verification, zipPath);
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

    private String getEspace() {
        String txt = "";
        switch (spinnerEspace.getSelectedItem().toString()) {
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

    private String getCheckBoxs() {
        String txt = "* ";
        if (checkBoxChene.isChecked()) {
            txt += "Chêne *";
        }
        if (checkBoxFrene.isChecked()) {
            txt += "Frêne *";
        }
        if (checkBoxPeuplier.isChecked()) {
            txt += "Peuplier *";
        }
        if (checkBoxPin.isChecked()) {
            txt += "Pin *";
        }
        if (checkBoxCedre.isChecked()) {
            txt += "Cèdre *";
        }
        if (checkBoxErable.isChecked()) {
            txt += "Érable *";
        }
        if (checkBoxSequoia.isChecked()) {
            txt += "Séquoia *";
        }
        if (checkBoxPlatane.isChecked()) {
            txt += "Platane *";
        }
        if (checkBoxMarronnier.isChecked()) {
            txt += "Marronnier *";
        }
        if (checkBoxChataignier.isChecked()) {
            txt += "Châtaignier *";
        }
        if (checkBoxHetre.isChecked()) {
            txt += "Hêtre *";
        }
        if (checkBoxMagnolia.isChecked()) {
            txt += "Magnolia *";
        }
        if (checkBoxTilleul.isChecked()) {
            txt += "Tilleul *";
        }
        if (checkBoxEspeceAutre.isChecked()) {
            txt += "autre *";
        }
        return txt.substring(0, txt.lastIndexOf(" *"));
    }

    private String getLien() {
        String txt = "* ";
        if (checkBoxEspaceBoise.isChecked()) {
            txt += "EB *";
        }
        if (checkBoxParc.isChecked()) {
            txt += "Parcs *";
        }
        if (checkBoxAutreAli.isChecked()) {
            txt += "alignement *";
        }
        if (checkBoxLienAutre.isChecked()) {
            txt += "autre *";
        }
        return txt.substring(0, txt.lastIndexOf(" *"));
    }

    private String getArbresNb() {
        String txt = "";
        switch (spinnerNombreArbre.getSelectedItem().toString()) {
            case "Moins de 10":
                txt = "Moins10";
                break;
            case "Entre 10 et 30":
                txt = "10a30";
                break;
            case "Plus de 30":
                txt = "plus30";
                break;
        }
        return txt;
    }

    private String getEspecesNb() {
        String txt = "";
        switch (spinnerNombreEspece.getSelectedItem().toString()) {
            case "Une":
                txt = "1";
                break;
            case "2":
                txt = "10a30";
                break;
            case "3":
                txt = "plus30";
                break;
            case "Plus de trois":
                txt = "plus3";
                break;
        }
        return txt;
    }

    private String getProtection() {
        String txt = "";
        switch (spinnerProtection.getSelectedItem().toString()) {
            case "Il relie d’autres espaces végétalisés entre eux (continuité écologique)":
                txt = "continuité";
                break;
            case "Il est impactant au niveau paysager":
                txt = "paysager";
                break;
            case "Il est très utile en cas de forte chaleur (ombre sur façade…) ":
                txt = "fraicheur";
                break;
            case "Il est composé d’arbres remarquables ":
                txt = "remarquable";
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
    private Boolean checkPatternMail(String txt) {
        Pattern MAIL = Pattern.compile("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$");
        return MAIL.matcher(txt).matches();
    }

    private Boolean checkPatternGeneral(String txt) {
        Pattern REG1 = Pattern.compile("^([A-Za-zâäèéêëîïôöûüñç ]+)(\\-?[A-Za-zâäèéêëîïôöûüñç ]+)*$");
        return REG1.matcher(txt).matches();
    }

    private Boolean checkPatternPseudo(String txt) {
        Pattern PSEUDO = Pattern.compile("^([A-zâäèéêëîïôöûüñç\\-\\d ])+$");
        return PSEUDO.matcher(txt).matches();
    }

    private Boolean checkPatternAdresse(String txt) {
        Pattern ADRESSE = Pattern.compile("^([A-Za-zâäèéêëîïôöûüñç\\-\\d ,])+[']?([A-Za-zâäèéêëîïôöûüñç\\-\\d ,])*$");
        return ADRESSE.matcher(txt).matches();
    }

    private Boolean checkPatternObervations(String txt) {
        Pattern OBSERVATIONS = Pattern.compile("^(([A-Za-zâäàèéêëîïôöûüùñç\\-\\d ])+[']?([A-Za-zâäàèéêëîïôöûüùñç\\-\\d ])*([,\\.;/!:?()\\[\\]])*)+$");
        return OBSERVATIONS.matcher(txt).matches();
    }

    private Boolean checkLatitude(String txt) {
        Pattern LATITUDE = Pattern.compile("^(\\+|-)?(?:90(?:(?:\\.0{1,8})?)|(?:[0-9]|[1-8][0-9])(?:(?:\\.[0-9]{1,8})?))$");
        return LATITUDE.matcher(txt).matches();
    }

    private Boolean checkLongitude(String txt) {
        Pattern LONGITUDE = Pattern.compile("^(\\+|-)?(?:180(?:(?:\\.0{1,8})?)|(?:[0-9]|[1-9][0-9]|1[0-7][0-9])(?:(?:\\.[0-9]{1,8})?))$");
        return LONGITUDE.matcher(txt).matches();
    }
}
