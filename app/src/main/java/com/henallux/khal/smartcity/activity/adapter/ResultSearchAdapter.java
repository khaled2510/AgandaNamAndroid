package com.henallux.khal.smartcity.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.EventActivity;
import com.henallux.khal.smartcity.activity.model.EventModel;

import java.util.List;

public class ResultSearchAdapter extends BaseAdapter {

    private Context context;
    private List<EventModel> events;
    private LayoutInflater inflater;


    public ResultSearchAdapter(Context context, List<EventModel> events) {
        this.context = context;
        this.events = events;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public EventModel getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_listing_result,null);

        EventModel event = events.get(position);

        TextView txtViewTitleResultSearch = convertView.findViewById(R.id.title_result_id);
        txtViewTitleResultSearch.setText(event.getName());

        TextView txtViewDescriptionResultSearch = convertView.findViewById(R.id.text_description_result_id);
        txtViewDescriptionResultSearch.setText(event.getDescription());

        convertView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                intent.putExtra("Title", events.get(position).getName());
                intent.putExtra("Description", events.get(position).getDescription());


                context.startActivity(intent);

            }
        });


        return convertView;
    }
}
