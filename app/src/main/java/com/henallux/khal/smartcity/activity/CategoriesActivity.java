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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.adapter.CategoryAdapter;
import com.henallux.khal.smartcity.activity.dao.CategoryDAO;
import com.henallux.khal.smartcity.activity.model.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity {

    private List<CategoryModel> categoryModelList;
    private SharedPreferences preferences;
    private LoadCategory loadCategory;
    private ProgressDialog progressDialog;
    private ArrayList<CategoryModel> categories;
    private String messageError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = preferences.getString("token", null);

        if(Connexion.isConnected(CategoriesActivity.this)) {
            loadCategory = new LoadCategory();
            loadCategory.execute(token);
        }
        else {
            Toast.makeText(CategoriesActivity.this, R.string.no_connection, Toast.LENGTH_LONG).show();
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
                Intent intentDec = new Intent(CategoriesActivity.this, MainActivity.class);
                this.startActivity(intentDec);
                return true;
            case R.id.itemSearchByDate :
                Intent intentDate = new Intent(CategoriesActivity.this, SearchByDateActivity.class);
                this.startActivity(intentDate);
                return true;
            case R.id.itemParticipation :
                Intent intentResult = new Intent(CategoriesActivity.this, ParticipationAvtivity.class);
                this.startActivity(intentResult);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class LoadCategory extends AsyncTask<String,Void,ArrayList<CategoryModel>>{
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CategoriesActivity.this);
            progressDialog.setMessage("Loading..."); // Setting Message
            progressDialog.setTitle(R.string.chargement); // Setting Title
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
            progressDialog.show(); // Display Progress Dialog
            progressDialog.setCancelable(false);
        }
        @Override
        protected ArrayList<CategoryModel> doInBackground(String... strings) {
            CategoryDAO categoryDAO = new CategoryDAO();
            ArrayList<CategoryModel> categories = new ArrayList<>();
            String token = strings[0];
            try{
                categories = categoryDAO.getAllCategories(token);
            } catch (Exception e){
                progressDialog.dismiss();
                CategoriesActivity.this.showError(e);
            }
            return categories;
        }
        @Override
        protected void onPostExecute(ArrayList<CategoryModel> categories){
            CategoriesActivity.this.categories = categories;
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.categories_recyclerview_id);
            CategoryAdapter categoryAdapter = new CategoryAdapter(CategoriesActivity.this, CategoriesActivity.this.categories);
            recyclerView.setLayoutManager(new GridLayoutManager(CategoriesActivity.this,3));
            recyclerView.setAdapter(categoryAdapter);
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if(loadCategory != null)
        {
            loadCategory.cancel(true);
        }
    }

    private void showError(Exception e)
    {
        this.messageError = e.getMessage();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (loadCategory != null)
                    loadCategory.cancel(true);
                Display display = getWindowManager(). getDefaultDisplay();
                Point size = new Point();
                display. getSize(size);
                int width = size. x;
                int height = size. y;

                ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.main);
                TextView errorDisplayer = new TextView(CategoriesActivity.this);
                errorDisplayer.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,ConstraintLayout.LayoutParams.WRAP_CONTENT));
                String message = CategoriesActivity.this.messageError;
                errorDisplayer.setText(message);
                errorDisplayer.setTextColor(Color.RED);
                int padding = (10 * message.length());
                errorDisplayer.setPadding(250, 850, 20, 20);// in pixels (left, top, right, bottom)
                layout.addView(errorDisplayer);
            }
        });
    }
}
