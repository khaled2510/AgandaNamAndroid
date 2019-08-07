package com.henallux.khal.smartcity.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.CategoriesActivity;
import com.henallux.khal.smartcity.activity.ResultSearchActivity;
import com.henallux.khal.smartcity.activity.model.CategoryModel;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;
    private List<CategoryModel> categoryModels;
    Intent intentResult ;
    String token;



    public CategoryAdapter(Context context, List<CategoryModel> categoryModels, String token) {
        this.context = context;
        this.categoryModels = categoryModels;
        this.intentResult = new Intent(context, ResultSearchActivity.class);
        this.token = token;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_category,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.tvCategoryLabel.setText(categoryModels.get(position).getlibelle());
        //holder.imgvCategory.setImageResource("@drawable/"+categoryModels.get(position).getlibelle()+".png");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentResult.putExtra("token", token);
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


}
