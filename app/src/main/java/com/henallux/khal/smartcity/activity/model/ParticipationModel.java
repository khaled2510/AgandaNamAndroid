package com.henallux.khal.smartcity.activity.model;

public class ParticipationModel {

    private int id;
    private String participantId;
    private int evenementId;

    public ParticipationModel(String participantId, int evenementId){
        this.evenementId = evenementId;
        this.participantId = participantId;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public int getEvenementId() {
        return evenementId;
    }

    public void setEvenementId(int evenementId) {
        this.evenementId = evenementId;
    }
}
