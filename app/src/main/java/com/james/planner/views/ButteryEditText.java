package com.james.planner.views;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

import com.james.planner.Planner;
import com.james.planner.data.NoteData;

public class ButteryEditText extends AppCompatEditText {

    private Planner planner;

    private boolean isEditable;
    private NoteData note;

    public ButteryEditText(Context context) {
        super(context);
        planner = (Planner) context.getApplicationContext();
    }

    public ButteryEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        planner = (Planner) context.getApplicationContext();
    }

    public ButteryEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        planner = (Planner) context.getApplicationContext();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if (!focused && note != null) {
            note.content = getText().toString();
            planner.saveNote(note);
        }
    }

    @Override
    public InputFilter[] getFilters() {
        return new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        return isEditable ? source : source.length() < 1 ? source.subSequence(dstart, dend) : "";
                    }
                }
        };
    }

    public void setEditable(boolean isEditable) {
        this.isEditable = isEditable;
        setFocusable(isEditable);
        setClickable(isEditable);
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setNote(NoteData note) {
        this.note = note;
    }
}
