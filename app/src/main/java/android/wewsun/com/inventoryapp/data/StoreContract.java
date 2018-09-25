package android.wewsun.com.inventoryapp.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class StoreContract {

    private StoreContract(){}

    public static final String CONTENT_AUTHORITY = "android.wewsun.com.inventoryapp";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STORE = "inventory";

    public static final class StoreEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STORE);

        public static final String TABLE_NAME = "inventory";

        public static final String _ID = BaseColumns._ID;

        public static final String PRODUCT_NAME = "product_name";

        public static final String PRICE = "price";

        public static final String QUANTITY = "quantity";

        public static final String SUPPLIER_NAME = "supplier_name";

        public static final String SUPPLIER_PHONE_NUMBER = "supplier_phone_number";

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;
    }
}
