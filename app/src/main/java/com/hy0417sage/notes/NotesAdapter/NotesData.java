package com.hy0417sage.notes.NotesAdapter;

import android.net.Uri;

public class NotesData {

    public Long tempIndex;
    public String title;
    public String content;
    public String string_url_list;

    public Uri url;
    public String activity_discrimination;

    public NotesData( ) {
    }

    public NotesData(Long tempIndex, String title, String content, String string_url_list) {
        this.tempIndex = tempIndex;
        this.title = title;
        this.content = content;
        this.string_url_list = string_url_list;
    }

    public NotesData(Uri url, String activity_discrimination) {
        this.url = url;
        this.activity_discrimination = activity_discrimination;
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
        return string_url_list;
    }

    public Uri getUrl() {
        return url;
    }

    public String getActivityDiscrimination() {
        return activity_discrimination;
    }

}