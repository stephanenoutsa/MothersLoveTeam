package com.motherslove.stephnoutsa.mothersloveteam;

import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.Cursor;
import android.content.Context;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

public class MyDBHandler extends SQLiteOpenHelper {

    Context context;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts.db";
    public static final String TABLE_CONTACTS = "contacts";
    public static final String CONTACT_COLUMN_ID = "_contactid";
    public static final String CONTACT_COLUMN_PHONE = "contactphone";
    public static final String CONTACT_COLUMN_LMP = "contactlmp";

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_CONTACTS + "(" +
                CONTACT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT " + ", " +
                CONTACT_COLUMN_PHONE + " TEXT " + ", " +
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

    // Get single contact from the CONTACTS table
    public Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.query(TABLE_CONTACTS, new String[] {CONTACT_COLUMN_ID, CONTACT_COLUMN_PHONE,
                CONTACT_COLUMN_LMP}, CONTACT_COLUMN_ID + "=?", new String[] {String.valueOf(id)},
                null, null, null, null);
        if(c != null)
            c.moveToFirst();

        Contact contact = new Contact(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(2));

        db.close();

        return contact;
    }

    // Get all contacts from the CONTACTS table
    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_CONTACTS + ";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        // Loop through all rows and add to list
        /*if(c.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.set_contactid(Integer.parseInt(c.getString(0)));
                contact.setContactphone(c.getString(1));
                contact.setContactlmp(c.getString(2));

                contactList.add(contact);
            } while (c.moveToNext());
        }*/
        while(!c.isAfterLast()) {
            Contact contact = new Contact();
            contact.set_contactid(Integer.parseInt(c.getString(0)));
            contact.setContactphone(c.getString(1));
            contact.setContactlmp(c.getString(2));

            contactList.add(contact);

            c.moveToNext();
        }

        return contactList;
    }

    // Get contacts count
    public int getContactsCount() {
        String query = "SELECT * FROM " + TABLE_CONTACTS + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(query, null);

        try {
            return c.getCount();
        } finally {
            c.close();
            db.close();
        }
    }

    // Update single contact in the CONTACTS table
    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_COLUMN_PHONE, contact.getContactphone());
        values.put(CONTACT_COLUMN_LMP, contact.getContactlmp());

        return db.update(TABLE_CONTACTS, values, CONTACT_COLUMN_ID + "=?",
                new String[] {String.valueOf(contact.get_contactid())});
    }

    // Delete a contact from the CONTACTS table
    public void deleteContact(String contactphone) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CONTACTS + " WHERE " + CONTACT_COLUMN_PHONE + " = " + contactphone + ";");
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
        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE 1;";

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
