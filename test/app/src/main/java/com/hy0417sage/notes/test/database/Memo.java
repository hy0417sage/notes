package com.hy0417sage.notes.test.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Memo {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="title")
    public String title;

    @ColumnInfo(name="text")
    public String text;
}