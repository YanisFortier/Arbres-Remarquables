package com.esaip.arbresremarquables.Dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
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

public class DialogArbre extends AppCompatDialogFragment {
    private String textNomPrenom;
    private String textPseudo;
    private String textEmail;
    private String textNomArbre;
    private String textAdresseArbre;
    private String textEspace;
    private String textRemarquable;
    private String textObservations;
    private Boolean boolVerification;
    private String zipPath;


    public DialogArbre(String textNomPrenom, String textPseudo, String textEmail, String textNomArbre, String textAdresseArbre, String textEspace, String textRemarquable, String textObservations, Boolean boolVerification, String zipPath) {
        this.textNomPrenom = textNomPrenom;
        this.textPseudo = textPseudo;
        this.textEmail = textEmail;
        this.textNomArbre = textNomArbre;
        this.textAdresseArbre = textAdresseArbre;
        this.textEspace = textEspace;
        this.textRemarquable = textRemarquable;
        this.textObservations = textObservations;
        this.boolVerification = boolVerification;
        this.zipPath = zipPath;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_arbre, null);
        TextView textDialog_NomPrenom = view.findViewById(R.id.textDialog_NomPrenom);
        TextView textDialog_Pseudo = view.findViewById(R.id.textDialog_Pseudo);
        TextView textDialog_Email = view.findViewById(R.id.textDialog_Email);
        TextView textDialog_Espace = view.findViewById(R.id.textDialog_Espace);
        TextView textDialog_NomArbre = view.findViewById(R.id.textDialog_NomArbre);
        TextView textDialog_AdresseArbre = view.findViewById(R.id.textDialog_AdresseArbre);
        TextView textDialog_Remarquable = view.findViewById(R.id.textDialog_Remarquable);
        TextView textDialog_Observations = view.findViewById(R.id.textDialog_Obervations);
        TextView textDialog_Verification = view.findViewById(R.id.textDialog_Verification);

        builder.setView(view)
                .setTitle("Récapitulatif")
                .setNegativeButton("Annuler", (dialog, which) -> {

                })
                .setPositiveButton("Valider", (dialog, which) -> {
                    Toast.makeText(getActivity(), "Envoi sur le site en cours...", Toast.LENGTH_LONG).show();

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
                    //String urlSardine = "https://www.webdavserver.com/User91245fe/";
                    String urlSardine = "https://nuage.sauvegarde-anjou.org/remote.php/dav/files/invitesaip/";
                    try {
                        Log.e("Zip", String.valueOf(fichierZip.length() / 1024));
                        sardine.put(urlSardine + fichierZip.getName(), fichierZip, "application/zip");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(getActivity(), MapsActivity.class));
                    Toast.makeText(getActivity(), "Merci de votre contribution :)", Toast.LENGTH_LONG).show();
                });

        textDialog_NomPrenom.setText(textNomPrenom);
        textDialog_Pseudo.setText(textPseudo);
        textDialog_Email.setText(textEmail);
        textDialog_NomArbre.setText(textNomArbre);
        textDialog_AdresseArbre.setText(textAdresseArbre);
        textDialog_Remarquable.setText(textRemarquable);
        textDialog_Observations.setText(textObservations);
        textDialog_Espace.setText(textEspace);
        if (boolVerification)
            textDialog_Verification.setText(R.string.textInfoBotaniste);

        return builder.create();
    }
}
