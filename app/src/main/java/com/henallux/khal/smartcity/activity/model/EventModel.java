package com.henallux.khal.smartcity.activity.model;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class EventModel implements Serializable {

    private int id;
    private String nom;
    private String description;
    private ArrayList<PresentationModel> presentations;
    private String rue;
    private String numero;
    private int codePostal;
    private String localite;
    private String dateCreationEvenement;
    private int likes;
    private int participants;

    public EventModel(String name, String description) {
        this.nom = name;
        this.description = description;
    }

    public EventModel(int id, String name, String description, ArrayList<PresentationModel> presentations, String rue,
                      String numero, int codePostal, String localite, String dateCreationEvenement) {
        this.id = id;
        this.nom = name;
        this.description = description;
        this.presentations = presentations;
        this.rue = rue;
        this.numero = numero;
        this.codePostal = codePostal;
        this.localite = localite;
        this.dateCreationEvenement = dateCreationEvenement;
    }

    public int getId() { return id; }

    public String getName() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getLikes() {
        return likes;
    }

    public int getParticipants() { return participants; }


    public void setId (int id) {this.id = id; }

    public void setName(String name) {
        this.nom = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setParticipants(int participants) { this.participants = participants; }

    public ArrayList<PresentationModel> getPresentation() {
        return presentations;
    }

    public void setPresentation(ArrayList<PresentationModel> presentations) {
        this.presentations = presentations;
    }

    public String getRue() {
        return rue;
    }

    public void setRue(String rue) {
        this.rue = rue;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(int codePostal) {
        this.codePostal = codePostal;
    }

    public String getLocalite() {
        return localite;
    }

    public void setLocalite(String localite) {
        this.localite = localite;
    }

    public String getDateCreationEvenement() {
        return dateCreationEvenement;
    }

    public void setDateCreationEvenement(String dateCreationEvenement) {
        this.dateCreationEvenement = dateCreationEvenement;
    }
}
