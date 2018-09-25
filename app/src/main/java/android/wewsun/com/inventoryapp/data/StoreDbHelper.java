package android.wewsun.com.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.wewsun.com.inventoryapp.data.StoreContract.StoreEntry;

public class StoreDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = StoreDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "inventory";

    private static final int DATABASE_VERSION = 5;

    public StoreDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + StoreEntry.TABLE_NAME + "("
                + StoreEntry._ID + " INTEGER, "
                + StoreEntry.PRODUCT_NAME + " TEXT, "
                + StoreEntry.PRICE + " INTEGER, "
                + StoreEntry.QUANTITY + " INTEGER, "
                + StoreEntry.SUPPLIER_NAME + " TEXT,"
                + StoreEntry.SUPPLIER_PHONE_NUMBER + " TEXT);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
