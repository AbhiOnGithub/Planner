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
    private List<NoteData> notes;
    private View empty;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        planner = (Planner) getContext().getApplicationContext();

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        empty = v.findViewById(R.id.empty);

        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        notes = planner.getDoneNotes();
        noteAdapter = new NoteAdapter(getContext(), recycler, notes);
        recycler.setAdapter(noteAdapter);

        empty.setVisibility(notes.size() > 0 ? View.GONE : View.VISIBLE);

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
        notes = doneNotes;
        noteAdapter.setNotes(notes);

        empty.setVisibility(notes.size() > 0 ? View.GONE : View.VISIBLE);
    }
}
