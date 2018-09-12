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
        final String CREATE_TABLE = "CREATE TABLE products(" +
                ProductEntry.COLUMN_PRD_ID + " Integer Primary Key Autoincrement," +
                ProductEntry.COLUMN_PRD_NAME +" Text NOT NULL," +
                ProductEntry.COLUMN_PRD_PRICE + " Integer NOT NULL," +
                ProductEntry.COLUMN_PRD_QUANTITY + " Integer," +
                ProductEntry.COLUMN_SUP_NAME + " Text NOT NULL, " +
                ProductEntry.COLUMN_SUP_PHONE + " Text NOT NULL " +
                ");";
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
