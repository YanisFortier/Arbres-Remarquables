package com.esaip.arbresremarquables;

public class Arbre {
    private String nomPrenom;
    private String pseudo;
    private String mail;
    private String nomArbre;
    private String nomBotanique;
    private double latitude;
    private double longitude;
    private String adresseArbre;
    private String photo;
    private String espace;
    private String remarquable;
    private String biodiversite;
    private String remarquabilite;
    private String observations;
    private Boolean verification;

    public Arbre() {
    }

    public Arbre(String nomPrenom, String pseudo, String mail, String nomArbre, String nomBotanique, double latitude, double longitude, String adresseArbre, String photo, String espace, String remarquable, String biodiversite, String remarquabilite, String observations, Boolean verification) {
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
        this.biodiversite = biodiversite;
        this.remarquabilite = remarquabilite;
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

    public String getBiodiversite() {
        return biodiversite;
    }

    public void setBiodiversite(String biodiversite) {
        this.biodiversite = biodiversite;
    }

    public String getRemarquabilite() {
        return remarquabilite;
    }

    public void setRemarquabilite(String remarquabilite) {
        this.remarquabilite = remarquabilite;
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
