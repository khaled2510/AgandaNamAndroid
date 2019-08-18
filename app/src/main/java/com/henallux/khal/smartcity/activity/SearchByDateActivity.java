package com.henallux.khal.smartcity.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.adapter.ResultSearchAdapter;
import com.henallux.khal.smartcity.activity.dao.EventDAO;
import com.henallux.khal.smartcity.activity.exception.ServerErrorException;
import com.henallux.khal.smartcity.activity.model.EventModel;
import com.henallux.khal.smartcity.activity.utils.ConvertionDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SearchByDateActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private TextView mDisplayDateStart, getmDisplayDateEnd;
    private DatePickerDialog.OnDateSetListener mDateSetListener, mDateSetListener2;
    private SharedPreferences preferences;
    private Date dateDebut;
    private Date dateFin;
    private String token;
    private ProgressDialog progressDialog;
    private LoadEvent loadEvent;
    private String messageError;
    private List<EventModel> events;
    private ListView listResultSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);

        listResultSearch = (ListView) findViewById(R.id.list_result_date_id);
        initDatePicker();

        Button lancerRecherche = (Button) findViewById(R.id.button_search);
        lancerRecherche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateDebut == null || dateFin == null){
                    Toast.makeText(SearchByDateActivity.this,R.string.date_requise,Toast.LENGTH_LONG).show();
                }
                else {
                    if(dateDebut.after(dateFin)){
                        Toast.makeText(SearchByDateActivity.this,R.string.erreur_date,Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        if(Connexion.isConnected(SearchByDateActivity.this)) {
                            loadEvent = new LoadEvent();
                            loadEvent.execute(token);
                        }
                        else {
                            Toast.makeText(SearchByDateActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }

    public void initDatePicker(){
        mDisplayDateStart = (TextView) findViewById(R.id.tvDate);
        mDisplayDateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SearchByDateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateDebut = (Date) new Date(year, month, dayOfMonth);
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                mDisplayDateStart.setText(date);
            }
        };

        getmDisplayDateEnd = (TextView) findViewById(R.id.tvDate2);
        getmDisplayDateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SearchByDateActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener2,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateFin = (Date) new Date(year, month, dayOfMonth);
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                getmDisplayDateEnd.setText(date);
            }
        };
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
                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", null);
                Intent intentDec = new Intent(SearchByDateActivity.this, MainActivity.class);
                this.startActivity(intentDec);
                return true;
            case R.id.itemSearchByDate :
                Toast.makeText(SearchByDateActivity.this,R.string.recherche_par_date,Toast.LENGTH_LONG).show();
                return true;
            case R.id.itemParticipation :
                Intent intentResult = new Intent(SearchByDateActivity.this, ParticipationAvtivity.class);
                this.startActivity(intentResult);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LoadEvent extends AsyncTask<String,Void,ArrayList<EventModel>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(SearchByDateActivity.this);
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
            try {
                eventModels = eventDAO.getEventByDate(dateDebut, dateFin, token);
            } catch (Exception e) {
                progressDialog.dismiss();
                SearchByDateActivity.this.showError(e);
            }
            return eventModels;
        }

        @Override
        protected void onPostExecute(ArrayList<EventModel> eventModels) {
            SearchByDateActivity.this.events = eventModels;
            listResultSearch.setAdapter(new ResultSearchAdapter(SearchByDateActivity.this, events));
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
        this.messageError = e.getMessage();        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadEvent != null)
                    loadEvent.cancel(true);
                listResultSearch.setAdapter(null);
                Toast.makeText(SearchByDateActivity.this, messageError, Toast.LENGTH_LONG).show();
            }
        });
    }
}
