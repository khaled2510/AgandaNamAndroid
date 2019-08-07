package com.henallux.khal.smartcity.activity.dao;

import com.henallux.khal.smartcity.activity.model.CategoryModel;


import java.net.*;
import java.util.ArrayList;
import java.io.*;
import org.json.*;
import com.google.gson.*;


public class CategoryDAO {

    public ArrayList<CategoryModel> getAllCategories (String token) throws Exception{
        URL url = new URL ("http://localhost:5000/api/categorie");
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

        return jsonToCategories(stringJSON);
    }

    private ArrayList<CategoryModel> jsonToCategories (String stringJSON) throws Exception {
        ArrayList<CategoryModel> categories = new ArrayList<CategoryModel>();
        CategoryModel category;
        JSONArray jsonArray = new JSONArray(stringJSON);

        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonCategory = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            category = object.fromJson(jsonCategory.toString(), CategoryModel.class);
            categories.add(category);
        }

        return categories;

    }
}
