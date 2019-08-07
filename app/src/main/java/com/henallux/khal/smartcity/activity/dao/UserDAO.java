package com.henallux.khal.smartcity.activity.dao;

import com.google.gson.JsonObject;
import com.henallux.khal.smartcity.activity.model.UserModel;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class UserDAO {
    public void addUser (UserModel user, String token) throws Exception{
        JSONObject jsonString = new JSONObject();
        jsonString.put ("pseudo", user.getPseudo());
        jsonString.put ("nom", user.getLastName());
        jsonString.put ("prenom", user.getFirstName());
        jsonString.put ("email", user.geteMail());
        jsonString.put ("motDePasse", user.getPassword());
        jsonString.put ("Role", "Utilisateur");

        URL url = new URL("http://agendnam.azurewebsites.net/api/utilisateur");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Bearer " + token);

        connection.setDoOutput(true);

        OutputStream out = connection.getOutputStream();
        OutputStreamWriter writer = new OutputStreamWriter(out);
        connection.connect();

        writer.write(jsonString.toString());

        writer.flush();
        writer.close();
        out.close();

        connection.disconnect();

    }
}
