package com.henallux.khal.smartcity.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String token = preferences.getString("token", null);

        //Intent intent = getIntent();
        //String token = "";
        //if (intent != null){
         //   if (intent.hasExtra("token")){
          //      token = intent.getStringExtra("token");
           // }
        //}
        loadCategory = new LoadCategory(this);
        loadCategory.execute(token);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        this.getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class LoadCategory extends AsyncTask<String,Void,ArrayList<CategoryModel>>{

        private Context context;
        private Activity activity;
        private String token;

        public LoadCategory(Activity activity) {
            this.context = activity.getApplicationContext();
            this.activity = activity;
        }

        @Override
        protected ArrayList<CategoryModel> doInBackground(String... strings) {
            CategoryDAO categoryDAO = new CategoryDAO();
            ArrayList<CategoryModel> categories = new ArrayList<>();
            token = strings[0];
            try{
                categories = categoryDAO.getAllCategories(token);
            } catch (Exception e){

            }
            return categories;
        }

        protected void onPostExecute(ArrayList<CategoryModel> categories){

            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.categories_recyclerview_id);
            CategoryAdapter categoryAdapter = new CategoryAdapter(context, categories, token);
            recyclerView.setLayoutManager(new GridLayoutManager(context,3));
            recyclerView.setAdapter(categoryAdapter);
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
}
