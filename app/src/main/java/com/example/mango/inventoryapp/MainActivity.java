package com.example.mango.inventoryapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mango.inventoryapp.data.ProductDBHelper;
import com.example.mango.inventoryapp.data.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity {

    private  ProductDBHelper productDBHelper;
    private TextView tableCountText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create instance of ProductDBHelper for further use.
        productDBHelper  = new ProductDBHelper(MainActivity.this);
        tableCountText = (TextView) findViewById(R.id.tableCountText);

        updateCount();
        displayData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu from res/menu/menu_main.xml
        // Adding menu to appBar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){

        switch (menuItem.getItemId()) {
            case R.id.action_insert_dummy_data:
                insetData();
                break;
            case R.id.action_delete_all_entries:
                int deleteVal = deleteAllData();
                Toast.makeText(MainActivity.this, getString(R.string.main_toast_insert_msg) + " " + String.valueOf(deleteVal), Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void updateCount(){
        SQLiteDatabase db = productDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + ProductEntry.TABLE_NAME, null);
        tableCountText.setText("Total Records : " + cursor.getCount());
        cursor.close();
        db.close();
    }

    private int deleteAllData(){
        SQLiteDatabase db = productDBHelper.getWritableDatabase();
        int delVal = db.delete(ProductEntry.TABLE_NAME, null, null);
        db.close();
        updateCount();
        return delVal;
    }

    private void displayData(){

        SQLiteDatabase db = productDBHelper.getReadableDatabase();
        String[] projections = {
                ProductEntry.COLUMN_PRD_ID,
                ProductEntry.COLUMN_PRD_NAME,
                ProductEntry.COLUMN_PRD_PRICE,
                ProductEntry.COLUMN_PRD_QUANTITY
        };

        Cursor cursor = db.query(ProductEntry.TABLE_NAME, projections, null, null, null, null, null);
        try{

            int idColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRD_ID);
            int prdNameColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRD_NAME);
            int prdPriceColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRD_PRICE);
            int prdQuantityColumnIndex = cursor.getColumnIndex(ProductEntry.COLUMN_PRD_QUANTITY);
            while(cursor.moveToNext()){
                Log.v("Product ID " , cursor.getString(idColumnIndex));
                Log.v("Product Name " , cursor.getString(prdNameColumnIndex));
                Log.v("Product Price ", cursor.getString(prdPriceColumnIndex));
                Log.v("Product Quntity ", cursor.getString(prdQuantityColumnIndex));
            }

        } finally {
            cursor.close();

        }
    }
    private void insetData() {
        // Get Writable Database.
        SQLiteDatabase db = productDBHelper.getWritableDatabase();

        // Creating contectValues.
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRD_NAME, "prd_1");
        contentValues.put(ProductEntry.COLUMN_PRD_PRICE, 200);
        contentValues.put(ProductEntry.COLUMN_PRD_QUANTITY, 10);
        contentValues.put(ProductEntry.COLUMN_SUP_NAME, "Sup 1");
        contentValues.put(ProductEntry.COLUMN_SUP_PHONE, "1234567890");

        long newId  = db.insert(ProductEntry.TABLE_NAME, null, contentValues);
        updateCount();
        db.close();

        Log.v("ROW_INSERTED", "NEW ID : " + newId);
        displayData();
        Toast.makeText(MainActivity.this, getString(R.string.main_toast_insert_msg) + " " + String.valueOf(newId), Toast.LENGTH_SHORT).show();
    }
}