package com.james.planner.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.planner.R;
import com.james.planner.data.NoteData;
import com.james.planner.utils.StaticUtils;

import java.util.Arrays;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<NoteData> notes;

    public NoteAdapter(Context context, List<NoteData> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NoteData note = notes.get(position);

        ViewCompat.setElevation(holder.v.findViewById(R.id.layout), StaticUtils.getPixelsFromDp(context, 2));

        if (note.content != null)
            ((TextView) holder.v.findViewById(R.id.title)).setText(note.content);
        else holder.v.findViewById(R.id.title).setVisibility(View.GONE);

        if (note.datas != null) {
            RecyclerView recycler = (RecyclerView) holder.v.findViewById(R.id.recycler);
            recycler.setLayoutManager(new GridLayoutManager(context, 1));
            recycler.setNestedScrollingEnabled(false);
            recycler.setAdapter(new SubNoteAdapter(context, Arrays.asList(note.datas)));
        } else holder.v.findViewById(R.id.recycler).setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        View v;

        public ViewHolder(View v) {
            super(v);
            this.v = v;
        }
    }
}
