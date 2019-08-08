package com.henallux.khal.smartcity.activity.exception;

public class ServerErrorException extends Exception {
    public String getMessage(){
        return "Impossible de se connecter au serveur";
    }
}
