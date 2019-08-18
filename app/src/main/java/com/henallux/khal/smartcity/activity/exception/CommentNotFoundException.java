package com.henallux.khal.smartcity.activity.exception;

public class CommentNotFoundException extends Exception {
    public String getMessage(){
        return "Aucun commentaires trouvé";
    }
}
