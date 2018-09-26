package com.example.mango.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ProductContract {

    public static final String CONTENT_AUTHORITY = "com.example.mango.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_PRODUCTS = "products";

    public static abstract class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_PRODUCTS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PRODUCTS;

        public static final String COLUMN_PRD_ID = BaseColumns._ID;
        public static final String COLUMN_PRD_NAME = "prd_name";
        public static final String COLUMN_PRD_PRICE = "prd_price";
        public static final String COLUMN_PRD_QUANTITY = "prd_quantity";
        public static final String COLUMN_SUP_NAME = "prd_sup_name";
        public static final String COLUMN_SUP_PHONE = "prd_sup_phone";
    }
}
