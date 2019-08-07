package com.henallux.khal.smartcity.activity.model;

import java.util.ArrayList;
import java.util.List;

public class EventModel {

    private int id;
    private String name;
    private String description;
    private int likes;
    private List<CommentModel> comments;
    private String adresse;
    private String dateDébut;
    private String dateFin;
    private int participants;

    public EventModel(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public EventModel(int id, String name, String description, int likes, String adresse, String dateDébut, String dateFin, int participants) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.likes = likes;
        this.adresse = adresse;
        this.dateDébut = dateDébut;
        this.dateFin = dateFin;
        this.participants = participants;
        this.comments = new ArrayList<CommentModel>();
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<CommentModel> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }

    public String getAdresse() { return adresse; }

    public String getDateDébut() { return dateDébut; }

    public String getDateFin() { return dateFin; }

    public int getParticipants() { return participants; }



    public void setId (int id) {this.id = id; }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setComments(CommentModel comment) { this.comments.add(comment); }

    public void setAdresse(String adresse) { this.adresse = adresse; }

    public void setDateDébut(String dateDébut) { this.dateDébut = dateDébut; }

    public void setDateFin(String dateFin) { this.dateFin = dateFin; }

    public void setParticipants(int participants) { this.participants = participants; }
}
