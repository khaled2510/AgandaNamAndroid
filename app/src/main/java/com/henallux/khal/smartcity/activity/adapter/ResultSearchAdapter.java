package com.henallux.khal.smartcity.activity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.EventActivity;
import com.henallux.khal.smartcity.activity.model.EventModel;
import com.henallux.khal.smartcity.activity.utils.ConvertionDate;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ResultSearchAdapter extends BaseAdapter {

    private Context context;
    private List<EventModel> events;
    private LayoutInflater inflater;
    private ConvertionDate convertionDate;
    private SharedPreferences preferences;

    public ResultSearchAdapter(Context context, List<EventModel> events) {
        this.context = context;
        this.events = events;
        this.inflater = LayoutInflater.from(context);
        this.convertionDate = new ConvertionDate();
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

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_listing_result,null);

        final EventModel event = events.get(position);

        TextView txtViewTitleResultSearch = convertView.findViewById(R.id.title_result_id);
        txtViewTitleResultSearch.setText(event.getName());

        TextView txtViewDescriptionResultSearch = convertView.findViewById(R.id.text_description_result_id);
        txtViewDescriptionResultSearch.setText(event.getDescription());

        TextView txtViewDateResultSearch = convertView.findViewById(R.id.text_date_creation);
        StringBuilder dateString = new StringBuilder();
        for(int i = 0; i < event.getPresentation().size(); i ++){
            dateString.append("Date de début : ");
            dateString.append(convertionDate.convertDate(event.getPresentation().get(i).getDateHeureDebut()));
            dateString.append("\n");
            dateString.append("Date de fin : ");
            dateString.append(convertionDate.convertDate(event.getPresentation().get(i).getDateHeureFin()));
            dateString.append("\n");
        }
        txtViewDateResultSearch.setText(dateString);

        convertView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventActivity.class);
                preferences = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(event);
                editor.putString("eventObject", json);
                editor.commit();
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
