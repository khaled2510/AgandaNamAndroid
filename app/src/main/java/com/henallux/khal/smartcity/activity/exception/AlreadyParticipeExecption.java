package com.henallux.khal.smartcity.activity.exception;

public class AlreadyParticipeExecption extends Exception {
    public String getMessage(){
        return "Vous participez déjâ à l'événement.";
    }
}
