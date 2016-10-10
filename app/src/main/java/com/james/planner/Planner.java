package com.james.planner;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.james.planner.data.NoteData;

import java.util.ArrayList;
import java.util.List;

public class Planner extends Application {

    private static final String PREF_NOTE = "com.james.planner.PREF_NOTE";
    private static final String PREF_NOTES_SIZE = "com.james.planner.PREF_NOTES_SIZE";

    private SharedPreferences prefs;
    public List<NoteData> notes;
    private List<NotesChangeListener> listeners;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        listeners = new ArrayList<>();
        notes = new ArrayList<>();

        int size = prefs.getInt(PREF_NOTES_SIZE, 0);
        for (int i = 0; i < size; i++) {
            if (prefs.contains(PREF_NOTE + i))
                notes.add(new Gson().fromJson(prefs.getString(PREF_NOTE + i, null), NoteData.class));
        }
    }

    public List<NoteData> getActiveNotes() {
        List<NoteData> notes = new ArrayList<>();
        for (NoteData note : this.notes) {
            if (!note.done) notes.add(note);
        }
        return notes;
    }

    public List<NoteData> getDoneNotes() {
        List<NoteData> notes = new ArrayList<>();
        for (NoteData note : this.notes) {
            if (note.done) notes.add(note);
        }
        return notes;
    }

    public void addNote(NoteData note) {
        notes.add(note);
        saveNote(note);
    }

    public void saveNote(NoteData note) {
        saveNotes();

        if (note.done) {
            List<NoteData> doneNotes = getDoneNotes();
            for (NotesChangeListener listener : listeners) {
                listener.onDoneNotesChanged(doneNotes);
            }
        } else {
            List<NoteData> activeNotes = getActiveNotes();
            for (NotesChangeListener listener : listeners) {
                listener.onActiveNotesChanged(activeNotes);
            }
        }
    }

    public void setDone(boolean done, NoteData note) {
        note.done = done;
        saveNotes();

        List<NoteData> activeNotes = getActiveNotes(), doneNotes = getDoneNotes();
        for (NotesChangeListener listener : listeners) {
            listener.onActiveNotesChanged(activeNotes);
            listener.onDoneNotesChanged(doneNotes);
        }
    }

    public void removeNote(NoteData note) {
        notes.remove(note);
        saveNotes();

        if (note.done) {
            List<NoteData> doneNotes = getDoneNotes();
            for (NotesChangeListener listener : listeners) {
                listener.onDoneNotesChanged(doneNotes);
            }
        } else {
            List<NoteData> activeNotes = getActiveNotes();
            for (NotesChangeListener listener : listeners) {
                listener.onActiveNotesChanged(activeNotes);
            }
        }
    }

    private void saveNotes() {
        SharedPreferences.Editor editor = prefs.edit();
        for (int i = 0; i < notes.size(); i++) {
            editor.putString(PREF_NOTE + i, new Gson().toJson(notes.get(i)));
        }
        editor.putInt(PREF_NOTES_SIZE, notes.size());
        editor.apply();
    }

    public void addNotesChangeListener(NotesChangeListener listener) {
        listeners.add(listener);
    }

    public void removeNotesChangeListener(NotesChangeListener listener) {
        listeners.remove(listener);
    }

    public interface NotesChangeListener {
        void onActiveNotesChanged(List<NoteData> activeNotes);

        void onDoneNotesChanged(List<NoteData> doneNotes);
    }
}
