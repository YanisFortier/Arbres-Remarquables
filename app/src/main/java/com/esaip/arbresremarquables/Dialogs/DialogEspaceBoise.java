package com.esaip.arbresremarquables.Dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.esaip.arbresremarquables.MapsActivity;
import com.esaip.arbresremarquables.R;

import org.w3c.dom.Text;

public class DialogEspaceBoise extends AppCompatDialogFragment {
    private TextView textDialog_NomPrenom,textDialog_Pseudo, textDialog_Email, textDialog_AdresseEsp, textDialog_Espace,textDialog_NombreArbre, textDialog_NombreEspece, textDialog_Niveau, textDialog_Eau, textDialog_Abris, textDialog_Eclairage, textDialog_Biodiversite, textDialog_Ombre, textDialog_Entretien, textDialog_Global, textDialog_Observations ;

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

    public DialogEspaceBoise(String textNomPrenom, String textPseudo, String textEmail, String textAdresseEsp, String textEspace, String textNombreArbre, String textNombreEspece, String textNiveau, String textEau, String textAbri, String textEclairage, String textBiodiversite, String textOmbre, String textEntretien, String textGlobal, String textObservations) {
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
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_espace_boise, null);
        textDialog_NomPrenom = view.findViewById(R.id.textDialog_NomPrenom);
        textDialog_Pseudo = view.findViewById(R.id.textDialog_Pseudo);
        textDialog_Email = view.findViewById(R.id.textDialog_Email);
        textDialog_AdresseEsp = view.findViewById(R.id.textDialog_AdresseEspace);
        textDialog_Espace = view.findViewById(R.id.textDialog_EspaceEspace);
        textDialog_NombreArbre = view.findViewById(R.id.textDialog_NombreArbre);
        textDialog_NombreEspece = view.findViewById(R.id.textDialog_NombreEspece);
        textDialog_Niveau = view.findViewById(R.id.textDialog_Niveau);
        textDialog_Eau = view.findViewById(R.id.textDialog_Eau);
        textDialog_Abris = view.findViewById(R.id.textDialog_Abris);
        textDialog_Eclairage = view.findViewById(R.id.textDialog_Eclairage);
        textDialog_Biodiversite = view.findViewById(R.id.textDialog_Biodiv);
        textDialog_Ombre = view.findViewById(R.id.textDialog_Ombre);
        textDialog_Entretien = view.findViewById(R.id.textDialog_Entretien);
        textDialog_Global = view.findViewById(R.id.textDialog_Global);
        textDialog_Observations = view.findViewById(R.id.textDialog_Obervations);

        builder.setView(view)
                .setTitle("Récapitulatif")
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Merci de votre contribution :)", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(getActivity(), MapsActivity.class));
                    }
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
