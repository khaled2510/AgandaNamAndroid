package com.henallux.khal.smartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

public class ResultSearchActivity extends AppCompatActivity {

    private List<EventModel> events;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;
    private LoadEvent loadEvent;
    private String messageError;
    private int idCatego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = preferences.getString("token", null);
        idCatego = preferences.getInt("idCatego",0);

        if(Connexion.isConnected(ResultSearchActivity.this)) {
            loadEvent = new LoadEvent();
            loadEvent.execute(token);
        }
        else {
            Toast.makeText(ResultSearchActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
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
                Intent intentDec = new Intent(ResultSearchActivity.this, MainActivity.class);
                this.startActivity(intentDec);
                return true;
            case R.id.itemSearchByDate :
                Intent intentDate = new Intent(ResultSearchActivity.this, SearchByDateActivity.class);
                this.startActivity(intentDate);
                return true;
            case R.id.itemParticipation :
                Intent intentResult = new Intent(ResultSearchActivity.this, ParticipationAvtivity.class);
                this.startActivity(intentResult);
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
            progressDialog = new ProgressDialog(ResultSearchActivity.this);
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
                eventModels = eventDAO.getEventByCategori(idCatego, token);
            } catch (Exception e){
                progressDialog.dismiss();
                ResultSearchActivity.this.showError(e);
            }
            return eventModels;
        }
        @Override
        protected void onPostExecute(ArrayList<EventModel> eventModels){
            ResultSearchActivity.this.events = eventModels;
            ListView listResultSearch = (ListView) findViewById(R.id.list_result_id);
            listResultSearch.setAdapter(new ResultSearchAdapter(ResultSearchActivity.this, events));
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(loadEvent != null)
        {
            loadEvent.cancel(true);
        }
    }

    private void showError(Exception e)
    {
        this.messageError = e.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadEvent != null)
                    loadEvent.cancel(true);
                Toast.makeText(ResultSearchActivity.this, messageError, Toast.LENGTH_LONG).show();
            }
        });
    }
}
