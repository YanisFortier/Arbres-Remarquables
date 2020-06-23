package com.esaip.arbresremarquables.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.esaip.arbresremarquables.MapsActivity;
import com.esaip.arbresremarquables.R;
import com.thegrizzlylabs.sardineandroid.Sardine;
import com.thegrizzlylabs.sardineandroid.impl.OkHttpSardine;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class DialogEspaceBoise extends AppCompatDialogFragment {
    private String textNomPrenom;
    private String textPseudo;
    private String textEmail;
    private String textAdresseEsp;
    private String textEspace;
    private String textNombreArbre;
    private String textNombreEspece;
    private String textNiveau;
    private String textEau;
    private String textAbri;
    private String textEclairage;
    private String textBiodiversite;
    private String textOmbre;
    private String textEntretien;
    private String textGlobal;
    private String textObservations;
    private String zipPath;

    public DialogEspaceBoise(String textNomPrenom, String textPseudo, String textEmail, String textAdresseEsp, String textEspace, String textNombreArbre, String textNombreEspece, String textNiveau, String textEau, String textAbri, String textEclairage, String textBiodiversite, String textOmbre, String textEntretien, String textGlobal, String textObservations, String zipPath) {
        this.textNomPrenom = textNomPrenom;
        this.textPseudo = textPseudo;
        this.textEmail = textEmail;
        this.textAdresseEsp = textAdresseEsp;
        this.textEspace = textEspace;
        this.textNombreArbre = textNombreArbre;
        this.textNombreEspece = textNombreEspece;
        this.textNiveau = textNiveau;
        this.textEau = textEau;
        this.textAbri = textAbri;
        this.textEclairage = textEclairage;
        this.textBiodiversite = textBiodiversite;
        this.textOmbre = textOmbre;
        this.textEntretien = textEntretien;
        this.textGlobal = textGlobal;
        this.textObservations = textObservations;
        this.zipPath = zipPath;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_espace_boise, null);
        TextView textDialog_NomPrenom = view.findViewById(R.id.textDialog_NomPrenom);
        TextView textDialog_Pseudo = view.findViewById(R.id.textDialog_Pseudo);
        TextView textDialog_Email = view.findViewById(R.id.textDialog_Email);
        TextView textDialog_AdresseEsp = view.findViewById(R.id.textDialog_AdresseEspace);
        TextView textDialog_Espace = view.findViewById(R.id.textDialog_EspaceEspace);
        TextView textDialog_NombreArbre = view.findViewById(R.id.textDialog_NombreArbre);
        TextView textDialog_NombreEspece = view.findViewById(R.id.textDialog_NombreEspece);
        TextView textDialog_Niveau = view.findViewById(R.id.textDialog_Niveau);
        TextView textDialog_Eau = view.findViewById(R.id.textDialog_Eau);
        TextView textDialog_Abris = view.findViewById(R.id.textDialog_Abris);
        TextView textDialog_Eclairage = view.findViewById(R.id.textDialog_Eclairage);
        TextView textDialog_Biodiversite = view.findViewById(R.id.textDialog_Biodiv);
        TextView textDialog_Ombre = view.findViewById(R.id.textDialog_Ombre);
        TextView textDialog_Entretien = view.findViewById(R.id.textDialog_Entretien);
        TextView textDialog_Global = view.findViewById(R.id.textDialog_Global);
        TextView textDialog_Observations = view.findViewById(R.id.textDialog_Obervations);

        builder.setView(view)
                .setTitle("Récapitulatif")
                .setNegativeButton("Annuler", (dialog, which) -> {

                })
                .setPositiveButton("Valider", (dialog, which) -> {
                    Toast.makeText(getActivity(), "Merci de votre contribution :)", Toast.LENGTH_LONG).show();
                    //Upload
                    //Gestion Asynchrone
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient.Builder builder1 = new OkHttpClient.Builder();
                    builder1.connectTimeout(5, TimeUnit.MINUTES) // connect timeout
                            .writeTimeout(5, TimeUnit.MINUTES) // write timeout
                            .readTimeout(5, TimeUnit.MINUTES); // read timeout

                    //Client sardine
                    File fichierZip = new File(zipPath);

                    Sardine sardine = new OkHttpSardine();
                    sardine.setCredentials("invitesaip", "Hg6ykLuvZBk");
                    String urlSardine = "https://www.webdavserver.com/User91245fe/";
                    //String urlSardine = "https://nuage.sauvegarde-anjou.org/remote.php/dav/files/invitesaip/";
                    try {
                        sardine.put(urlSardine + fichierZip.getName(), fichierZip, "application/zip");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(), MapsActivity.class));
                });

        textDialog_NomPrenom.setText(textNomPrenom);
        textDialog_Pseudo.setText(textPseudo);
        textDialog_Email.setText(textEmail);
        textDialog_AdresseEsp.setText(textAdresseEsp);
        textDialog_Espace.setText(textEspace);
        textDialog_NombreArbre.setText(textNombreArbre);
        textDialog_NombreEspece.setText(textNombreEspece);
        textDialog_Niveau.setText(textNiveau);
        textDialog_Eau.setText(textEau);
        textDialog_Abris.setText(textAbri);
        textDialog_Eclairage.setText(textEclairage);
        textDialog_Biodiversite.setText(textBiodiversite);
        textDialog_Ombre.setText(textOmbre);
        textDialog_Entretien.setText(textEntretien);
        textDialog_Global.setText(textGlobal);
        textDialog_Observations.setText(textObservations);

        return builder.create();
    }
}
