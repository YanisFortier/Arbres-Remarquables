package com.esaip.arbresremarquables.Models;

import android.os.Environment;

import com.esaip.arbresremarquables.Csv;
import java.util.ArrayList;
import java.util.List;


public class Arbre extends generique {
    private String nomArbre;
    private String autreArbre;
    private String nomBotanique;
    private String espace;
    private String remarquable;
    private String verification;

   public Arbre(){
       super();
   }

    public Arbre(String nomPrenom, String pseudo, String mail, String latitude, String longitude, String adresseArbre, String photo, String observations,String nomArbre, String autreArbre, String nomBotanique, String espace, String remarquable, String verification) {

       //Récup les infos de l'héritage (générique).
        super(nomPrenom,pseudo,mail,latitude,longitude,adresseArbre,photo,observations);

        this.nomArbre = nomArbre;
        this.autreArbre = autreArbre;
        this.nomBotanique = nomBotanique;
        this.espace = espace;
        this.remarquable = remarquable;
        this.verification = verification;
    }

    public void CreateCsv(String path){

        List<String[]> data= new ArrayList<String[]>();
        //Liste de données que contiendra mon CSV (Dans l'ordre Haut-Bas équivalent Gauche-Droit)
        //En tête
        data.add(new String[]{
                "Id_Reponse",
                "Date",
                "Nom Prenom",
                "Pseudo",
                "Mail",
                "Nom de l'arbre",
                "Autre arbres",
                "Nom botannique",
                "Latitude",
                "Longitude",
                "Adresse Arbre",
                "Photo",
                "Espace",
                "Autre raison",
                "Biodiversité",
                "Espace protégé",
                "Remarquable",
                "Observations",
                "Verification"
        });
        //Valeur ajouté du formulaire
        data.add(new String[]{
                super.idReponse,
                super.date,
                super.nomPrenom,
                super.pseudo,
                super.mail,
                nomArbre,
                autreArbre,
                nomBotanique,
                super.latitude,
                super.longitude,
                super.adresseArbre,
                super.photo,
                espace,
                "*Sans Réponse*",
                "*Sans Réponse*",
                "*Sans Réponse*",
                remarquable,
                super.observations,
                verification
        });
        //création de mon objet qui gère les CSV
        Csv csv = new Csv();
        //appelle de ma fonction qui va créer un CSV.
        csv.createCSV(data,path,"reponse_"+super.photo.replace("JPEG_","").replace(".jpg",""));
    }

    //Getter and setter (generate)

    public String getAutreArbre() {
        return autreArbre;
    }

    public void setAutreArbre(String autreArbre) {
        this.autreArbre = autreArbre;
    }

    public String getNomArbre() {
        return nomArbre;
    }

    public void setNomArbre(String nomArbre) {
        this.nomArbre = nomArbre;
    }

    public String getNomBotanique() {
        return nomBotanique;
    }

    public void setNomBotanique(String nomBotanique) {
        this.nomBotanique = nomBotanique;
    }

    public String getEspace() {
        return espace;
    }

    public void setEspace(String espace) {
        this.espace = espace;
    }

    public String getRemarquable() {
        return remarquable;
    }

    public void setRemarquable(String remarquable) {
        this.remarquable = remarquable;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
