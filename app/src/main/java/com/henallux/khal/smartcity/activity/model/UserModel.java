package com.henallux.khal.smartcity.activity.model;

import java.util.ArrayList;

public class UserModel {

    private String pseudo;
    private String lastName;
    private String firstName;
    private String eMail;
    private String password;
    private String role;
    private ArrayList<CommentModel> comments;
    private ArrayList<EventModel> events;


    public UserModel(String pseudo, String lastName, String firstName, String eMail, String password, String role, ArrayList<CommentModel> comments, ArrayList<EventModel> events) {
        this.pseudo = pseudo;
        this.lastName = lastName;
        this.firstName = firstName;
        this.eMail = eMail;
        this.password = password;
        this.role = role;
        this.comments = comments;
        this.events = events;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String geteMail() {
        return eMail;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public ArrayList<CommentModel> getComments() {
        return comments;
    }

    public ArrayList<EventModel> getEvents() {
        return events;
    }


    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setComments(ArrayList<CommentModel> comments) {
        this.comments = comments;
    }

    public void setEvents(ArrayList<EventModel> events) {
        this.events = events;
    }
}
