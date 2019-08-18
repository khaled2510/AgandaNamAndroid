package com.henallux.khal.smartcity.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.henallux.khal.smartcity.R;
import com.henallux.khal.smartcity.activity.adapter.CommentAdapter;
import com.henallux.khal.smartcity.activity.model.CommentModel;
import java.util.Arrays;
import java.util.List;

public class CommentListFragment extends Fragment {

    //private List<CommentModel> comments = Arrays.asList(new CommentModel("pseudo", "salut"), new CommentModel("pseudo 2", "salut 2"));

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_list_comment, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_comment);
        CommentAdapter commentAdapter = new CommentAdapter(getActivity(), comments);
        recyclerView.setAdapter(commentAdapter);

        return view;
    }
    */
}
