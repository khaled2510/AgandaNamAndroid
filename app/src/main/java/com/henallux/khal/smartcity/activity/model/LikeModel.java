package com.henallux.khal.smartcity.activity.model;

public class LikeModel {

    private int id;
    private String utilisateurId;
    private int evenementId;

    public LikeModel(String utilisateurId, int evenementId){
        this.evenementId = evenementId;
        this.utilisateurId = utilisateurId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }

    public String getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
    }
}
