package com.henallux.khal.smartcity.activity.model;

public class TokenModel {

    private String access_token;
    private String expires_in;

    public TokenModel(String access_token, String expires_in) {
        this.access_token = access_token;
        this.expires_in = expires_in;
    }

    public String getAccess_token() {
        return access_token;
    }
}
