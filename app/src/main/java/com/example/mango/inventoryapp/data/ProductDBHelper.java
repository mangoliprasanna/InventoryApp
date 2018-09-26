package com.example.mango.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.mango.inventoryapp.data.ProductContract.ProductEntry;

public class ProductDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ProductDetails.db";
    public static final int DATABASE_VERSION = 1;

    public ProductDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + ProductEntry.TABLE_NAME  + "(" +
                ProductEntry.COLUMN_PRD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ProductEntry.COLUMN_PRD_NAME +" TEXT NOT NULL," +
                ProductEntry.COLUMN_PRD_PRICE + " INTEGER NOT NULL," +
                ProductEntry.COLUMN_PRD_QUANTITY + " INTEGER," +
                ProductEntry.COLUMN_SUP_NAME + " TEXT NOT NULL, " +
                ProductEntry.COLUMN_SUP_PHONE + " TEXT NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
