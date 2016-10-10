package com.james.planner.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.planner.R;
import com.james.planner.data.NoteData;
import com.james.planner.utils.ImageUtils;

import java.util.Arrays;
import java.util.List;

public class SubNoteAdapter extends RecyclerView.Adapter<SubNoteAdapter.ViewHolder> {

    private Context context;
    private List<NoteData> notes;

    private OnEditListener onEditListener;

    public SubNoteAdapter(Context context, List<NoteData> notes) {
        this.context = context;
        this.notes = notes;
    }

    public SubNoteAdapter(Context context, List<NoteData> notes, OnEditListener onEditListener) {
        this.context = context;
        this.notes = notes;
        this.onEditListener = onEditListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(onEditListener != null ? R.layout.item_sub_note_editable : R.layout.item_sub_note, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NoteData note = notes.get(position);

        if (onEditListener != null) {
            AppCompatEditText editText = (AppCompatEditText) holder.v.findViewById(R.id.title);
            editText.setText(note.content);

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int position = holder.getAdapterPosition();
                    NoteData note = getNote(position);
                    if (note == null) return;

                    AppCompatEditText editText = (AppCompatEditText) holder.v.findViewById(R.id.title);
                    note.content = editText.getText().toString();

                    if (note.content.length() < 1)
                        editText.setError(context.getString(R.string.error_no_text), ImageUtils.getVectorDrawable(context, R.drawable.ic_error));
                    if (onEditListener != null)
                        onEditListener.onEdit(notes.toArray(new NoteData[notes.size()]));
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            editText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_DEL && ((AppCompatEditText) holder.v.findViewById(R.id.title)).getText().length() < 1) {
                        int position = holder.getAdapterPosition();
                        if (position > 0 && position < notes.size()) {
                            notes.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                    return false;
                }
            });

            holder.v.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    NoteData note = getNote(position);
                    if (note == null) return;

                    if (note.datas == null) note.datas = new NoteData[]{new NoteData()};
                    else {
                        NoteData[] datas = Arrays.copyOf(note.datas, note.datas.length + 1);
                        datas[note.datas.length] = new NoteData();
                        note.datas = datas;
                    }

                    notifyItemChanged(position);
                }
            });
        } else {
            TextView textView = (TextView) holder.v.findViewById(R.id.title);
            if (note.content != null) textView.setText(note.content);
        }

        if (note.datas != null) {
            RecyclerView recycler = (RecyclerView) holder.v.findViewById(R.id.recycler);
            recycler.setVisibility(View.VISIBLE);
            recycler.setLayoutManager(new GridLayoutManager(context, 1));
            recycler.setNestedScrollingEnabled(false);

            SubNoteAdapter subNoteAdapter;
            if (onEditListener != null) {
                subNoteAdapter = new SubNoteAdapter(context, note.getNotes(), new OnEditListener() {
                    @Override
                    public void onEdit(NoteData[] notes) {
                        NoteData note = getNote(holder.getAdapterPosition());
                        if (note == null) return;
                        note.datas = notes;

                        if (onEditListener != null)
                            onEditListener.onEdit(SubNoteAdapter.this.notes.toArray(new NoteData[SubNoteAdapter.this.notes.size()]));
                    }
                });
            } else subNoteAdapter = new SubNoteAdapter(context, note.getNotes());

            recycler.setAdapter(subNoteAdapter);
        } else holder.v.findViewById(R.id.recycler).setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<NoteData> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public boolean hasError() {
        for (NoteData note : notes) {
            if (note.content == null || note.content.length() < 1 || (note.datas != null && hasError(note.datas)))
                return true;
        }
        return false;
    }

    private boolean hasError(NoteData[] notes) {
        for (NoteData note : notes) {
            if (note.content == null || note.content.length() < 1 || (note.datas != null && hasError(note.datas)))
                return true;
        }
        return false;
    }

    @Nullable
    private NoteData getNote(int position) {
        if (position < 0 || position > notes.size()) return null;
        else return notes.get(position);
    }

    public interface OnEditListener {
        void onEdit(NoteData[] notes);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
