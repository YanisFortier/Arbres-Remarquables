package com.esaip.arbresremarquables.Models;


import com.esaip.arbresremarquables.Csv;
import java.util.ArrayList;
import java.util.List;

public class EspaceBoise extends generique{
    private String nbArbres;
    private String espace;
    private String nbEspeces;
    private String niveau;
    private String pointEau;
    private String abris;
    private String eclairage;
    private String biodiversite;
    private String autreBio;
    private String ombre;
    private String entretien;
    private String globalement;

    public EspaceBoise(){
        super();
    }

    public EspaceBoise(String nomPrenom, String pseudo, String mail, String latitude, String longitude, String adresseArbre, String photo,String espace, String observations,
                       String nbArbres, String nbEspeces, String niveau, String pointEau, String abris, String eclairage, String biodiversite, String autreBio, String ombre, String entretien, String globalement) {
        //Récup les infos de l'héritage (générique).
        super(nomPrenom,pseudo,mail,latitude,longitude,adresseArbre,photo,observations);

        this.autreBio = autreBio;
        this.espace = espace;
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
    }

    public void CreateCsv(String path){

        List<String[]> data= new ArrayList<String[]>();
        //Liste de données que contiendra mon CSV (Dans l'ordre Haut-Bas équivalent Gauche-Droit)
        //En tête
        data.add(new String[]{
                "id_Reponse",
                "date",
                "Nom Prenom",
                "Pseudo",
                "Mail",
                "Latitude",
                "Longitude",
                "Adresse Arbre",
                "Photo",
                "Espace",
                "Nombre Arbres",
                "Nombre Especes",
                "Niveau",
                "Point Eau",
                "Abris",
                "Eclairage",
                "Biodiversite",
                "Autre Bio",
                "Ombre",
                "Entretien",
                "Globalement",
                "Observations"
        });
        //Valeur ajouté du formulaire
        data.add(new String[]{
                super.idReponse,
                super.date,
                super.nomPrenom,
                super.pseudo,
                super.mail,
                super.latitude,
                super.longitude,
                super.adresseArbre,
                super.photo,
                espace,
                nbArbres,
                nbEspeces,
                niveau,
                pointEau,
                abris,
                eclairage,
                biodiversite,
                autreBio,
                ombre,
                entretien,
                globalement,
                super.observations
        });

        //création de mon objet qui gère les CSV
        Csv csv = new Csv();
        //appelle de ma fonction qui va créer un CSV.
        csv.createCSV(data,path,"reponse_"+super.photo.replace("JPEG_","").replace(".jpg",""));

    }


    public String getEspace() {
        return espace;
    }

    public void setEspace(String espace) {
        this.espace = espace;
    }

    public String getAutreBio() {
        return autreBio;
    }

    public void setAutreBio(String autreBio) {
        this.autreBio = autreBio;
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

    public String getPointEau() {
        return pointEau;
    }

    public void setPointEau(String pointEau) {
        this.pointEau = pointEau;
    }

    public String getAbris() {
        return abris;
    }

    public void setAbris(String abris) {
        this.abris = abris;
    }

    public String getEclairage() {
        return eclairage;
    }

    public void setEclairage(String eclairage) {
        this.eclairage = eclairage;
    }

    public String getBiodiversite() {
        return biodiversite;
    }

    public void setBiodiversite(String biodiversite) {this.biodiversite = biodiversite; }

    public String getOmbre() {
        return ombre;
    }

    public void setOmbre(String ombre) {
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
}
