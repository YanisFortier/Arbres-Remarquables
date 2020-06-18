package com.esaip.arbresremarquables.Models;

import java.text.SimpleDateFormat;
import java.util.Date;

// Objet abstrait qui contient les variables et fonctions qui sont commune à tout les formulaires

//Objet mère de Alignement - Arbre - EspaceBoise
public class generique {

    protected String nomPrenom;
    protected String pseudo;
    protected String mail;
    protected String latitude;
    protected String longitude;
    protected String adresseArbre;
    protected String photo;
    protected String observations;
    protected String date;

    public generique(){

    }

    public generique(String nomPrenom, String pseudo, String mail, String latitude, String longitude, String adresseArbre, String photo, String observations) {
        this.nomPrenom = nomPrenom;
        this.pseudo = pseudo;
        this.mail = mail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresseArbre = adresseArbre;
        this.photo = photo;
        this.observations = observations;
        this.date = DateDuJour();
    }

    private String DateDuJour(){
        final Date today = new Date();
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(today);
    }

    public String getNomPrenom() {
        return nomPrenom;
    }

    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAdresseArbre() {
        return adresseArbre;
    }

    public void setAdresseArbre(String adresseArbre) {
        this.adresseArbre = adresseArbre;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
