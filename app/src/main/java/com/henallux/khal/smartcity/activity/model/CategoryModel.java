package com.henallux.khal.smartcity.activity.model;

import java.net.URL;

public class CategoryModel {

    private int id;
    private String libelle;
    private URL urlIcon;

    public CategoryModel(int id, String libelle) {
        this.id = id;
        this.libelle = libelle;
    }

    public String getlibelle() {
        return libelle;
    }

    public URL getUrlIcon () { return this.urlIcon; }

    public int getId () { return this.id; }


    public void setLibelle(String libelle) { this.libelle = libelle; }

    public void setUrlIcon (URL urlIcon) { this.urlIcon = urlIcon; }

    public void setId (int id) { this.id = id; }



}
