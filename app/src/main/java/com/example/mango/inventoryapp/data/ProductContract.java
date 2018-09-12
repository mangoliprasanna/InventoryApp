package com.example.mango.inventoryapp.data;

import android.provider.BaseColumns;

public class ProductContract {

    public class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "Products";

        public static final String COLUMN_PRD_ID = BaseColumns._ID;
        public static final String COLUMN_PRD_NAME = "prd_name";
        public static final String COLUMN_PRD_PRICE = "prd_price";
        public static final String COLUMN_PRD_QUANTITY = "prd_quantity";
        public static final String COLUMN_SUP_NAME = "prd_sup_name";
        public static final String COLUMN_SUP_PHONE = "prd_sup_phone";

    }

}
