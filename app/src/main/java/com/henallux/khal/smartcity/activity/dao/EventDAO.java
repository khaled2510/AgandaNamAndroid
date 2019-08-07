package com.henallux.khal.smartcity.activity.dao;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.khal.smartcity.activity.model.EventModel;
import com.henallux.khal.smartcity.activity.model.EventModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EventDAO {
    public ArrayList<EventModel> getEventByCategori (int idEvent, String token) throws Exception{
        URL url = new URL ("https://localhost:5000/api/categorie/" + idEvent);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Authorization", "Bearer " + token);

        BufferedReader buffer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String stringJSON = "",line;
        while ((line=buffer.readLine()) != null){
            builder.append(line);
        }
        buffer.close();
        stringJSON = builder.toString();

        return jsonToEvents(stringJSON);
    }

    private ArrayList<EventModel> jsonToEvents (String stringJSON) throws Exception {
        ArrayList<EventModel> events = new ArrayList<EventModel>();
        EventModel event;
        JSONArray jsonArray = new JSONArray(stringJSON);

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonEvent = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            event = object.fromJson(jsonEvent.toString(), EventModel.class);
            events.add(event);
        }

        return events;

    }
}
