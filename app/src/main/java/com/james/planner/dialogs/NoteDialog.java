package com.james.planner.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.james.planner.Planner;
import com.james.planner.R;
import com.james.planner.adapters.SubNoteAdapter;
import com.james.planner.data.NoteData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NoteDialog extends AnimatedDialog {

    private Planner planner;
    private NoteData noteData;
    private SubNoteAdapter noteAdapter;

    AppCompatEditText title;

    public NoteDialog(Context context, NoteData noteData) {
        super(context, R.style.AppTheme_Dialog_Fullscreen);
        this.noteData = noteData;
        planner = (Planner) context.getApplicationContext();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_note);
        animateView(findViewById(R.id.root));

        title = (AppCompatEditText) findViewById(R.id.title);
        if (noteData.content != null) title.setText(noteData.content);

        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                noteData.content = title.getText().toString();
                if (noteData.content.length() > 0) title.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        List<NoteData> datas = new ArrayList<>();
        if (noteData.datas != null) datas.addAll(Arrays.asList(noteData.datas));
        noteAdapter = new SubNoteAdapter(getContext(), datas);
        recycler.setAdapter(noteAdapter);

        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.reminder).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteData.done) planner.setDone(false, noteData);
                else if (!planner.getActiveNotes().contains(noteData)) planner.addNote(noteData);
                else planner.saveNote(noteData);

                dismiss();
            }
        });
    }
}
