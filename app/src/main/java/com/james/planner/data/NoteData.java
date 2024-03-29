package com.james.planner.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class NoteData implements Parcelable {

    public String content;
    public NoteData[] datas;
    public boolean done;

    public NoteData() {
    }

    public List<NoteData> getNotes() {
        List<NoteData> notes = new ArrayList<>();
        if (datas != null) {
            for (NoteData data : datas) {
                notes.add(data.clone());
            }
        }
        return notes;
    }

    public void setNotes(List<NoteData> notes) {
        datas = notes.toArray(new NoteData[notes.size()]);
    }

    @Override
    public NoteData clone() {
        NoteData note = new NoteData();
        note.content = content;
        if (datas != null) note.datas = getNotes().toArray(new NoteData[datas.length]);
        note.done = done;
        return note;
    }

    protected NoteData(Parcel in) {
        content = in.readString();
        datas = in.createTypedArray(NoteData.CREATOR);
    }

    public static final Creator<NoteData> CREATOR = new Creator<NoteData>() {
        @Override
        public NoteData createFromParcel(Parcel in) {
            return new NoteData(in);
        }

        @Override
        public NoteData[] newArray(int size) {
            return new NoteData[size];
        }
    };

    public String getDescription() {
        String description = "";
        for (NoteData data : datas) {
            if (description.length() > 0) description += ", ";
            description += data.content;
        }

        return description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(content);
        dest.writeTypedArray(datas, flags);
    }
}
