package com.henallux.khal.smartcity.activity.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PresentationModel implements Serializable {

    private int id;
    private String dateHeureDebut;
    private String dateHeureFin;

    public PresentationModel(int id, String dateHeureDebut, String dateHeureFin){
        this.id = id;
        this.dateHeureDebut = dateHeureDebut;
        this.dateHeureFin = dateHeureFin;
    }

    public String getDateHeureFin() {
        return dateHeureFin;
    }

    public void setDateHeureFin(String dateHeureFin) {
        this.dateHeureFin = dateHeureFin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateHeureDebut() {
        return dateHeureDebut;
    }

    public void setDateHeureDebut(String dateHeureDebut) {
        this.dateHeureDebut = dateHeureDebut;
    }
}
