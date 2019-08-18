package com.henallux.khal.smartcity.activity.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.henallux.khal.smartcity.activity.exception.AlreadyExistException;
import com.henallux.khal.smartcity.activity.exception.ServerErrorException;
import com.henallux.khal.smartcity.activity.exception.UnAuthorised;
import com.henallux.khal.smartcity.activity.model.TokenModel;
import com.henallux.khal.smartcity.activity.model.UserModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class UserDAO {

    private static final String ROLE = "Subscribe";
    private UserModel userAdd;
    private int responseCode = 0;

    public UserDAO(){

    }

    public UserModel addUser (UserModel user) throws ServerErrorException, AlreadyExistException{

        try{
            JSONObject jsonString = new JSONObject();
            jsonString.put ("pseudo", user.getPseudo());
            jsonString.put ("nom", user.getLastName());
            jsonString.put ("prenom", user.getFirstName());
            jsonString.put ("email", user.geteMail());
            jsonString.put ("motDePasse", user.getPassword());
            jsonString.put ("role", ROLE);

            URL url = new URL("https://agendanam2.azurewebsites.net/api/utilisateur");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            connection.connect();

            writer.write(jsonString.toString());

            writer.flush();
            writer.close();
            out.close();
            responseCode = connection.getResponseCode()/100;

            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String stringJSON = "",line;
            while ((line=buffer.readLine()) != null){
                builder.append(line);
            }
            buffer.close();
            stringJSON = builder.toString();
            connection.disconnect();

            return jsonToUser(stringJSON);
        }
        catch (Exception e){
            if(responseCode == 4){
                throw new AlreadyExistException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    private UserModel jsonToUser (String stringJSON) throws Exception {
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        userAdd = object.fromJson(jsonToken.toString(), UserModel.class);

        return userAdd;
    }
}
