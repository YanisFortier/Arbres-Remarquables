package com.esaip.arbresremarquables.Models;

public class Arbre {
    private String nomPrenom;
    private String pseudo;
    private String mail;
    private String nomArbre;
    private String nomBotanique;
    private String latitude;
    private String longitude;
    private String adresseArbre;
    private String photo;
    private String espace;
    private String remarquable;
    private String observations;
    private String verification;

    public Arbre() {
    }

    public Arbre(String nomPrenom, String pseudo, String mail, String nomArbre, String nomBotanique, String latitude, String longitude, String adresseArbre, String photo, String espace, String remarquable, String observations, String verification) {
        this.nomPrenom = nomPrenom;
        this.pseudo = pseudo;
        this.mail = mail;
        this.nomArbre = nomArbre;
        this.nomBotanique = nomBotanique;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresseArbre = adresseArbre;
        this.photo = photo;
        this.espace = espace;
        this.remarquable = remarquable;
        this.observations = observations;
        this.verification = verification;
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

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }
}
