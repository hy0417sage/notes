package com.hy0417sage.notes.database;

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
    public static SQLiteDatabase database;
    private DatabaseHelper databaseHelper;
    private final Context context;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, String name, CursorFactory cursorFactory, int version) {
            super(context, name, cursorFactory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(DataBases.CreateDB.CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB.TABLE_NAME);
            onCreate(sqLiteDatabase);
        }
    }

    public DBHelper(Context context) {
        this.context = context;
    }

    public void open() throws SQLException {
        databaseHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = databaseHelper.getWritableDatabase();
    }

    public void create() {
        databaseHelper.onCreate(database);
    }

    public void close() {
        database.close();
    }

    public void insertColumn(String title, String content, String pictureUrl) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.Title, title);
        values.put(DataBases.CreateDB.Content, content);
        values.put(DataBases.CreateDB.PictureUrl, pictureUrl);
        database.insert(DataBases.CreateDB.TABLE_NAME, null, values);
    }

    public void updateColumn(long id, String title, String content, String pictureUrl) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.Title, title);
        values.put(DataBases.CreateDB.Content, content);
        values.put(DataBases.CreateDB.PictureUrl, pictureUrl);
        database.update(DataBases.CreateDB.TABLE_NAME, values, "_id=" + id, null);
    }

    public void deleteColumn(long id) {
        database.delete(DataBases.CreateDB.TABLE_NAME, "_id=" + id, null);
    }

    public Cursor selectColumns() {
        return database.query(DataBases.CreateDB.TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor sortColumn(){
        return database.rawQuery( "SELECT * FROM NotesTable ORDER BY " + "_id" + " DESC;", null);
    }

}