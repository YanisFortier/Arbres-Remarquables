package com.esaip.arbresremarquables.Dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

public class DialogAlignement extends AppCompatDialogFragment {
    private TextView textDialog_NomPrenom;
    private TextView textDialog_Pseudo;
    private TextView textDialog_Email;
    private TextView textDialog_AdresseAlignement;
    private TextView textDialog_Espace;
    private TextView textDialog_NombreArbre;
    private TextView textDialog_NombreEspece;
    private TextView textDialog_Especes;
    private TextView textDialog_Lien;
    private TextView textDialog_Protection;
    private TextView textDialog_Observations;
    private TextView textDialog_Verification;

    private String textNomPrenom;
    private String textPseudo;
    private String textEmail;
    private String textAdresseAlignement;
    private String textEspace;
    private String textNombreArbre;
    private String textNombreEspece;
    private String textEspeces;
    private String textLien;
    private String textProtection;
    private String textObservations;
    private Boolean textVerification;

    public DialogAlignement(String textNomPrenom, String textPseudo, String textEmail, String textAdresseAlignement, String textEspace, String textNombreArbre, String textNombreEspece, String textEspeces, String textLien, String textProtection, String textObservations, Boolean textVerification) {
        this.textNomPrenom = textNomPrenom;
        this.textPseudo = textPseudo;
        this.textEmail = textEmail;
        this.textAdresseAlignement = textAdresseAlignement;
        this.textEspace = textEspace;
        this.textNombreArbre = textNombreArbre;
        this.textNombreEspece = textNombreEspece;
        this.textEspeces = textEspeces;
        this.textLien = textLien;
        this.textProtection = textProtection;
        this.textObservations = textObservations;
        this.textVerification = textVerification;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_alignement, null);
        textDialog_NomPrenom = view.findViewById(R.id.textDialog_NomPrenom);
        textDialog_Pseudo = view.findViewById(R.id.textDialog_Pseudo);
        textDialog_Email = view.findViewById(R.id.textDialog_Email);
        textDialog_AdresseAlignement = view.findViewById(R.id.textDialog_AdresseAlignement);
        textDialog_Espace = view.findViewById(R.id.textDialog_EspaceAlignement);
        textDialog_NombreArbre = view.findViewById(R.id.textDialog_NombreArbre);
        textDialog_NombreEspece = view.findViewById(R.id.textDialog_NombreEspece);
        textDialog_Especes = view.findViewById(R.id.textDialog_Especes);
        textDialog_Lien = view.findViewById(R.id.textDialog_Lien);
        textDialog_Protection = view.findViewById(R.id.textDialog_Protection);
        textDialog_Observations = view.findViewById(R.id.textDialog_Obervations);
        textDialog_Verification = view.findViewById(R.id.textDialog_Verification);

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
        textDialog_AdresseAlignement.setText(textAdresseAlignement);
        textDialog_Espace.setText(textEspace);
        textDialog_NombreArbre.setText(textNombreArbre);
        textDialog_NombreEspece.setText(textNombreEspece);
        textDialog_Especes.setText(textEspeces);
        textDialog_Lien.setText(textLien);
        textDialog_Protection.setText(textProtection);
        textDialog_Observations.setText(textObservations);


        //if(boolVerification)
        //  textDialog_Verification.setText("Informations vérifiées sur site par un botaniste");

        return builder.create();
    }
}
