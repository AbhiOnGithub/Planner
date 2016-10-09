package com.james.planner.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.james.planner.Planner;
import com.james.planner.R;
import com.james.planner.data.NoteData;
import com.james.planner.dialogs.NoteDialog;
import com.james.planner.utils.ImageUtils;
import com.james.planner.utils.StaticUtils;

import java.util.Arrays;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private Context context;
    private List<NoteData> notes;
    private Planner planner;

    private Integer expandedItem;

    public NoteAdapter(Context context, List<NoteData> notes) {
        this.context = context;
        this.notes = notes;
        planner = (Planner) context.getApplicationContext();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NoteData note = notes.get(position);

        ViewCompat.setElevation(holder.v.findViewById(R.id.layout), StaticUtils.getPixelsFromDp(context, expandedItem != null && expandedItem == position ? 3 : 1));

        TextView textView = (TextView) holder.v.findViewById(R.id.title);
        if (note.content != null) {
            textView.setText(note.content);
        } else textView.setVisibility(View.GONE);

        RecyclerView recycler = (RecyclerView) holder.v.findViewById(R.id.recycler);
        if (note.datas != null) {
            recycler.setLayoutManager(new GridLayoutManager(context, 1));
            recycler.setNestedScrollingEnabled(false);
            recycler.setAdapter(new SubNoteAdapter(context, Arrays.asList(note.datas)));
        } else recycler.setVisibility(View.GONE);

        holder.v.findViewById(R.id.actions).setVisibility(expandedItem != null && expandedItem == position ? View.VISIBLE : View.GONE);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer previousItem;

                View actions = v.findViewById(R.id.actions);
                if (actions.getVisibility() == View.VISIBLE) {
                    actions.setVisibility(View.GONE);
                    expandedItem = null;
                } else {
                    actions.setVisibility(View.VISIBLE);
                    previousItem = expandedItem;
                    expandedItem = holder.getAdapterPosition();
                    if (previousItem != null) notifyItemChanged(previousItem);
                }

                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        holder.v.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle(R.string.action_delete_note).setMessage(R.string.confirm_delete_note).setPositiveButton(R.string.action_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        expandedItem = null;
                        planner.removeNote(notes.get(holder.getAdapterPosition()));
                        dialog.dismiss();
                    }
                }).setNegativeButton(R.string.action_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });

        AppCompatImageView archive = (AppCompatImageView) holder.v.findViewById(R.id.archive);
        archive.setImageDrawable(ImageUtils.getVectorDrawable(context, note.done ? R.drawable.ic_unarchive : R.drawable.ic_archive));
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandedItem = null;
                NoteData note = notes.get(holder.getAdapterPosition());
                planner.setDone(!note.done, note);
            }
        });

        holder.v.findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoteDialog dialog = new NoteDialog(context, notes.get(holder.getAdapterPosition()));
                dialog.show();
            }
        });
    }

    public void setNotes(List<NoteData> notes) {
        this.notes = notes;
        notifyDataSetChanged();
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
