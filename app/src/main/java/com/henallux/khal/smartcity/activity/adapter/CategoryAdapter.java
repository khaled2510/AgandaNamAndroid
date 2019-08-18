package com.henallux.khal.smartcity.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.ResultSearchActivity;
import com.henallux.khal.smartcity.activity.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryModel> categoryModels;
    private Intent intentResult ;
    private SharedPreferences preferences;

    public CategoryAdapter(Context context, List<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.intentResult = new Intent(context, ResultSearchActivity.class);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_category,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tvCategoryLabel.setText(categoryModels.get(position).getlibelle());
        holder.imgvCategory.setImageResource(searchImage(categoryModels.get(position).getId()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("idCatego", categoryModels.get(position).getId());
                editor.commit();
                context.startActivity(intentResult);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvCategoryLabel;
        private ImageView imgvCategory;
        private CardView cardView;

        public MyViewHolder (View categoryView){
            super(categoryView);

            tvCategoryLabel = (TextView) categoryView.findViewById(R.id.category_label_id);
            imgvCategory = (ImageView) categoryView.findViewById(R.id.category_img_id);
            cardView = (CardView) categoryView.findViewById(R.id.category_cardview_id);
        }
    }

    public int searchImage(int idCatego){
        int result = 0;
        switch (idCatego){
            case 1 :
                result = R.drawable.soiree;
                break;
            case 2 :
                result = R.drawable.barcafe;
                break;
            case 3 :
                result = R.drawable.cinema;
                break;
            case 4 :
                result = R.drawable.festival;
                break;
            case 5 :
                result = R.drawable.concert;
                break;
            case 6 :
                result = R.drawable.sport;
                break;
            case 7 :
                result = R.drawable.loisir;
                break;
            case 8 :
                result = R.drawable.foiresalon;
                break;
            case 9 :
                result = R.drawable.conference;
                break;
            case 10 :
                result = R.drawable.theatre;
                break;
            case 11 :
                result = R.drawable.exposition;
                break;
            case 12 :
                result = R.drawable.musees;
                break;
            default:
                result = R.drawable.fondbleufoncer;
                break;
        }
        return result;
    }
}
