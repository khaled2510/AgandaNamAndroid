package com.henallux.khal.smartcity.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.adapter.CommentAdapter;
import com.henallux.khal.smartcity.activity.adapter.ResultSearchAdapter;
import com.henallux.khal.smartcity.activity.dao.CommentDAO;
import com.henallux.khal.smartcity.activity.dao.EventDAO;
import com.henallux.khal.smartcity.activity.model.CommentModel;
import com.henallux.khal.smartcity.activity.model.EventModel;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private Context context = this;
    private SharedPreferences preferences;
    private EventModel event;
    private LoadComment loadComment;
    private ProgressDialog progressDialog;
    private String messageError;
    private String pseudo;
    private ArrayList<CommentModel> commentModels;
    protected int pageIndex = 0;
    private static final int PAGESIZE = 5;
    private CommentAdapter commentAdapter;
    private TextView loadMore;
    private String token;
    private Button addCommentButton;
    private AddComment addCommentAsyn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token", null);
        pseudo = preferences.getString("pseudo", null);

        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null)
            event = (EventModel) bundle.getSerializable("event");

        commentModels = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list_comment);
        commentAdapter = new CommentAdapter(CommentActivity.this, commentModels);
        recyclerView.setLayoutManager(new GridLayoutManager(CommentActivity.this,1));
        recyclerView.setAdapter(commentAdapter);

        testConnetionAndLoad();
        loadMore = (TextView) findViewById(R.id.load_more);
        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageIndex += 1;
                testConnetionAndLoad();
            }
        });

        addCommentButton = (Button) findViewById(R.id.add_comment_button);
        addCommentDialog();
    }

    public void testConnetionAndLoad(){
        if(Connexion.isConnected(CommentActivity.this)) {
            loadComment = new LoadComment();
            loadComment.execute();
        }
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CommentActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                }
            });
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
                Intent intentDec = new Intent(CommentActivity.this, MainActivity.class);
                this.startActivity(intentDec);
                return true;
            case R.id.itemSearchByDate :
                Intent intentDate = new Intent(CommentActivity.this, SearchByDateActivity.class);
                this.startActivity(intentDate);
                return true;
            case R.id.itemParticipation :
                Intent intentResult = new Intent(CommentActivity.this, ParticipationAvtivity.class);
                this.startActivity(intentResult);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addCommentDialog(){
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

            LayoutInflater li = LayoutInflater.from(context);
            View promptsView = li.inflate(R.layout.dialog_addcomment, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            alertDialogBuilder.setView(promptsView);

            final EditText userInput = (EditText) promptsView
                    .findViewById(R.id.editTextDialogUserInput);

            alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(R.string.add,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            if(userInput.getText().length() <= 0){
                                Toast.makeText(CommentActivity.this, R.string.error_add_comment, Toast.LENGTH_LONG).show();
                            }
                            else{
                                if(Connexion.isConnected(CommentActivity.this)) {
                                    addCommentAsyn = new AddComment();
                                    addCommentAsyn.execute(userInput.getText().toString());
                                }
                                else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(CommentActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }
                    })
                .setNegativeButton(R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            }
        });
    }

    private class LoadComment extends AsyncTask<String,Void,ArrayList<CommentModel>> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }
        @Override
        protected ArrayList<CommentModel> doInBackground(String... strings) {
            CommentDAO commentDAO = new CommentDAO();
            ArrayList<CommentModel> commentModels = new ArrayList<>();
            try{
                commentModels = commentDAO.getCommentEvent(token, event.getId(), pageIndex, PAGESIZE);
            } catch (Exception e){
                progressDialog.dismiss();
                pageIndex -= 1;
                CommentActivity.this.showError(e);
            }
            return commentModels;
        }
        @Override
        protected void onPostExecute(ArrayList<CommentModel> commentModels){
            CommentActivity.this.commentModels.addAll(commentModels);
            commentAdapter.notifyDataSetChanged();
            progressDialog.dismiss();
        }
    }

    private class AddComment extends AsyncTask<String,Void,CommentModel> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommentActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }
        @Override
        protected CommentModel doInBackground(String... strings) {
            CommentDAO commentDAO = new CommentDAO();
            String text = strings[0];
            CommentModel commentModel = new CommentModel(pseudo,text, event.getId());
            try{
                commentModel = commentDAO.addComment(commentModel, token);
            } catch (Exception e){
                progressDialog.dismiss();
                CommentActivity.this.showError(e);
            }
            return commentModel;
        }
        @Override
        protected void onPostExecute(CommentModel commentModel){
            progressDialog.dismiss();
            if(commentModel != null){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CommentActivity.this, R.string.comment_add_success, Toast.LENGTH_LONG).show();
                        loadComment = new LoadComment();
                        loadComment.execute();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(loadComment != null)
            loadComment.cancel(true);
        if(addCommentAsyn != null)
            addCommentAsyn.cancel(true);

    }

    private void showError(Exception e)
    {
        this.messageError = e.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadComment != null)
                    loadComment.cancel(true);
                if(addCommentAsyn != null)
                    addCommentAsyn.cancel(true);
                Toast.makeText(CommentActivity.this, messageError, Toast.LENGTH_LONG).show();
            }
        });
    }
}
