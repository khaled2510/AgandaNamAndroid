package com.henallux.khal.smartcity.activity.dao;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.henallux.khal.smartcity.activity.exception.AlreadyExistException;
import com.henallux.khal.smartcity.activity.exception.AlreadyLikeException;
import com.henallux.khal.smartcity.activity.exception.AlreadyParticipeExecption;
import com.henallux.khal.smartcity.activity.exception.EventNotFoundException;
import com.henallux.khal.smartcity.activity.exception.LikesParticipationException;
import com.henallux.khal.smartcity.activity.exception.ServerErrorException;
import com.henallux.khal.smartcity.activity.exception.UnAuthorised;
import com.henallux.khal.smartcity.activity.model.EventModel;
import com.henallux.khal.smartcity.activity.model.EventModel;
import com.henallux.khal.smartcity.activity.model.LikeModel;
import com.henallux.khal.smartcity.activity.model.ParticipationModel;
import com.henallux.khal.smartcity.activity.model.PresentationModel;
import com.henallux.khal.smartcity.activity.model.UserModel;
import com.henallux.khal.smartcity.activity.utils.ConvertionDate;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class EventDAO {

    private int responseCode = 0;
    private ConvertionDate convertionDate = new ConvertionDate();

    // recupére par categorie
    public ArrayList<EventModel> getEventByCategori (int idEvent, String token) throws ServerErrorException, EventNotFoundException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Evenement/parCatego/" + idEvent);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return jsonToEvents(stringJSON);
        }
        catch(Exception e){
            if(responseCode == 4){
                throw new EventNotFoundException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    // récupére sur base des participation du user
    public ArrayList<EventModel> getEventByParticipation (String pseudo, String token) throws ServerErrorException, EventNotFoundException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Evenement/parParticipation/" + pseudo);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return jsonToEvents(stringJSON);
        }
        catch(Exception e){
            if(responseCode == 4){
                throw new EventNotFoundException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    // récupére sur base de dates données
    public ArrayList<EventModel> getEventByDate (Date dateDebut, Date dateFin, String token) throws ServerErrorException, EventNotFoundException {
        try{
            String dateDeb = convertionDate.convertDateToString(dateDebut);
            String dateF = convertionDate.convertDateToString(dateFin);
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Evenement/parDate/" + dateDeb + "/" + dateF);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return jsonToEvents(stringJSON);
        }
        catch(Exception e){
            if(responseCode == 4){
                throw new EventNotFoundException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    private ArrayList<EventModel> jsonToEvents (String stringJSON) throws Exception {
        ArrayList<EventModel> events = new ArrayList<EventModel>();
        EventModel event;
        JSONArray jsonArray = new JSONArray(stringJSON);
        for (int i = 0; i < jsonArray.length(); i++){
            JSONObject jsonEvent = jsonArray.getJSONObject(i);
            Gson object = new GsonBuilder().create();
            event = object.fromJson(jsonEvent.toString(), EventModel.class);

            ArrayList<PresentationModel> presentationModels = new ArrayList<>();
            PresentationModel presentationModel;
            JSONArray jsonArrayPres = new JSONArray(jsonEvent.get("presentation").toString());
            for (int j = 0; j < jsonArrayPres.length(); j++){
                JSONObject jsonPres = jsonArrayPres.getJSONObject(j);
                Gson objectPres = new GsonBuilder().create();
                presentationModel = objectPres.fromJson(jsonPres.toString(), PresentationModel.class);
                presentationModels.add(presentationModel);
            }
            event.setPresentation(presentationModels);
            events.add(event);
        }

        return events;
    }

    // récupére les likes
    public String getLikes (int idEvent, String token) throws ServerErrorException, LikesParticipationException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/NbJaime/count/" + idEvent);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return stringJSON;
        }
        catch(Exception e){
            if(responseCode == 4){
                throw new LikesParticipationException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    // récupére les participations
    public String getParticipation (int idEvent, String token) throws ServerErrorException, LikesParticipationException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Participation/count/" + idEvent);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return stringJSON;
        }
        catch(Exception e){
            if(responseCode == 4){
                throw new LikesParticipationException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    // ajoute un like
    public LikeModel addLike (LikeModel like, String token) throws ServerErrorException, AlreadyLikeException {

        try{
            JSONObject jsonString = new JSONObject();
            jsonString.put ("utilisateurId", like.getUtilisateurId());
            jsonString.put ("evenementId", like.getEvenementId());

            URL url = new URL("https://agendanam2.azurewebsites.net/api/NbJaime");
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

            return jsonToLike(stringJSON);
        }
        catch (Exception e){
            if(responseCode == 4){
                throw new AlreadyLikeException();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    private LikeModel jsonToLike (String stringJSON) throws Exception {
        LikeModel likeModel;
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        likeModel = object.fromJson(jsonToken.toString(), LikeModel.class);

        return likeModel;
    }

    // ajoute une participation
    public ParticipationModel addParticipation (ParticipationModel participationModel, String token) throws ServerErrorException, AlreadyParticipeExecption {

        try{
            JSONObject jsonString = new JSONObject();
            jsonString.put ("participantId", participationModel.getParticipantId());
            jsonString.put ("evenementId", participationModel.getEvenementId());

            URL url = new URL("https://agendanam2.azurewebsites.net/api/Participation");
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

            return jsonToParticipation(stringJSON);
        }
        catch (Exception e){
            if(responseCode == 4){
                throw new AlreadyParticipeExecption();
            }
            else
            {
                throw new ServerErrorException();
            }
        }
    }

    private ParticipationModel jsonToParticipation (String stringJSON) throws Exception {
        ParticipationModel participationModel;
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        participationModel = object.fromJson(jsonToken.toString(), ParticipationModel.class);

        return participationModel;
    }

    // test si aime déjâ
    public String testLike (LikeModel like, String token) throws ServerErrorException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/NbJaime/testLike/" + like.getEvenementId() + "/" + like.getUtilisateurId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return stringJSON;
        }
        catch(Exception e){
            throw new ServerErrorException();
        }
    }

    // supprime j'aime
    public LikeModel deleteLike (LikeModel like, String token) throws ServerErrorException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/NbJaime/" + like.getEvenementId() + "/" + like.getUtilisateurId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
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

            return jsonToLikeDelete(stringJSON);
        }
        catch(Exception e){
            throw new ServerErrorException();
        }
    }

    private LikeModel jsonToLikeDelete (String stringJSON) throws Exception {
        LikeModel likeModel;
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        likeModel = object.fromJson(jsonToken.toString(), LikeModel.class);

        return likeModel;
    }

    // test si participe déjâ
    public String testParticipation (ParticipationModel particip, String token) throws ServerErrorException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Participation/testParticipation/" + particip.getEvenementId() + "/" + particip.getParticipantId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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

            return stringJSON;
        }
        catch(Exception e){
            throw new ServerErrorException();
        }
    }

    // supprime participation
    public ParticipationModel deletePart (ParticipationModel participationModel, String token) throws ServerErrorException {
        try{
            URL url = new URL ("https://agendanam2.azurewebsites.net/api/Participation/" + participationModel.getEvenementId() + "/" + participationModel.getParticipantId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
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

            return jsonToParticipeDelete(stringJSON);
        }
        catch(Exception e){
            throw new ServerErrorException();
        }
    }

    private ParticipationModel jsonToParticipeDelete (String stringJSON) throws Exception {
        ParticipationModel participationModel;
        JSONObject jsonToken = new JSONObject(stringJSON);
        Gson object = new GsonBuilder().create();
        participationModel = object.fromJson(jsonToken.toString(), ParticipationModel.class);

        return participationModel;
    }
}
