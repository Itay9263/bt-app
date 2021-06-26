package com.syu.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    String mStrSqlCreate;
    String mStrSqlDrop;

    public DBHelper(Context context, String str, SQLiteDatabase.CursorFactory cursorFactory, int i) {
        super(context, str, cursorFactory, i);
    }

    public DBHelper(Context context, String str, String str2, String str3, int i) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, i);
        this.mStrSqlCreate = str2;
        this.mStrSqlDrop = str3;
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(this.mStrSqlCreate);
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL(this.mStrSqlDrop);
        onCreate(sQLiteDatabase);
    }
}
