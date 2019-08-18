package com.henallux.khal.smartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.adapter.ResultSearchAdapter;
import com.henallux.khal.smartcity.activity.dao.EventDAO;
import com.henallux.khal.smartcity.activity.model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class ParticipationAvtivity extends Activity {

    private List<EventModel> events;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private String messageError;
    private LoadEvent loadEvent;
    private String pseudo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participation);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = preferences.getString("token", null);
        pseudo = preferences.getString("pseudo", null);

        if(Connexion.isConnected(ParticipationAvtivity.this)) {
            loadEvent = new LoadEvent();
            loadEvent.execute(token);
        }
        else {
            Toast.makeText(ParticipationAvtivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
        }
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
                Intent intentDec = new Intent(ParticipationAvtivity.this, MainActivity.class);
                this.startActivity(intentDec);
                return true;
            case R.id.itemSearchByDate :
                Intent intentDate = new Intent(ParticipationAvtivity.this, SearchByDateActivity.class);
                this.startActivity(intentDate);
                return true;
            case R.id.itemParticipation :
                Toast.makeText(ParticipationAvtivity.this,R.string.par_participation,Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LoadEvent extends AsyncTask<String,Void,ArrayList<EventModel>> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ParticipationAvtivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }
        @Override
        protected ArrayList<EventModel> doInBackground(String... strings) {
            EventDAO eventDAO = new EventDAO();
            ArrayList<EventModel> eventModels = new ArrayList<>();
            String token = strings[0];
            try{
                eventModels = eventDAO.getEventByParticipation(pseudo, token);
            } catch (Exception e){
                progressDialog.dismiss();
                ParticipationAvtivity.this.showError(e);
            }
            return eventModels;
        }
        @Override
        protected void onPostExecute(ArrayList<EventModel> eventModels){
            ParticipationAvtivity.this.events = eventModels;
            ListView listResultSearch = (ListView) findViewById(R.id.list_result_id_participe);
            listResultSearch.setAdapter(new ResultSearchAdapter(ParticipationAvtivity.this, events));
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(loadEvent != null)
            loadEvent.cancel(true);

    }

    private void showError(Exception e)
    {
        this.messageError = e.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadEvent != null)
                    loadEvent.cancel(true);
                Toast.makeText(ParticipationAvtivity.this, messageError, Toast.LENGTH_LONG).show();
            }
        });
    }
}
