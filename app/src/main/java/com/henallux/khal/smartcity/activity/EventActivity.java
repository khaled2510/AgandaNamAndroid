package com.henallux.khal.smartcity.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.model.EventModel;

public class EventActivity extends AppCompatActivity {

    private EventModel event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
