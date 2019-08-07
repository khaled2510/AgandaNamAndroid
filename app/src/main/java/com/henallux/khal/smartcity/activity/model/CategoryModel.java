package com.henallux.khal.smartcity.activity.model;

import java.net.URL;

public class CategoryModel {

    private String libelle;
    //private URL urlIcon;
    private int id;
    private int categorieMere;

    public CategoryModel(String libelle, int id) {
        this.libelle = libelle;
        this.id = id;
    }

    public CategoryModel(String libelle/*,URL urlIcon*/, int id, int categorieMere) {
        this.libelle = libelle;
        //this.urlIcon = urlIcon;
        this.id = id;
        this.categorieMere = categorieMere;

    }

    public String getlibelle() {
        return libelle;
    }

    //public URL getUrlIcon () { return this.urlIcon; }

    public int getId () { return this.id; }

    public int getCategorieMere () { return this.categorieMere; }

    public void setLibelle(String libelle) { this.libelle = libelle; }

    //public void setUrlIcon (URL urlIcon) { this.urlIcon = urlIcon; }

    public void setId (int id) { this.id = id; }

    public void setCategorieMere (int categorieMere) { this.categorieMere = categorieMere; }


}
