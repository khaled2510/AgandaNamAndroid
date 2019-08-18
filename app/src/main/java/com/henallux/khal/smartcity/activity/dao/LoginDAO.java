package com.henallux.khal.smartcity.activity.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.khal.smartcity.activity.utils.TLSSocketFactory;
import com.henallux.khal.smartcity.activity.exception.ServerErrorException;
import com.henallux.khal.smartcity.activity.exception.UnAuthorised;
import com.henallux.khal.smartcity.activity.model.TokenModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class LoginDAO {

    private int responseCode = 0;

    public LoginDAO(){

    }

    public TokenModel login (String username, String password) throws ServerErrorException, UnAuthorised{

        JSONObject login = new JSONObject();
        try {
            login.put("UserName", username);
            login.put("Password", password);
            URL url = new URL("https://agendanam2.azurewebsites.net/api/jwt");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(new TLSSocketFactory());
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            connection.connect();

            writer.write(login.toString());
            writer.flush();
            writer.close();
            out.close();
            responseCode = connection.getResponseCode()/100;
            //Log.i("code serveur :",connection.getResponseMessage());

            BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String stringJSON = "",line;
            while ((line=buffer.readLine()) != null){
                builder.append(line);
            }
            buffer.close();
            stringJSON = builder.toString();
            connection.disconnect();

            return jsonToToken(stringJSON);
        }
        catch (Exception e){
            if(responseCode == 4){
                throw new UnAuthorised();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    private TokenModel jsonToToken (String stringJSON) throws Exception {
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        TokenModel token = object.fromJson(jsonToken.toString(), TokenModel.class);

        return token;
    }
}
