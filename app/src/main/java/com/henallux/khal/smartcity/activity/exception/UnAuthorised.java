package com.henallux.khal.smartcity.activity.exception;

public class UnAuthorised extends Exception {
    public String getMessage(){
        return "Pseudo ou mot de passe incorrect.";
    }
}
