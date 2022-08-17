package com.hy0417sage.notes.NotesAdapter;

import android.net.Uri;

public class NotesData {

    public long tempIndex;
    public String title;
    public String content;
    public String stringUrlList;

    public Uri pictureUrl;
    public String activityDiscrimination;

    public NotesData( ) {
    }

    public NotesData(Long tempIndex, String title, String content, String stringUrlList) {
        this.tempIndex = tempIndex;
        this.title = title;
        this.content = content;
        this.stringUrlList = stringUrlList;
    }

    public NotesData(Uri pictureUrl, String activityDiscrimination) {
        this.pictureUrl = pictureUrl;
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

    public Uri getPictureUrl() {
        return pictureUrl;
    }

    public String getActivityDiscrimination() {
        return activityDiscrimination;
    }

}