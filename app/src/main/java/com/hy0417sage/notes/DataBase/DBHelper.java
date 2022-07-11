package com.hy0417sage.notes.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {

    private static final String DATABASE_NAME = "Notes.db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB.TABLENAME);
            onCreate(db);
        }
    }

    public DBHelper(Context context) {
        this.mCtx = context;
    }

    public void open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    public void create() {
        mDBHelper.onCreate(mDB);
    }

    public void close() {
        mDB.close();
    }

    public void insertColumn(String title, String content, String url) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.Title, title);
        values.put(DataBases.CreateDB.Content, content);
        values.put(DataBases.CreateDB.Url, url);
        mDB.insert(DataBases.CreateDB.TABLENAME, null, values);
    }

    public void updateColumn(long id, String title, String content, String url) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.Title, title);
        values.put(DataBases.CreateDB.Content, content);
        values.put(DataBases.CreateDB.Url, url);
        mDB.update(DataBases.CreateDB.TABLENAME, values, "_id=" + id, null);
    }

    public void deleteColumn(long id) {
        mDB.delete(DataBases.CreateDB.TABLENAME, "_id=" + id, null);
    }

    public Cursor selectColumns() {
        return mDB.query(DataBases.CreateDB.TABLENAME, null, null, null, null, null, null);
    }

    public Cursor sortColumn(){
        return mDB.rawQuery( "SELECT * FROM NotesTable ORDER BY " + "_id" + " DESC;", null);
    }

}