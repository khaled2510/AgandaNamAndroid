package com.henallux.khal.smartcity.activity.dao;

import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.khal.smartcity.activity.TLSSocketFactory;
import com.henallux.khal.smartcity.activity.exception.ServerErrorException;
import com.henallux.khal.smartcity.activity.exception.UnAuthorised;
import com.henallux.khal.smartcity.activity.model.TokenModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginDAO {

    TokenModel token;

    public LoginDAO(){

    }

    public TokenModel login (String username, String password) throws ServerErrorException, UnAuthorised{

        JSONObject login = new JSONObject();
        try{
            login.put("UserName", username);
            login.put("Password", password);
            Log.i("JSON objet", login.toString());
        }
        catch (JSONException e){
            Log.i("JSON 1", e.getMessage());
        }

        try {
            URL url = new URL("https://agendanam.azurewebsites.net/api/jwt");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(new TLSSocketFactory());

            int code = connection.getResponseCode();
            Log.i("Reponse du serveur", connection.getResponseMessage());
            int typecode = code/100;
            if(typecode == 5)
            {
                throw new ServerErrorException();
            }
            if(typecode == 4)
            {
                throw new UnAuthorised();
            }
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
        catch(IOException e)
        {
            Log.i("I/O test", e.getMessage());
        }
        catch(JSONException e)
        {
            Log.i("JSON test", e.getMessage());
        }
        catch (Exception e){
            Log.i("EXCEPTION", e.getMessage());
        }
         return null;
    }

    private TokenModel jsonToToken (String stringJSON) throws Exception {
        JSONArray jsonArray = new JSONArray(stringJSON);

        JSONObject jsonToken = jsonArray.getJSONObject(0);
        Gson object = new GsonBuilder().create();
        token = object.fromJson(jsonToken.toString(), TokenModel.class);

        return token;
    }
}
