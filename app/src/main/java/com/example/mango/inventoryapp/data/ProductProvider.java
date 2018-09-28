package com.example.mango.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

public class ProductProvider extends ContentProvider {
    private static final int PRODUCTS = 100;
    private static final int PRODUCT_ID = 101;
    private ProductDBHelper productDBHelper;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS, PRODUCTS);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PATH_PRODUCTS + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        productDBHelper = new ProductDBHelper(getContext());
        return false;
    }

    private void validateContentValue(Uri uri, ContentValues contentValues){

        // Checking if value is set for Product Name and it is null.
        if(contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRD_NAME) && TextUtils.isEmpty(contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRD_NAME)))
            throw new IllegalArgumentException("Product Name is not provided." + uri);

        // Checking if value is set for Product Price and it is null.
        if(contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRD_PRICE) && TextUtils.isEmpty(contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRD_PRICE)))
            throw new IllegalArgumentException("Product Price is not provided." + uri);

        // Checking if value is set for Product Quantity and it is null.
        if(contentValues.containsKey(ProductContract.ProductEntry.COLUMN_PRD_QUANTITY) && TextUtils.isEmpty(contentValues.getAsString(ProductContract.ProductEntry.COLUMN_PRD_QUANTITY)))
            throw new IllegalArgumentException("Product Quantity is not provided." + uri);

        // Checking if value is set for Supplier Name and it is null.
        if(contentValues.containsKey(ProductContract.ProductEntry.COLUMN_SUP_NAME) && TextUtils.isEmpty(contentValues.getAsString(ProductContract.ProductEntry.COLUMN_SUP_NAME)))
            throw new IllegalArgumentException("Supplier Name is not provided." + uri);

        // Checking if value is set for Supplier phone and it is null.
        if(contentValues.containsKey(ProductContract.ProductEntry.COLUMN_SUP_PHONE) && TextUtils.isEmpty(contentValues.getAsString(ProductContract.ProductEntry.COLUMN_SUP_PHONE)))
            throw new IllegalArgumentException("Supplier Contact Number is not provided." + uri);
    }

    @Override
    public Cursor query(Uri uri,   String[] projections,   String selection,   String[] selectionArgs,   String sortOrder) {
        // database instance just to read data.
        SQLiteDatabase sqLiteDatabase = productDBHelper.getReadableDatabase();

        // Matching requested URI with predefined pattern.
        int match = uriMatcher.match(uri);

        Cursor cursor;
        switch (match){
            case PRODUCTS:
                cursor = sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, projections,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry.COLUMN_PRD_ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri)) };
                cursor = sqLiteDatabase.query(ProductContract.ProductEntry.TABLE_NAME, projections,
                        selection, selectionArgs, null, null, sortOrder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query, Unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), ProductContract.ProductEntry.CONTENT_URI);
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductContract.ProductEntry.CONTENT_LIST_TYPE;
            case PRODUCT_ID:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Override
    public Uri insert(  Uri uri,   ContentValues contentValues) {
        int match = uriMatcher.match(uri);
        switch (match){
            case PRODUCTS:
                return insertProduct(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Invalid URI");
        }
    }

    private Uri insertProduct(Uri uri, ContentValues contentValues){

        // database instance just to write data.
        SQLiteDatabase db = productDBHelper.getWritableDatabase();

        // Validating data.
        validateContentValue(uri, contentValues);


        long id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, contentValues);
        if(id == -1)
            throw new IllegalArgumentException("Unable to Insert." + uri);

        // Notifying data inserted.
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(  Uri uri,   String selections,   String[] selectionArgs) {
        // database instance just to write data.
        SQLiteDatabase sqLiteDatabase = productDBHelper.getWritableDatabase();

        // Matching requested URI with predefined pattern.
        int match = uriMatcher.match(uri);

        int result;
        switch (match){
            case PRODUCTS:
                result = sqLiteDatabase.delete(ProductContract.ProductEntry.TABLE_NAME, selections, selectionArgs);
                break;
            case PRODUCT_ID:
                selections = ProductContract.ProductEntry.COLUMN_PRD_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                result = sqLiteDatabase.delete(ProductContract.ProductEntry.TABLE_NAME, selections, selectionArgs);
                break;
                default:
                    throw new IllegalArgumentException("Cannot delete, Invalid URI : "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    private int updateProduct(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){

        // database instance just to write data.
        SQLiteDatabase sqLiteDatabase = productDBHelper.getWritableDatabase();

        int result = sqLiteDatabase.update(ProductContract.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update( Uri uri, ContentValues contentValues,String selection, String[] selectionArgs) {
        // Matching requested URI with predefined pattern.

        int match = uriMatcher.match(uri);

        int result = 0;
        switch (match){
            case PRODUCTS:
                result = updateProduct(uri, contentValues, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                selection = ProductContract.ProductEntry.COLUMN_PRD_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                result = updateProduct(uri, contentValues, selection, selectionArgs);
                break;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }
}
