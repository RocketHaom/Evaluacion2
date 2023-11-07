package com.example.evaluacion1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDataManager {
    private UserSTDataBaseHelper dbHelper;

    public UserDataManager(Context context) {
        dbHelper = new UserSTDataBaseHelper(context);
    }

    public long insertUser(String username, String email, String phone, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserSTDataBaseHelper.COLUMN_USERNAME, username);
        values.put(UserSTDataBaseHelper.COLUMN_EMAIL, email);
        values.put(UserSTDataBaseHelper.COLUMN_PHONE, phone);
        values.put(UserSTDataBaseHelper.COLUMN_PASSWORD, password);

        return db.insert(UserSTDataBaseHelper.TABLE_NAME, null, values);
    }

    public int updateUser(long id, String username, String email, String phone, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserSTDataBaseHelper.COLUMN_USERNAME, username);
        values.put(UserSTDataBaseHelper.COLUMN_EMAIL, email);
        values.put(UserSTDataBaseHelper.COLUMN_PHONE, phone);
        values.put(UserSTDataBaseHelper.COLUMN_PASSWORD, password);

        return db.update(UserSTDataBaseHelper.TABLE_NAME, values, UserSTDataBaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteUser(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        return db.delete(UserSTDataBaseHelper.TABLE_NAME, UserSTDataBaseHelper.COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public Cursor getAllUsers() {
        return dbHelper.getAllUserData();
    }

}
