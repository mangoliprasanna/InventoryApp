package com.example.mango.inventoryapp;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.content.CursorLoader;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mango.inventoryapp.data.ProductContract.ProductEntry;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private final int LOADER_ID = 0;
    private ProductAdapter productAdapter;
    private ListView productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation of ListView
        productList = (ListView) findViewById(R.id.productList);

        // Creating Adapter
        productAdapter = new ProductAdapter(this, null);

        // Empty View when list is empty.
        productList.setEmptyView(findViewById(R.id.emptyView));

        // Handling item clicks.
        productList.setOnItemClickListener (new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        productList.setAdapter(productAdapter);
        productList.setDescendantFocusability(ListView.FOCUS_BLOCK_DESCENDANTS);

        // Initiating Loader.
        getLoaderManager().initLoader(LOADER_ID, null, this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        // Handling menu item clicks.
        switch (menuItem.getItemId()) {
            case R.id.action_insert_dummy_data:
                insetData();
                break;
            case R.id.action_delete_all_entries:
                // Displaying confirmation dialog to confirm delete all.
                showDeleteConfirmationDialog();
                break;
            case R.id.action_settings:
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                // starting settings activity.
                startActivity(i);
                break;
        }
        return true;
    }

    // Delete all function used to delete all data from dattabase after user confirmation.
    private void deleteALlData(){
        int i = getContentResolver().delete(ProductEntry.CONTENT_URI, null, null);
        Toast.makeText(this, i + " Rows deleted.", Toast.LENGTH_SHORT).show();
    }
    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_confirm_text));
        // Handling positive response.
        builder.setPositiveButton(getString(R.string.delete_confirm_positive),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteALlData();
            }
        });
        // Handling Negative Response.
        builder.setNegativeButton(R.string.delete_confirm_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void insetData() {

        // Creating contectValues.
        ContentValues contentValues = new ContentValues();
        contentValues.put(ProductEntry.COLUMN_PRD_NAME, "prd_1");
        contentValues.put(ProductEntry.COLUMN_PRD_PRICE, 200);
        contentValues.put(ProductEntry.COLUMN_PRD_QUANTITY, 10);
        contentValues.put(ProductEntry.COLUMN_SUP_NAME, "Sup 1");
        contentValues.put(ProductEntry.COLUMN_SUP_PHONE, "1234567890");
        getContentResolver().insert(ProductEntry.CONTENT_URI, contentValues);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        // Creating cursor loader to get all product data from database.
        return new CursorLoader(this, ProductEntry.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        // Swap for new cursor.
        productAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productAdapter.swapCursor(null);
    }
}