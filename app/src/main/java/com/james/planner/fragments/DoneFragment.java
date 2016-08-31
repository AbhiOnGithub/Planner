package com.james.planner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.planner.Planner;
import com.james.planner.R;
import com.james.planner.adapters.NoteAdapter;

public class DoneFragment extends SimpleFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recycler.setAdapter(new NoteAdapter(getContext(), ((Planner) getContext().getApplicationContext()).getDoneNotes()));

        return v;
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.state_done);
    }
}
