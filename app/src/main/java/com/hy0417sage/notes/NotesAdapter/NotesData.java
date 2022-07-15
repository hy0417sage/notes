package com.hy0417sage.notes.NotesAdapter;

import android.net.Uri;

public class NotesData {

    public long tempIndex;
    public String title;
    public String content;
    public String stringUrlList;

    public Uri url;
    public String activityDiscrimination;

    public NotesData( ) {
    }

    public NotesData(Long tempIndex, String title, String content, String stringUrlList) {
        this.tempIndex = tempIndex;
        this.title = title;
        this.content = content;
        this.stringUrlList = stringUrlList;
    }

    public NotesData(Uri url, String activityDiscrimination) {
        this.url = url;
        this.activityDiscrimination = activityDiscrimination;
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

    public String getStringUrlList() {
        return stringUrlList;
    }

    public Uri getUrl() {
        return url;
    }

    public String getActivityDiscrimination() {
        return activityDiscrimination;
    }

}