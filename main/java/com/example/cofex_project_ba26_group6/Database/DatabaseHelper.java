package com.example.cofex_project_ba26_group6.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.cofex_project_ba26_group6.Data.Coffee;
import com.example.cofex_project_ba26_group6.Data.Transaction;
import com.example.cofex_project_ba26_group6.Data.User;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "CofexDB";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public boolean dbInsert(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("username", user.getUsername());
        cv.put("password", user.getPassword());
        cv.put("email", user.getEmail());
        cv.put("phoneNumber", user.getPhoneNumber());
        cv.put("gender", user.getGender());
        cv.put("dob", user.getDob());
        cv.put("address", user.getAddress());
        long id = db.insert("User", null, cv);
        db.close();
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean dbInsert(Coffee coffee) {
        boolean isCoffeeExist = false;
        Cursor coffeeCursor = getDatabaseData("Coffee", "");
        if (coffeeCursor.getCount() != 0) {
            try {
                for (int i = 0; i < coffeeCursor.getCount(); i++) {
                    if (coffeeCursor.moveToPosition(i)) {
                        if (coffee.getCoffeeName().equals(coffeeCursor.getString(1)) &&
                                coffee.getPrice().equals(coffeeCursor.getString(2))) {
                            isCoffeeExist = true;
                            break;
                        }
                    }
                }
            } catch (NullPointerException npe) {
                isCoffeeExist = false;
            }
        }
        if (!isCoffeeExist) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("coffeeName", coffee.getCoffeeName());
            cv.put("price", coffee.getPrice());
            cv.put("cafeId", coffee.getCafeId());
            long id = db.insert("Coffee", null, cv);
            db.close();
            if (id == -1) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean dbInsert(Transaction transaction) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userId", transaction.getUserId());
        cv.put("coffeeId", transaction.getCoffeeId());
        cv.put("quantity", transaction.getQuantity());
        cv.put("date", transaction.getDate());
        long id = db.insert("`Transaction`", null, cv);
        db.close();
        if (id == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getDatabaseData(String tableName, String condition) {
        String sql = "SELECT * FROM " + tableName;
        if (!condition.isEmpty()) {
            sql += " WHERE " + condition;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(sql, null);
    }


    public void dbUpdateUserPassword(int id, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ID", id);
        cv.put("password", newPassword);
        db.update("User", cv, "ID = ?", new String[]{"" + id});
    }

    public void dbDeleteAllTransaction(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM `Transaction` WHERE userId = " + userId);
    }

    public void dbDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("`Transaction`","ID=?",new String[]{String.valueOf(id)});
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS User (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, username TEXT NOT NULL, password TEXT NOT NULL, email TEXT NOT NULL, phoneNumber TEXT NOT NULL, gender TEXT NOT NULL, dob TEXT NOT NULL, address TEXT NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS Coffee (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, coffeeName TEXT NOT NULL, price TEXT NOT NULL, cafeId INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `Transaction` (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, userId INTEGER NOT NULL, coffeeId INTEGER NOT NULL, quantity INTEGER NOT NULL, date TEXT NOT NULL, FOREIGN KEY (userId) REFERENCES User(userId), FOREIGN KEY (coffeeId) REFERENCES Coffee(coffeeId))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS Coffee");
        db.execSQL("DROP TABLE IF EXISTS `Transaction`");
        onCreate(db);
    }
}
