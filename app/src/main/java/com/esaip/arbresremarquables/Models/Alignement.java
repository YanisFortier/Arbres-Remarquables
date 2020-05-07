package com.esaip.arbresremarquables.Models;

public class Alignement {
    private String nomPrenom;
    private String pseudo;
    private String mail;
    private double latitude;
    private double longitude;
    private String adresseArbre;
    private String photo;
    private String nbArbres;
    private String nbEspeces;
    private String especes;
    private String lien;
    private String protection;
    private String observations;
    private Boolean verification;

    public Alignement() {
    }

    public Alignement(String nomPrenom, String pseudo, String mail, double latitude, double longitude, String adresseArbre, String photo, String nbArbres, String nbEspeces, String especes, String lien, String protection, String observations, Boolean verification) {
        this.nomPrenom = nomPrenom;
        this.pseudo = pseudo;
        this.mail = mail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresseArbre = adresseArbre;
        this.photo = photo;
        this.nbArbres = nbArbres;
        this.nbEspeces = nbEspeces;
        this.especes = especes;
        this.lien = lien;
        this.protection = protection;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
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

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean getVerification() {
        return verification;
    }

    public void setVerification(Boolean verification) {
        this.verification = verification;
    }
}
