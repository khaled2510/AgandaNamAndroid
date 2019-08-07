package com.henallux.khal.smartcity.activity.model;

import java.lang.ref.SoftReference;

public class CommentModel {
    private int id;
    private String auteurId;
    private String texte;
    private int evenementId;

    public CommentModel(int id, String auteurId, String texte, int evenementId) {
        this.id = id;
        this.auteurId = auteurId;
        this.texte = texte;
        this.evenementId = evenementId;
    }

    public int getId() { return id; }

    public String getAuteurId() {
        return auteurId;
    }

    public String getTexte() {
        return texte;
    }

    public int getEvenementId() { return evenementId; }


    public void setId (int id) { this.id = id; }

    public void setAuteurId(String auteurId) {
        this.auteurId = auteurId;
    }

    public void setTexte(String texte) {
        this.texte = texte;
    }

    public void setEvenementId(int evenementId) { this.evenementId = evenementId; }
}
