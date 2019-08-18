package com.henallux.khal.smartcity.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.dao.UserDAO;
import com.henallux.khal.smartcity.activity.model.TokenModel;
import com.henallux.khal.smartcity.activity.model.UserModel;

public class ViewInscription extends AppCompatActivity {

    private AwesomeValidation awesomeValidation;
    protected ProgressDialog progressDialog;
    private EditText pseudo, nom, prenom, email, motDePasse, comfirmMdp;
    private Button valide;
    private Intent intentConnexion;
    protected AddAccount addAccount;
    protected String messageError;
    protected UserModel userModel;
    protected String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        intentConnexion = new Intent(ViewInscription.this,MainActivity.class);

        pseudo = (EditText) findViewById(R.id.editTextPseudo);
        nom = (EditText) findViewById(R.id.editTextNom);
        prenom = (EditText) findViewById(R.id.editTextPrenom);
        email = (EditText) findViewById(R.id.editTextEmail);
        motDePasse = (EditText) findViewById(R.id.editTextMotDePasse);
        comfirmMdp = (EditText) findViewById(R.id.editTextMotDePasseComfirmation);
        valide = (Button) findViewById(R.id.buttonValide);
        awesomeValidation.addValidation(this, R.id.editTextPseudo, "[A-z0-9 ]{4,}", R.string.pseudo_error);
        awesomeValidation.addValidation(this, R.id.editTextNom, RegexTemplate.NOT_EMPTY, R.string.name_error);
        awesomeValidation.addValidation(this, R.id.editTextPrenom, RegexTemplate.NOT_EMPTY, R.string.firstname_error);
        awesomeValidation.addValidation(this, R.id.editTextEmail, Patterns.EMAIL_ADDRESS, R.string.email_error);
        awesomeValidation.addValidation(this, R.id.editTextMotDePasse, "[A-z0-9]{3,}", R.string.mdp_error);
        awesomeValidation.addValidation(this, R.id.editTextMotDePasseComfirmation, "[A-z0-9]{3,}", R.string.mdp_error);

        valide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (awesomeValidation.validate()){
                    if(motDePasse.getText().toString().equals(comfirmMdp.getText().toString())){
                        if(Connexion.isConnected(ViewInscription.this)) {
                            userModel = new UserModel(pseudo.getText().toString(),
                                    nom.getText().toString(),
                                    prenom.getText().toString(),
                                    email.getText().toString(),
                                    motDePasse.getText().toString());
                            addAccount = new AddAccount ();
                            addAccount.execute();
                        }
                        else {
                            Toast.makeText(ViewInscription.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(ViewInscription.this, R.string.comfirm_mdp_error, Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(addAccount != null)
        {
            addAccount.cancel(true);
        }
    }

    private class AddAccount extends AsyncTask<String, Void, UserModel> {

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewInscription.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }

        @Override
        protected UserModel doInBackground(String ... strings) {
            UserDAO addUser = new UserDAO();
            UserModel user = null;
            try {
                user = addUser.addUser(userModel);
            }catch (Exception e){
                progressDialog.dismiss();
                ViewInscription.this.showError(e);
            }
            return user;
        }

        @Override
        protected void onPostExecute(UserModel user) {
            if(user != null){
                Toast.makeText(ViewInscription.this, R.string.account_create, Toast.LENGTH_LONG).show();
                startActivity(intentConnexion);
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
                if (addAccount != null)
                    addAccount.cancel(true);
                Toast.makeText(ViewInscription.this, messageError, Toast.LENGTH_LONG).show();
            }
        });
    }
}
