package com.example.task1.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.task1.db.entity.Contact;
import com.example.task1.db.entity.Contact;

import java.util.ArrayList;
import java.util.Collection;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "contacts_db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {



        db.execSQL(Contact.CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+Contact.TABLE_NAME);
        onCreate(db);

    }

    //insert data into database method
    public long insertContact(String name,String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Contact.COL_NAME,name);
        values.put(Contact.COL_EMAIL,email);
        long id = db.insert(Contact.TABLE_NAME,null,values);

        db.close();

        return id;
    }

    //Getting Contact from database
    public Contact getContact(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(Contact.TABLE_NAME,
                new String[]
                        {
                                Contact.COL_ID,
                                Contact.COL_NAME,
                                Contact.COL_EMAIL
                        },
                Contact.COL_ID + "=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);

        if (cursor!=null){
            cursor.moveToFirst();
        }
        Contact contact = new Contact(
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COL_NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(Contact.COL_EMAIL)),
                cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COL_ID))
        );

        cursor.close();
        return contact;
    }

    //Getting All Contacts from database;
    public Collection<? extends Contact> getAllContacts(){
        ArrayList<Contact> contactArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+Contact.TABLE_NAME
                +" ORDER BY " + Contact.COL_ID + " DESC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()) {

            do {
                Contact contact = new Contact();
                contact.setId(cursor.getInt(cursor.getColumnIndexOrThrow(Contact.COL_ID)));
                contact.setName(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COL_NAME)));
                contact.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(Contact.COL_EMAIL)));

                contactArrayList.add(contact);
            }while (cursor.moveToNext());
        }
        db.close();

        return contactArrayList;

    }

    //Updating contact from database;
    public int updateContact(Contact contact){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values =new ContentValues();
        values.put(Contact.COL_NAME,contact.getName());
        values.put(Contact.COL_EMAIL,contact.getEmail());

        return db.update(Contact.TABLE_NAME,values,Contact.COL_ID +
                " = ?",new String[]{
                        String.valueOf(contact.getId())
        });

    }

    //Deleting Contact from database;
    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Contact.TABLE_NAME,Contact.COL_ID +
                " = ?",new String[]{
                        String.valueOf(contact.getId())
        });

        db.close();
    }
}
