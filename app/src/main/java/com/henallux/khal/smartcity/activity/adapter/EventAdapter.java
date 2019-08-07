package com.henallux.khal.smartcity.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.model.CommentModel;

import java.util.ArrayList;

public class EventAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<CommentModel> comments;
    private LayoutInflater inflater;

    public EventAdapter (Context context, ArrayList<CommentModel> comments){
        this.context=context;
        this.comments = comments;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() { return comments.size(); }

    @Override
    public Object getItem(int position) { return comments.get(position); }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_comment, null);

        CommentModel comment = comments.get(position);

        TextView textViewPseudo = (TextView) convertView.findViewById(R.id.pseudo_comments_id);
        textViewPseudo.setText(comment.getAuteurId());

        TextView textViewDescription = (TextView) convertView.findViewById(R.id.text_comment_id);
        textViewDescription.setText(comment.getTexte());

        return convertView;
    }
}
