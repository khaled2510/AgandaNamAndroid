package com.henallux.khal.smartcity.activity.exception;

public class AlreadyExistException extends Exception {
    public String getMessage(){
        return "Pseudo déjâ existant, choisissez un autre pseudo.";
    }
}
