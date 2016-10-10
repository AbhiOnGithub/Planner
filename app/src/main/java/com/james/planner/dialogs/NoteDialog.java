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
import com.james.planner.utils.ImageUtils;

import java.util.List;

public class NoteDialog extends AnimatedDialog {

    private Planner planner;
    private NoteData noteData;

    private RecyclerView recycler;
    private SubNoteAdapter noteAdapter;
    private List<NoteData> datas;

    private AppCompatEditText title;

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

        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(getContext(), 1));

        datas = noteData.getNotes();
        if (datas.size() < 1) datas.add(new NoteData());

        noteAdapter = new SubNoteAdapter(getContext(), datas, new SubNoteAdapter.OnEditListener() {
            @Override
            public void onEdit(NoteData[] notes) {
            }
        });

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
                if (noteData.content == null || noteData.content.length() < 1) {
                    title.setError(getContext().getString(R.string.error_no_text), ImageUtils.getVectorDrawable(getContext(), R.drawable.ic_error));
                    return;
                }

                if (noteAdapter.hasError()) return;

                noteData.setNotes(datas);

                if (noteData.done) planner.setDone(false, noteData);
                else if (!planner.getActiveNotes().contains(noteData)) planner.addNote(noteData);
                else planner.saveNote(noteData);

                dismiss();
            }
        });

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datas.add(new NoteData());
                noteAdapter.setNotes(datas);

                recycler.smoothScrollToPosition(datas.size() - 1);
            }
        });
    }
}
