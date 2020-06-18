package com.esaip.arbresremarquables.Dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.esaip.arbresremarquables.R;

public class DialogAbout extends AppCompatDialogFragment {
    private WebView webView;

    public DialogAbout() {
    }

    @NonNull
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.layout_dialog_about, null);
        WebView myWebView = view.findViewById(R.id.webview);
        myWebView.loadUrl("https://www.sauvegarde-anjou.org/arbres1/spip.php?rubrique5");


        builder.setView(view)
                //.setTitle("Récapitulatif")
                .setNeutralButton("Retour", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        return builder.create();
    }
}
