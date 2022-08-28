package com.hy0417sage.notes.database;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String Title = "title";
        public static final String Text = "text";
        public static final String PictureUrl = "pictureUrl";
        public static final String TABLE_NAME = "NotesTable";
        public static final String CREATE = "create table if not exists " + TABLE_NAME + "("
                + _ID + " integer primary key autoincrement, "
                + Title + " Text , "
                + Text + " Text , "
                + PictureUrl + " Url List Text );";
    }
}