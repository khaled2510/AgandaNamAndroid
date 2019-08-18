package com.henallux.khal.smartcity.activity.dao;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.henallux.khal.smartcity.activity.exception.AlreadyExistException;
import com.henallux.khal.smartcity.activity.exception.CommentNotFoundException;
import com.henallux.khal.smartcity.activity.exception.ServerErrorException;
import com.henallux.khal.smartcity.activity.exception.UnAuthorised;
import com.henallux.khal.smartcity.activity.model.CategoryModel;
import com.henallux.khal.smartcity.activity.model.CommentModel;
import com.henallux.khal.smartcity.activity.model.UserModel;
import com.henallux.khal.smartcity.activity.utils.TLSSocketFactory;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class CommentDAO {

    private int responseCode = 0;

    public ArrayList<CommentModel> getCommentEvent (String token, int idEvent, int pageIndex, int pageSize) throws ServerErrorException, CommentNotFoundException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Commentaire/commentEvent/"+idEvent+"/"+pageSize+"/"+pageIndex);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(new TLSSocketFactory());
            connection.setRequestProperty("Authorization", "Bearer " + token);
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
            return jsonToComments(stringJSON);
        }
        catch (Exception e){
            if(responseCode == 4){
                throw new CommentNotFoundException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    private ArrayList<CommentModel> jsonToComments (String stringJSON) throws Exception {
        ArrayList<CommentModel> commentModels = new ArrayList<CommentModel>();
        CommentModel commentModel;
        JSONArray jsonArray = new JSONArray(stringJSON);

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonCategory = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            commentModel = object.fromJson(jsonCategory.toString(), CommentModel.class);
            commentModels.add(commentModel);
        }
        return commentModels;
    }

    public CommentModel addComment (CommentModel comment, String token) throws ServerErrorException {

        try{
            JSONObject jsonString = new JSONObject();
            jsonString.put ("texte", comment.getTexte());
            jsonString.put ("auteurId", comment.getAuteurId());
            jsonString.put ("evenementId", comment.getEvenementId());

            URL url = new URL("https://agendanam2.azurewebsites.net/api/Commentaire");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + token);
            connection.setDoOutput(true);

            OutputStream out = connection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(out);
            connection.connect();

            writer.write(jsonString.toString());

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
            connection.disconnect();

            return jsonToComment(stringJSON);
        }
        catch (Exception e){
            Log.i("mon exection :", e.getMessage());
            throw new ServerErrorException();
        }
    }

    private CommentModel jsonToComment (String stringJSON) throws Exception {
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        CommentModel commentModel;
        commentModel = object.fromJson(jsonToken.toString(), CommentModel.class);

        return commentModel;
    }
}
;