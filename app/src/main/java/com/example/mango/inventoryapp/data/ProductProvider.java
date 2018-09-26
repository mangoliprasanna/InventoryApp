package com.example.mango.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

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


    @Override
    public Cursor query(Uri uri,   String[] projections,   String selection,   String[] selectionArgs,   String sortOrder) {
        SQLiteDatabase sqLiteDatabase = productDBHelper.getReadableDatabase();
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
        SQLiteDatabase db = productDBHelper.getWritableDatabase();
        long id = db.insert(ProductContract.ProductEntry.TABLE_NAME, null, contentValues);
        if(id == -1)
            throw new IllegalArgumentException("Unable to Insert." + uri);

        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    @Override
    public int delete(  Uri uri,   String selections,   String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase = productDBHelper.getWritableDatabase();
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
        SQLiteDatabase sqLiteDatabase = productDBHelper.getWritableDatabase();
        int result = sqLiteDatabase.update(ProductContract.ProductEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return result;
    }

    @Override
    public int update( Uri uri, ContentValues contentValues,String selection,   String[] selectionArgs) {
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
