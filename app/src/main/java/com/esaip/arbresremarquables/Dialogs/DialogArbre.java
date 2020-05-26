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


    public DialogArbre(String textNomPrenom, String textPseudo, String textEmail, String textNomArbre, String textAdresseArbre, String textEspace, String textRemarquable, String textObservations, Boolean boolVerification) {
        this.textNomPrenom = textNomPrenom;
        this.textPseudo = textPseudo;
        this.textEmail = textEmail;
        this.textNomArbre = textNomArbre;
        this.textAdresseArbre = textAdresseArbre;
        this.textEspace = textEspace;
        this.textRemarquable = textRemarquable;
        this.textObservations = textObservations;
        this.boolVerification = boolVerification;
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

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
        textDialog_NomArbre.setText(textNomArbre);
        textDialog_AdresseArbre.setText(textAdresseArbre);
        textDialog_Remarquable.setText(textRemarquable);
        textDialog_Observations.setText(textObservations);
        textDialog_Espace.setText(textEspace);
        if (boolVerification)
            textDialog_Verification.setText("Informations vérifiées sur site par un botaniste");

        return builder.create();
    }
}
