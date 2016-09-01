package com.james.planner;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.james.planner.data.NoteData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Planner extends Application {

    private static final String PREF_NOTES = "com.james.planner.PREF_NOTES";

    private SharedPreferences prefs;
    public List<NoteData> notes;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        notes = new ArrayList<>();
        if (prefs.contains(PREF_NOTES)) {
            notes.addAll(Arrays.asList(new Gson().fromJson(prefs.getString(PREF_NOTES, ""), NoteData[].class)));
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
        notes.add(0, note);
        saveNotes();
    }

    public void saveNote(NoteData note) {
        saveNotes();
    }

    public void setDone(boolean done, NoteData note) {
        note.done = done;
        saveNotes();
    }

    public void removeNote(NoteData note) {
        notes.remove(note);
        saveNotes();
    }

    private void saveNotes() {
        prefs.edit().putString(PREF_NOTES, new Gson().toJson(notes.toArray(new NoteData[notes.size()]))).apply();
    }
}
