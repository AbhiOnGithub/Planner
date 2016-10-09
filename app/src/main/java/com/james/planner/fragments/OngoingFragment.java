package com.james.planner.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.james.planner.Planner;
import com.james.planner.R;
import com.james.planner.adapters.NoteAdapter;
import com.james.planner.data.NoteData;
import com.james.planner.dialogs.NoteDialog;
import com.james.planner.utils.StaticUtils;

import java.util.List;

public class OngoingFragment extends SimpleFragment implements Planner.NotesChangeListener {

    private Planner planner;
    private NoteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recycler, container, false);

        planner = (Planner) getContext().getApplicationContext();

        RecyclerView recycler = (RecyclerView) v.findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        adapter = new NoteAdapter(getContext(), planner.getActiveNotes());
        recycler.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteData data = new NoteData();

                NoteDialog dialog = new NoteDialog(getContext(), data);
                dialog.setTransition(NoteDialog.TRANSITION_CIRCLE, StaticUtils.getRelativeLeft(v) + (v.getWidth() / 2), StaticUtils.getRelativeTop(v), v.getWidth(), v.getHeight());
                dialog.show();
            }
        });

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
        return context.getString(R.string.state_ongoing);
    }

    @Override
    public void onActiveNotesChanged(List<NoteData> activeNotes) {
        adapter.setNotes(activeNotes);
    }

    @Override
    public void onDoneNotesChanged(List<NoteData> doneNotes) {
    }
}
