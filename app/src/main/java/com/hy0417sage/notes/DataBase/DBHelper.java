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
    public static SQLiteDatabase db;
    private DatabaseHelper dbHelper;
    private final Context ctx;

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
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB.TABLE_NAME);
            onCreate(db);
        }
    }

    public DBHelper(Context context) {
        this.ctx = context;
    }

    public void open() throws SQLException {
        dbHelper = new DatabaseHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        db = dbHelper.getWritableDatabase();
    }

    public void create() {
        dbHelper.onCreate(db);
    }

    public void close() {
        db.close();
    }

    public void insertColumn(String title, String content, String url) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.Title, title);
        values.put(DataBases.CreateDB.Content, content);
        values.put(DataBases.CreateDB.Url, url);
        db.insert(DataBases.CreateDB.TABLE_NAME, null, values);
    }

    public void updateColumn(long id, String title, String content, String url) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.Title, title);
        values.put(DataBases.CreateDB.Content, content);
        values.put(DataBases.CreateDB.Url, url);
        db.update(DataBases.CreateDB.TABLE_NAME, values, "_id=" + id, null);
    }

    public void deleteColumn(long id) {
        db.delete(DataBases.CreateDB.TABLE_NAME, "_id=" + id, null);
    }

    public Cursor selectColumns() {
        return db.query(DataBases.CreateDB.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor sortColumn(){
        return db.rawQuery( "SELECT * FROM NotesTable ORDER BY " + "_id" + " DESC;", null);
    }

}