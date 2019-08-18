package com.henallux.khal.smartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.dao.LoginDAO;
import com.henallux.khal.smartcity.activity.model.TokenModel;

public class MainActivity extends AppCompatActivity {

    private Button boutonConnexion;
    private TextView boutonInscription;
    private Intent intentConnexion, intentInscription;
    private EditText pseudo, password;
    private SharedPreferences preferences;
    private Login login; //asyn
    private ProgressDialog progressDialog;
    private String messageError;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intentConnexion = new Intent(MainActivity.this,CategoriesActivity.class);
        intentInscription = new Intent(MainActivity.this,ViewInscription.class);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        boutonInscription = (TextView) findViewById(R.id.inscription);
        boutonInscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intentInscription);
            }
        });

        pseudo = (EditText) findViewById(R.id.pseudo);
        password = (EditText) findViewById(R.id.password);
        awesomeValidation.addValidation(this, R.id.pseudo, "[A-z0-9 ]{4,}", R.string.pseudo_error);
        awesomeValidation.addValidation(this, R.id.password, "[A-z0-9]{3,}", R.string.mdp_error);

        boutonConnexion = (Button) findViewById(R.id.connexion);
        boutonConnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    if(Connexion.isConnected(MainActivity.this)) {
                        login = new Login ();
                        login.execute(pseudo.getText().toString(), password.getText().toString());
                    }
                    else {
                        Toast.makeText(MainActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                    }
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
            progressDialog.setTitle(R.string.connexion); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected TokenModel doInBackground(String ... strings) {
            TokenModel token = null;
            LoginDAO user = new LoginDAO();
            String username = strings[0];
            String password = strings[1];
            try {
                token = user.login(username, password);
            }catch (Exception e){
                progressDialog.dismiss();
                MainActivity.this.showError(e);
            }
            return token;
        }

        @Override
        protected void onPostExecute(TokenModel token) {
            if(token != null){
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("token", token.getAccess_token());
                editor.putString("pseudo", pseudo.getText().toString());
                editor.commit();
                MainActivity.this.startActivity(intentConnexion);
            }
            progressDialog.dismiss();
        }
    }

    private void showError(Exception e)
    {
        this.messageError = e.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (login != null)
                    login.cancel(true);

                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main);
                TextView errorDisplayer = new TextView(MainActivity.this);
                errorDisplayer.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT));
                String message = messageError;
                errorDisplayer.setText(message);
                errorDisplayer.setTextColor(Color.RED);
                errorDisplayer.setPadding(240, 980, 20, 20);// in pixels (left, top, right, bottom)
                layout.addView(errorDisplayer);
            }
        });
    }
}
