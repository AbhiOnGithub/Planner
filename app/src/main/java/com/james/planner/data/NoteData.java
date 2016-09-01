package com.james.planner.data;

import android.os.Parcel;
import android.os.Parcelable;

public class NoteData implements Parcelable {

    public String content;
    public NoteData[] datas;
    public boolean done;

    public NoteData() {
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
