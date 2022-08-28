package com.hy0417sage.notes.test.model;

import android.content.Context;

public class MemoModel {

    private String title;
    private String text;

    private static MemoModel INSTANCE = null;

    public static MemoModel getInstance(Context context) {
        return INSTANCE;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void setINSTANCE(MemoModel INSTANCE) {
        MemoModel.INSTANCE = INSTANCE;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }
}
