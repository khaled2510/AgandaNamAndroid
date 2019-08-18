package com.henallux.khal.smartcity.activity.exception;

public class AlreadyLikeException extends Exception {
    public String getMessage(){
        return "Vous avez déjâ aimer cet événement.";
    }
}
