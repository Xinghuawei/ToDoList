package com.example.todolist.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table login(password text primary key)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists login");
    }

    //inserting in database
    public boolean insert(String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password",password);
        long ins = db.insert("login",null,contentValues);
        if(ins==-1){
            return false;
        } else {
            return true;
        }
    }


    //check username and password when login
    public Boolean checkPassword(String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from login where password=?",
                new String[]{password});
        if(cursor.getCount()>0) return true;
        else return false;
    }

    public Boolean checkPasswordExist(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from login",
                new String[]{});
        if(cursor.getCount()>0) return true;
        else return false;
    }
}
