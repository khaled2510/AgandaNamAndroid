package com.henallux.khal.smartcity.activity.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.khal.smartcity.activity.model.CategoryModel;
import com.henallux.khal.smartcity.activity.model.TokenModel;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import exception.ServerErrorException;

public class LoginDAO {

    TokenModel token;

    public TokenModel login (String username, String password) throws ServerErrorException{

        try{
            JSONObject login = new JSONObject();
            login.put("username", username);
            login.put("password", password);

            URL url = new URL ("https://agendanam.azurewebsites.net/api/jwt");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int code = connection.getResponseCode();
            int typecode = code/100;
            if(typecode != 2)
            {
                throw new ServerErrorException();
            }
            connection.setRequestMethod ("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            connection.connect();

            writer.write(login.toString());
            writer.flush();
            writer.close();
            out.close();

            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String stringJSON = "",line;
            while ((line=buffer.readLine()) != null){
                builder.append(line);
            }
            buffer.close();
            stringJSON = builder.toString();

            return jsonToToken(stringJSON);
        }
        catch (Exception e){
             throw new ServerErrorException();
        }
    }

    private TokenModel jsonToToken (String stringJSON) throws Exception {
        JSONArray jsonArray = new JSONArray(stringJSON);

        JSONObject jsonToken = jsonArray.getJSONObject(0);
        Gson object = new GsonBuilder().create();
        token = object.fromJson(jsonToken.toString(), TokenModel.class);

        return token;
    }
}
