package com.hy0417sage.notes.test.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Memo.class}, version = 1)
public abstract class MemoDB extends RoomDatabase {

    private static MemoDB INSTANCE = null;

    public abstract MemoDao memoDao();


    public static MemoDB getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    MemoDB.class, "memo.db").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}