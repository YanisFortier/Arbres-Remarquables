package com.esaip.arbresremarquables.Model;

public class EspaceBoise {
    private String nomPrenom;
    private String pseudo;
    private String mail;
    private double latitude;
    private double longitude;
    private String adresseArbre;
    private String photo;
    private String nbArbres;
    private String nbEspeces;
    private String niveau;
    private Boolean pointEau;
    private Boolean abris;
    private Boolean eclairage;
    private String biodiversite;
    private Boolean ombre;
    private String entretien;
    private String globalement;
    private String observations;

    public EspaceBoise() {
    }

    public EspaceBoise(String nomPrenom, String pseudo, String mail, double latitude, double longitude, String adresseArbre, String photo, String nbArbres, String nbEspeces, String niveau, Boolean pointEau, Boolean abris, Boolean eclairage, String biodiversite, Boolean ombre, String entretien, String globalement, String observations) {
        this.nomPrenom = nomPrenom;
        this.pseudo = pseudo;
        this.mail = mail;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adresseArbre = adresseArbre;
        this.photo = photo;
        this.nbArbres = nbArbres;
        this.nbEspeces = nbEspeces;
        this.niveau = niveau;
        this.pointEau = pointEau;
        this.abris = abris;
        this.eclairage = eclairage;
        this.biodiversite = biodiversite;
        this.ombre = ombre;
        this.entretien = entretien;
        this.globalement = globalement;
        this.observations = observations;
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

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Boolean getPointEau() {
        return pointEau;
    }

    public void setPointEau(Boolean pointEau) {
        this.pointEau = pointEau;
    }

    public Boolean getAbris() {
        return abris;
    }

    public void setAbris(Boolean abris) {
        this.abris = abris;
    }

    public Boolean getEclairage() {
        return eclairage;
    }

    public void setEclairage(Boolean eclairage) {
        this.eclairage = eclairage;
    }

    public String getBiodiversite() {
        return biodiversite;
    }

    public void setBiodiversite(String biodiversite) {
        this.biodiversite = biodiversite;
    }

    public Boolean getOmbre() {
        return ombre;
    }

    public void setOmbre(Boolean ombre) {
        this.ombre = ombre;
    }

    public String getEntretien() {
        return entretien;
    }

    public void setEntretien(String entretien) {
        this.entretien = entretien;
    }

    public String getGlobalement() {
        return globalement;
    }

    public void setGlobalement(String globalement) {
        this.globalement = globalement;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
