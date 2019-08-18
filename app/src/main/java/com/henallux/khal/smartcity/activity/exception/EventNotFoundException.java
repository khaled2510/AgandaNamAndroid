package com.henallux.khal.smartcity.activity.exception;

public class EventNotFoundException extends Exception {
    public String getMessage(){
        return "Aucun événement trouvé.";
    }
}
