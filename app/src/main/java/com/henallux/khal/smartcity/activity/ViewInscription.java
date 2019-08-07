package com.henallux.khal.smartcity.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;

public class ViewInscription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
    }

    // MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu){    //comment faire pour que toute les activité aille le méme menu sans le rajouté a chaque fois
        this.getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId())
        {
            case R.id.itemDeco :
                Toast.makeText(ViewInscription.this,"permet de se déconnecté",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
