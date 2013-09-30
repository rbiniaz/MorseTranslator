package com.mobpro.wolfpack.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class NotesDbAdapter {

    private static final String DATABASE_NAME = "messages.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "messages";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_MESSAGE = "message";

    private final Context context;
    private NotesDbHelper dbHelper;
    private SQLiteDatabase db;

    public NotesDbAdapter(Context context) {
        this.context = context;
    }

    public NotesDbAdapter open(){
        dbHelper = new NotesDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }


    public Note createNote(String name) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, name);
        long id = db.insert(TABLE_NAME, null, values);

        return new Note(id, name);
    }


    public boolean deleteNote(Note note) {
        return db.delete(TABLE_NAME, COLUMN_ID + "=" + note.getId(), null) > 0;
    }

    public Note getNote(long id){
        Cursor cursor = db.query(true, TABLE_NAME, new String[] {COLUMN_ID, COLUMN_MESSAGE}, COLUMN_ID + "=" + id, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return noteFromCursor(cursor);

    }

    public Cursor getAllNotes(){
        return db.query(true, TABLE_NAME, new String[] {COLUMN_ID, COLUMN_MESSAGE}, null, null, null, null, null, null);
    }

    public static Note noteFromCursor(Cursor cursor){
        return new Note(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMN_MESSAGE))
        );
    }


    private class NotesDbHelper extends SQLiteOpenHelper {

        private static final String CREATE_DATABASE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_MESSAGE +  " TEXT)";

        public NotesDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DATABASE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
