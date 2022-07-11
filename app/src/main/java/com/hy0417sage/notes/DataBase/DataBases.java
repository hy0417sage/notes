package com.hy0417sage.notes.DataBase;

import android.provider.BaseColumns;

public final class DataBases {

    public static final class CreateDB implements BaseColumns {
        public static final String Title = "title";
        public static final String Content = "content";
        public static final String Url = "url";
        public static final String TABLENAME = "NotesTable";
        public static final String CREATE = "create table if not exists " + TABLENAME + "("
                + _ID + " integer primary key autoincrement, "
                + Title + " Text , "
                + Content + " Text , "
                + Url + " Url List Text );";
    }
}