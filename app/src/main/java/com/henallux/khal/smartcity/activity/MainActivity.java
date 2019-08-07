package com.henallux.khal.smartcity.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.dao.LoginDAO;
import com.henallux.khal.smartcity.activity.model.TokenModel;

import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;

public class MainActivity extends AppCompatActivity {

    private Button boutonConnexion, boutonInscription;
    private Intent intentConnexion, intentInscription;
    private EditText pseudo, password;
    private SharedPreferences preferences;
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentConnexion = new Intent(MainActivity.this,CategoriesActivity.class);
        intentInscription = new Intent(MainActivity.this,ViewInscription.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boutonConnexion = (Button) findViewById(R.id.connexion);
        boutonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pseudo = (EditText) findViewById(R.id.pseudo);
                password = (EditText) findViewById(R.id.password);
                if(Connexion.isConnected(MainActivity.this)) {
                    login = new Login ();
                    login.execute(pseudo.getText().toString(), password.getText().toString());
                }
                else {
                    Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                }
            }
        });
        boutonInscription = (Button) findViewById(R.id.inscription);
        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentInscription);
            }
        });
    }

    private class Login extends AsyncTask<String,Void,TokenModel>{

        @Override
        protected TokenModel doInBackground(String ... strings) {
            TokenModel token = null;
            LoginDAO user = new LoginDAO();
            String username = strings[0];
            String password = strings[1];
            try {
                token = user.login(username, password);
            }catch (Exception e){
                MainActivity.this.showError(e);
            }
            return  token;
        }

        @Override
        protected void onPostExecute(TokenModel token) {
            if (token != null){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token.getAccess_token());
                editor.commit();

                MainActivity.this.startActivity(intentConnexion);
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(login != null)
        {
            login.cancel(true);
        }
    }

    private void showError(Exception e)
    {
        super.onDestroy();
        if (login != null)
            login.cancel(true);
        Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
    }
}
