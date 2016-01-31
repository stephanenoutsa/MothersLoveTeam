package com.motherslove.stephnoutsa.mothersloveteam;

import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

public class MyDBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String CONTACT_COLUMN_ID = "_contactid";
    public static final String CONTACT_COLUMN_PHONE = "contactphone";
    public static final String CONTACT_COLUMN_LMP = "contactlmp";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + "(" +
                CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + ", " +
                CONTACT_COLUMN_PHONE + " INT " + ", " +
                CONTACT_COLUMN_LMP + " DATE" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    // Add new contact to CONTACTS table
    public void addContact(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_COLUMN_PHONE, String.valueOf(contact.getContactphone()));
        values.put(CONTACT_COLUMN_LMP, String.valueOf(contact.getContactlmp()));
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    // Get the LMP
    public String getLMP() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE 1;";
        Cursor c = db.rawQuery(query, null);
        if (c == null)
            return null;
        c.moveToLast();
        String lmpDate = c.getString(c.getColumnIndex(CONTACT_COLUMN_LMP));
        try {
            return lmpDate;
        } finally {
            c.close();
            db.close();
        }
    }

    // Get the Phone number
    public String getPhone() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE 1;";
        Cursor c = db.rawQuery(query, null);
        if (c == null)
            return null;
        c.moveToLast();
        String phone = c.getString(c.getColumnIndex(CONTACT_COLUMN_PHONE));
        try {
            return phone;
        } finally {
            c.close();
            db.close();
        }
    }

    // Print out database as a String
    public String databaseToString() {
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE 1";

        // Cursor to point to a location in your results
        Cursor c = db.rawQuery(query, null);

        // Move to the first row in your results
        c.moveToFirst();

        while (!c.isAfterLast()) {
            if(c.getString(c.getColumnIndex("contactphone")) != null && c.getString(c.getColumnIndex("contactlmp")) != null) {
                dbString += c.getString(c.getColumnIndex("contactphone"));
                dbString += "\t";
                dbString += c.getString(c.getColumnIndex("contactlmp"));
                dbString += "\n";
            }
        }
        db.close();

        return dbString;
    }
}
