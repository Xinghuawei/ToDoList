package com.example.todolist.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class TaskHelper extends SQLiteOpenHelper {
    public TaskHelper(@Nullable Context context) {
        super(context, Task.DB_NAME,null,Task.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + Task.TaskEntry.TABLE + "(" +
                                              Task.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                                              Task.TaskEntry.COL_TASK_TITLE+ " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Task.TaskEntry.TABLE);
        onCreate(db);
    }

//    public Cursor queueAll(){
//        db
//        Cursor  cursor = db.rawQuery("select * from table",null);
//    }
}
