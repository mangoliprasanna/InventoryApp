package com.example.mango.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mango.inventoryapp.data.ProductContract;

public class EditActivity extends AppCompatActivity {

    private Uri productUri;
    private EditText prdNameEditText;
    private EditText prdPriceEditText;
    private EditText prdQuantityEditText;
    private EditText supNameEditText;
    private EditText supContactEditText;
    private boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        prdNameEditText = findViewById(R.id.productName);
        prdQuantityEditText = findViewById(R.id.productQuantity);
        prdPriceEditText = findViewById(R.id.productPrice);
        supContactEditText = findViewById(R.id.supplierContact);
        supNameEditText = findViewById(R.id.supplierName);

        // Attaching onTouchListener to each Edit Field.
        prdNameEditText.setOnTouchListener(onTouchListener);
        prdPriceEditText.setOnTouchListener(onTouchListener);
        prdQuantityEditText.setOnTouchListener(onTouchListener);
        supContactEditText.setOnTouchListener(onTouchListener);
        supNameEditText.setOnTouchListener(onTouchListener);


        // Checking URI sent by previous activity.
        if(getIntent().getData() == null){
            // changing label of activity as Add Product as no data received from previous activity.
            setTitle(getString(R.string.edit_add_title));
        } else {
            // CHanging label of activity as Edit Product as user requested to edit product.
            productUri = getIntent().getData();
            setTitle(getString(R.string.edit_edit_title));
            getProductFromDatabase();
        }
    }

    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            // set flag as true when user touch any of EditText.
            flag = true;
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        if (!flag) {
            super.onBackPressed();
            return;
        }
        discardChangesDialog();
    }
    private void discardChangesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.edit_product_discardDialog_msg));
        // Handling positive response.
        builder.setPositiveButton(getString(R.string.edit_product_discardDialog_positive),  new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Handling Negative Response.
        builder.setNegativeButton(R.string.edit_product_discardDialog_negative, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    // this function get executed only in edit mode.
    private void getProductFromDatabase(){

        // Loading the cursor for requesrted URI
        Cursor cursor = getContentResolver().query(productUri, null, null, null, null);
        try{
            // moving cursor to next position.
            if(cursor.moveToNext())
            {

                // putting values from database to respective edit field.
                prdNameEditText.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRD_NAME)));
                prdPriceEditText.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRD_PRICE)));
                prdQuantityEditText.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_PRD_QUANTITY)));
                supNameEditText.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUP_NAME)));
                supContactEditText.setText(cursor.getString(cursor.getColumnIndex(ProductContract.ProductEntry.COLUMN_SUP_PHONE)));
            }
        } finally {

            // finally closing the cursor.
            cursor.close();
        }
    }

    // This is combined function to add or edit the product depending upon the request.
    private void performActionOnProduct(){

        // Validating Product Name. if empty prompt on screen.
        if(TextUtils.isEmpty(prdNameEditText.getText().toString())){
            prdNameEditText.setError(getString(R.string.edit_prdName_error));
            Toast.makeText(this, R.string.edit_prdName_error, Toast.LENGTH_LONG).show();
            return;
        }

        // Validating Product Price. if empty prompt on screen.
        if(TextUtils.isEmpty(prdPriceEditText.getText().toString())){
            prdPriceEditText.setError(getString(R.string.edit_prdPrice_error));
            Toast.makeText(this, R.string.edit_prdPrice_error, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validating Product Quantity. if empty prompt on screen.
        if(TextUtils.isEmpty(prdQuantityEditText.getText().toString())){
            prdQuantityEditText.setError(getString(R.string.edit_prdPrice_error));
            Toast.makeText(this, R.string.edit_prdPrice_error, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validating Supplier Name. if empty prompt on screen.
        if(TextUtils.isEmpty(supNameEditText.getText().toString())){
            supNameEditText.setError(getString(R.string.edit_supName_error));
            Toast.makeText(this, R.string.edit_supName_error, Toast.LENGTH_SHORT).show();
            return;
        }

        // Validating Supplier Contact Number. if empty prompt on screen.
        if(TextUtils.isEmpty(supContactEditText.getText().toString())){
            supContactEditText.setError(getString(R.string.edit_supName_contact));
            Toast.makeText(this, R.string.edit_supName_contact, Toast.LENGTH_SHORT).show();
            return;
        }

        // Creating instance of content value to pass in content resolver.
        ContentValues contentValue = new ContentValues();
        contentValue.put(ProductContract.ProductEntry.COLUMN_PRD_NAME, prdNameEditText.getText().toString().trim());
        contentValue.put(ProductContract.ProductEntry.COLUMN_PRD_PRICE, prdPriceEditText.getText().toString().trim());
        contentValue.put(ProductContract.ProductEntry.COLUMN_PRD_QUANTITY, prdQuantityEditText.getText().toString().trim());
        contentValue.put(ProductContract.ProductEntry.COLUMN_SUP_NAME, supNameEditText.getText().toString().trim());
        contentValue.put(ProductContract.ProductEntry.COLUMN_SUP_PHONE, supContactEditText.getText().toString().trim());

        // Depending upoon mode executing contentResolver function.
        if(productUri == null){
            getContentResolver().insert(ProductContract.ProductEntry.CONTENT_URI, contentValue);
            Toast.makeText(this, R.string.edit_product_insert, Toast.LENGTH_SHORT).show();
        } else {
            getContentResolver().update(productUri, contentValue, null, null);
            Toast.makeText(this, R.string.edit_product_updae, Toast.LENGTH_SHORT).show();
        }

        // Closing the activity once edit or insert is done.
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        switch (menuItem.getItemId()){
            case R.id.action_check:
                performActionOnProduct();
                break;
            case android.R.id.home:
                if(!flag){
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                discardChangesDialog();
                break;
        }
        return true;
    }
}
