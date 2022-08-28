package com.hy0417sage.notes.model;

import android.net.Uri;

import com.hy0417sage.notes.funtion.Memo;

public class MemoModel {

    public static final MemoModel INSTANCE = new MemoModel();
    public static MemoModel getInstance(){
        return INSTANCE;
    }

    private long id;
    private String title;
    private String text;

    private MemoModel(){

    }

    public void setMemoModel(long id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }


}