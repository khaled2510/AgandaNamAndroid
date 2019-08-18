package com.henallux.khal.smartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.dao.EventDAO;
import com.henallux.khal.smartcity.activity.model.EventModel;
import com.henallux.khal.smartcity.activity.model.LikeModel;
import com.henallux.khal.smartcity.activity.model.ParticipationModel;

public class EventActivity extends AppCompatActivity {

    private EventModel event;
    private ProgressDialog progressDialog;
    private String messageError;
    private LoadLike loadLike;
    private LoadParticipation loadParticipation;
    private SharedPreferences preferences;
    private AddLike addLike;
    private AddParticipation addParticipation;
    private String token;
    private String pseudo;
    private boolean like;
    private boolean participe;
    private TestLike testLike;
    private TestParticipation testParticipation;
    private ImageButton boutonLike;
    private ImageButton boutonParticip;
    private DeleteLike deleteLike;
    private DeletePart deletePart;
    private TextView viewComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);
        pseudo = preferences.getString("pseudo", null);

        Gson gson = new Gson();
        String json = preferences.getString("eventObject", null);
        event = gson.fromJson(json, EventModel.class);

        TextView titleEvent = (TextView) findViewById(R.id.title_event_id);
        titleEvent.setText(event.getName());

        TextView adresse = (TextView) findViewById(R.id.adresse_event_id);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(event.getRue());
        stringBuilder.append(" ");
        stringBuilder.append(event.getNumero());
        stringBuilder.append(", ");
        stringBuilder.append(event.getCodePostal());
        stringBuilder.append(" ");
        stringBuilder.append(event.getLocalite());
        adresse.setText(stringBuilder);

        final TextView description = (TextView) findViewById(R.id.description_text_id);
        description.setText(event.getDescription());

        if(Connexion.isConnected(EventActivity.this)) {
            loadLike = new LoadLike();
            loadLike.execute(token);
            loadParticipation = new LoadParticipation();
            loadParticipation.execute(token);
            testLike = new TestLike();
            testLike.execute(token);
            testParticipation = new TestParticipation();
            testParticipation.execute(token);
        }
        else {
            Toast.makeText(EventActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }

        boutonLike = (ImageButton) findViewById(R.id.button_like_id);
        boutonParticip = (ImageButton) findViewById(R.id.button_participant_id);

        viewComment = (TextView) findViewById(R.id.view_comment_event_id);
        viewComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCom = new Intent(EventActivity.this, CommentActivity.class);
                intentCom.putExtra("event", event);
                startActivity(intentCom);
            }
        });

        initEventButton();
    }

    public void initEventButton(){
        boutonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(like){
                    if(Connexion.isConnected(EventActivity.this)) {
                        deleteLike = new DeleteLike();
                        deleteLike.execute();
                    }
                    else {
                        messageErrorInternet();
                    }
                }
                else{
                    if(Connexion.isConnected(EventActivity.this)) {
                        addLike = new AddLike();
                        addLike.execute();
                    }
                    else {
                        messageErrorInternet();
                    }
                }
            }
        });

        boutonParticip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(participe){
                    if(Connexion.isConnected(EventActivity.this)) {
                        deletePart = new DeletePart();
                        deletePart.execute();
                    }
                    else {
                        messageErrorInternet();
                    }
                }
                else{
                    if(Connexion.isConnected(EventActivity.this)) {
                        addParticipation = new AddParticipation();
                        addParticipation.execute();
                    }
                    else {
                        messageErrorInternet();
                    }
                }
            }
        });
    }

    public void messageErrorInternet(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(EventActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
            }
        });
    }

    // Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.itemDeco :
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", null);
                Intent intentDec = new Intent(EventActivity.this, MainActivity.class);
                this.startActivity(intentDec);
                return true;
            case R.id.itemSearchByDate :
                Intent intentDate = new Intent(EventActivity.this, SearchByDateActivity.class);
                this.startActivity(intentDate);
                return true;
            case R.id.itemParticipation :
                Intent intentResult = new Intent(EventActivity.this, ParticipationAvtivity.class);
                this.startActivity(intentResult);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TestLike extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            String testLike = null;
            String token = strings[0];
            try{
                testLike = eventDAO.testLike(new LikeModel(pseudo, event.getId()), token);
            } catch (Exception e){
                EventActivity.this.showError(e);
            }
            return testLike;
        }
        @Override
        protected void onPostExecute(String testLike){
            like = Boolean.parseBoolean(testLike);
            if(like){
                boutonLike.setBackgroundColor(getResources().getColor(R.color.yes));
            }
            else{
                boutonLike.setBackgroundColor(getResources().getColor(R.color.no));
            }
        }
    }

    private class TestParticipation extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            String testPart = null;
            String token = strings[0];
            try{
                testPart = eventDAO.testParticipation(new ParticipationModel(pseudo, event.getId()), token);
            } catch (Exception e){
                progressDialog.dismiss();
                EventActivity.this.showError(e);
            }
            return testPart;
        }
        @Override
        protected void onPostExecute(String testPart){
            participe = Boolean.parseBoolean(testPart);
            if(participe){
                boutonParticip.setBackgroundColor(getResources().getColor(R.color.yes));
            }
            else{
                boutonParticip.setBackgroundColor(getResources().getColor(R.color.no));
            }
            progressDialog.dismiss();
        }
    }

    private class LoadLike extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            String likes = null;
            String token = strings[0];
            try{
                likes = eventDAO.getLikes(event.getId(), token);
            } catch (Exception e){
                EventActivity.this.showError(e);
            }
            return likes;
        }
        @Override
        protected void onPostExecute(String likes){
            TextView likesText = (TextView) findViewById(R.id.likes_id);
            likesText.setText(likes);
        }
    }

    private class LoadParticipation extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            String participations = null;
            String token = strings[0];
            try{
                participations = eventDAO.getParticipation(event.getId(), token);
            } catch (Exception e){
                EventActivity.this.showError(e);
            }
            return participations;
        }
        @Override
        protected void onPostExecute(String participations){
            TextView participText = (TextView) findViewById(R.id.participation_id);
            participText.setText(participations);
        }
    }

    private class AddLike extends AsyncTask<String,Void,LikeModel> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected LikeModel doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            LikeModel likeModel = new LikeModel(pseudo, event.getId());
            try{
                likeModel = eventDAO.addLike(likeModel, token);
            } catch (Exception e){
                progressDialog.dismiss();
                EventActivity.this.showError(e);
            }
            return likeModel;
        }

        @Override
        protected void onPostExecute(LikeModel likeModel){
            if(likeModel != null){
                loadLike = new LoadLike();
                loadLike.execute(token);
                like = true;
                boutonLike.setBackgroundColor(getResources().getColor(R.color.yes));
            }
            progressDialog.dismiss();
        }
    }

    private class AddParticipation extends AsyncTask<String,Void,ParticipationModel> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected ParticipationModel doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            ParticipationModel participationModel = new ParticipationModel(pseudo, event.getId());
            try{
                participationModel = eventDAO.addParticipation(participationModel, token);
            } catch (Exception e){
                progressDialog.dismiss();
                EventActivity.this.showError(e);
            }
            return participationModel;
        }

        @Override
        protected void onPostExecute(ParticipationModel participationModel){
            if(participationModel != null){
                loadParticipation = new LoadParticipation();
                loadParticipation.execute(token);
                participe = true;
                boutonParticip.setBackgroundColor(getResources().getColor(R.color.yes));
            }
            progressDialog.dismiss();
        }
    }

    private class DeleteLike extends AsyncTask<String,Void,LikeModel> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected LikeModel doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            LikeModel likeModel = new LikeModel(pseudo, event.getId());
            try{
                likeModel = eventDAO.deleteLike(likeModel, token);
            } catch (Exception e){
                progressDialog.dismiss();
                EventActivity.this.showError(e);
            }
            progressDialog.dismiss();
            return likeModel;
        }

        @Override
        protected void onPostExecute(LikeModel likeModel){
            if(likeModel != null){
                loadLike = new LoadLike();
                loadLike.execute(token);
                like = false;
                boutonLike.setBackgroundColor(getResources().getColor(R.color.no));
            }
            progressDialog.dismiss();
        }
    }

    private class DeletePart extends AsyncTask<String,Void,ParticipationModel> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(EventActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected ParticipationModel doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            ParticipationModel participationModel = new ParticipationModel(pseudo, event.getId());
            try{
                participationModel = eventDAO.deletePart(participationModel, token);
            } catch (Exception e){
                progressDialog.dismiss();
                EventActivity.this.showError(e);
            }
            progressDialog.dismiss();
            return participationModel;
        }

        @Override
        protected void onPostExecute(ParticipationModel participationModel){
            if(participationModel != null){
                loadParticipation = new LoadParticipation();
                loadParticipation.execute(token);
                participe = false;
                boutonParticip.setBackgroundColor(getResources().getColor(R.color.no));
            }
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (loadLike != null)
            loadLike.cancel(true);
        if (loadParticipation != null)
            loadParticipation.cancel(true);
        if (addLike != null)
            addLike.cancel(true);
        if (addParticipation != null)
            addParticipation.cancel(true);
        if (testLike != null)
            testLike.cancel(true);
        if (deleteLike != null)
            deleteLike.cancel(true);
    }

    private void showError(Exception e)
    {
        this.messageError = e.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadLike != null)
                    loadLike.cancel(true);
                if (loadParticipation != null)
                    loadParticipation.cancel(true);
                if (addLike != null)
                    addLike.cancel(true);
                if (addParticipation != null)
                    addParticipation.cancel(true);
                if (testLike != null)
                    testLike.cancel(true);
                if (deleteLike != null)
                    deleteLike.cancel(true);
                Toast.makeText(EventActivity.this,messageError,Toast.LENGTH_LONG).show();
            }
        });
    }
}
