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
import com.james.planner.data.NoteData;

import java.util.List;

public class DoneFragment extends SimpleFragment implements Planner.NotesChangeListener {

    private Planner planner;
    private NoteAdapter noteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        planner = (Planner) getContext().getApplicationContext();

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        noteAdapter = new NoteAdapter(getContext(), ((Planner) getContext().getApplicationContext()).getDoneNotes());
        recycler.setAdapter(noteAdapter);

        v.findViewById(R.id.fab).setVisibility(View.GONE);

        planner.addNotesChangeListener(this);

        return v;
    }

    @Override
    public void onDestroy() {
        planner.removeNotesChangeListener(this);
        super.onDestroy();
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.state_done);
    }

    @Override
    public void onActiveNotesChanged(List<NoteData> activeNotes) {
    }

    @Override
    public void onDoneNotesChanged(List<NoteData> doneNotes) {
        noteAdapter.setNotes(doneNotes);
    }
}
