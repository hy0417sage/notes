package com.hy0417sage.notes.model;

import androidx.fragment.app.Fragment;

public class MemoModel {

//    public static MemoModel getInstance() {
//        return ;
//    }

    private long newIndex;
    private long tempIndex;
    private String title;
    private String content;
//    private final String stringUrlList;
    private Fragment fragment;

    public MemoModel(){

    }

    public MemoModel(String title, String content) {
        this.title = title;
        this.content = content;
//        this.stringUrlList = stringUrlList;
    }

    public MemoModel(long tempIndex, String title, String content) {
        this.tempIndex = tempIndex;
        this.title = title;
        this.content = content;
//        this.stringUrlList = stringUrlList;
    }

    public void setNewIndex(long newIndex) {
        this.newIndex = newIndex;
    }

    public long getNewIndex() {
        return newIndex;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public Long getTempIndex() {
        return tempIndex;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Fragment getFragment() {
        return fragment;
    }

//    public String getStringUrlList() {
//        return stringUrlList;
//    }

}