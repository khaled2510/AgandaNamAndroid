package com.henallux.khal.smartcity.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.adapter.ResultSearchAdapter;
import com.henallux.khal.smartcity.activity.model.EventModel;

import java.util.ArrayList;
import java.util.List;

public class ResultSearchActivity extends AppCompatActivity {

    private List<EventModel> events;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_search);

        Intent intent = getIntent();
        String token = "";
        if (intent != null){
            if (intent.hasExtra("token")){
                token = intent.getStringExtra("token");
            }
        }

        events = new ArrayList<EventModel>();
        events.add(new EventModel("Aquaman", "le nouveau film DC présenter dans votre cinéma Acinapolis"));
        events.add(new EventModel("Avenger", "le nouveau film Marvel présenter dans votre cinéma Acinapolis"));
        events.add(new EventModel("Le roi lion", "le nouveau film Disney présenter dans votre cinéma Acinapolis"));

        ListView listResultSearch = (ListView) findViewById(R.id.list_result_id);
        listResultSearch.setAdapter(new ResultSearchAdapter(this, events));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

}
