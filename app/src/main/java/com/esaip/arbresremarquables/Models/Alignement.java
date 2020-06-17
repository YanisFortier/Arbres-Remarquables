package com.esaip.arbresremarquables.Models;

import com.esaip.arbresremarquables.Csv;
import java.util.ArrayList;
import java.util.List;

public class Alignement extends generique{
    private String nbArbres;
    private String nbEspeces;
    private String especes;
    private String lien;
    private String nomBotannique;
    private String protection;
    private Boolean verification;

    public Alignement() {
        super();
    }

    public Alignement(String nomPrenom, String pseudo, String mail, String latitude, String longitude, String adresseArbre, String photo, String observations,
                      String nbArbres, String nbEspeces, String especes, String lien, String nomBotannique, String protection, Boolean verification) {
        //Récup les infos de l'héritage (générique).
        super(nomPrenom,pseudo,mail,latitude,longitude,adresseArbre,photo,observations);

        this.nbArbres = nbArbres;
        this.nbEspeces = nbEspeces;
        this.especes = especes;
        this.nomBotannique = nomBotannique;
        this.lien = lien;
        this.protection = protection;
        this.verification = verification;
    }

    public void CreateCsv(){
        //Conversion d'un booléen en String
        String verif;
        if (verification == true){ verif = "oui";}
        else {verif = "non";}
        // /Conversion


        List<String[]> data= new ArrayList<String[]>();
        //Liste de données que contiendra mon CSV (Dans l'ordre Haut-Bas équivalent Gauche-Droit)
        data.add(new String[]{
                "id_Reponse",
                super.date,
                "utilisateur",
                "IP",
                super.nomPrenom,
                super.pseudo,
                super.mail,
                especes,
                nbEspeces,
                nomBotannique,
                super.latitude,
                super.longitude,
                super.adresseArbre,
                "Photo",
                nbArbres,
                lien,
                "Autre raison",
                "biodiversite",
                "autre espace protege",
                super.observations,
                protection,
                verif
        });

        //création de mon objet qui gère les CSV
        Csv csv = new Csv();
        //appelle de ma fonction qui va créer un CSV.
        csv.createCSV(data);
    }

    public String getNbArbres() {
        return nbArbres;
    }

    public void setNbArbres(String nbArbres) {
        this.nbArbres = nbArbres;
    }

    public String getNbEspeces() {
        return nbEspeces;
    }

    public void setNbEspeces(String nbEspeces) {
        this.nbEspeces = nbEspeces;
    }

    public String getEspeces() {
        return especes;
    }

    public void setEspeces(String especes) {
        this.especes = especes;
    }

    public String getLien() {
        return lien;
    }

    public void setLien(String lien) {
        this.lien = lien;
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection;
    }

    public Boolean getVerification() {
        return verification;
    }

    public void setVerification(Boolean verification) {
        this.verification = verification;
    }
}
