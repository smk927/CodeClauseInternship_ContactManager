package com.example.task1.db.entity;

public class Contact {

    public static final String TABLE_NAME ="contact";

    public static final String COL_EMAIL = "email";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";

    public String name,email;
    public int id;

    public Contact(){

    }

    public Contact(String name, String email, int id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //SQL Query: CREATING TABLE
    public static final String CREATE_TABLE =
        "CREATE TABLE "+ TABLE_NAME +
                "("+
                 COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                 COL_NAME + " TEXT,"+
                 COL_EMAIL + " TEXT"+
                ");";
}
