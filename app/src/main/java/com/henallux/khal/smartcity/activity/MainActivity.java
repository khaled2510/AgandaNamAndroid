package com.henallux.khal.smartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.dao.LoginDAO;
import com.henallux.khal.smartcity.activity.model.TokenModel;

public class MainActivity extends AppCompatActivity {

    private Button boutonConnexion, boutonInscription;
    private Intent intentConnexion, intentInscription;
    private EditText pseudo, password;
    private SharedPreferences preferences;
    private Login login; //asyn
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentConnexion = new Intent(MainActivity.this,CategoriesActivity.class);
        intentInscription = new Intent(MainActivity.this,ViewInscription.class);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boutonInscription = (Button) findViewById(R.id.inscription);
        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentInscription);
            }
        });

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

    private class Login extends AsyncTask<String,Void,TokenModel>{

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle("ProgressDialog"); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected TokenModel doInBackground(String ... strings) {
            TokenModel token = new TokenModel();
            LoginDAO user = new LoginDAO();
            String username = strings[0];
            String password = strings[1];
            try {
                token = user.login(username, password);
            }catch (Exception e){
                //MainActivity.this.showError(e);
                Log.i("Test", "token: " + e.getMessage());
            }
            return token;
        }

        @Override
        protected void onPostExecute(TokenModel token) {
            if(token != null){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token.getAccess_token());
                editor.commit();
                //MainActivity.this.startActivity(intentConnexion);
            }
            progressDialog.dismiss();
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
