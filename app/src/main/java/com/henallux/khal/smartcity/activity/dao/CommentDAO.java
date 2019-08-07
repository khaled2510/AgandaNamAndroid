package com.henallux.khal.smartcity.activity.dao;

import com.google.gson.JsonObject;
import com.henallux.khal.smartcity.activity.model.CommentModel;

import org.json.JSONObject;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommentDAO {

    public void addComment (CommentModel newComment, String token) throws Exception{

        JSONObject jsonString = new JSONObject ();
        jsonString.put("auteurId", newComment.getAuteurId());
        jsonString.put("texte", newComment.getEvenementId());
        jsonString.put("evenementId", newComment.getEvenementId());

        URL url = new URL("http://agendnam.azurewebsites.net/api/commentaire");
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
