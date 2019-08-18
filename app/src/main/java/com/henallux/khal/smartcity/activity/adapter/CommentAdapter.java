package com.henallux.khal.smartcity.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.model.CommentModel;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    private Context context;
    private List<CommentModel> commentModels;

    public CommentAdapter(Context context, List<CommentModel> commentModels) {
        this.context = context;
        this.commentModels = commentModels;
    }

    @Override
    public CommentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.cardview_comment,parent,false);
        return new CommentAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.pseudo.setText(commentModels.get(position).getAuteurId());
        holder.commentText.setText(commentModels.get(position).getTexte());
    }

    @Override
    public int getItemCount() {
        return commentModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView pseudo, commentText;

        public MyViewHolder (View commentView){
            super(commentView);

            pseudo = (TextView) commentView.findViewById(R.id.pseudo_comment_id);
            commentText = (TextView) commentView.findViewById(R.id.text_comment_id);
        }
    }
}
