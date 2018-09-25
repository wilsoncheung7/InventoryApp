package android.wewsun.com.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.wewsun.com.inventoryapp.data.StoreContract.StoreEntry;

public class StoreProvider extends ContentProvider {

    public static final String LOG_TAG = StoreProvider.class.getSimpleName();

    private static final int STORE = 100;

    private static final int STORE_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY,StoreContract.PATH_STORE, STORE);

        uriMatcher.addURI(StoreContract.CONTENT_AUTHORITY, StoreContract.PATH_STORE+ "/#", STORE_ID);
    }

    private StoreDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new StoreDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectArgs, String sortOrder){

        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor;

        int match = uriMatcher.match(uri);

        switch (match){
            case STORE:

                cursor = database.query(StoreEntry.TABLE_NAME, projection, selection, selectArgs,
                        null, null, sortOrder);
                break;
            case STORE_ID:

                selection = StoreEntry._ID + "=?";
                selectArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(StoreEntry.TABLE_NAME, projection, selection, selectArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case STORE:
              return insertStock(uri, contentValues);
            default:
              throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertStock(Uri uri, ContentValues values){
        String name = values.getAsString(StoreEntry.PRODUCT_NAME);
        if (name == null){
            throw new IllegalArgumentException("Product require a name");
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        long id = database.insert(StoreEntry.TABLE_NAME, null, values);
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case STORE:
            return update(uri, contentValues, selection, selectionArgs);
            case STORE_ID:
            selection = StoreEntry._ID + "=?";
            selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
            return updateInventory(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    public int updateInventory(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){

        if (contentValues.containsKey(StoreEntry.PRODUCT_NAME)){
            String name = contentValues.getAsString(StoreEntry.PRODUCT_NAME);
            if (name == null){
                throw new IllegalArgumentException("require a product name");
            }
        }
        if (contentValues.containsKey(StoreEntry.QUANTITY)){
            Integer quantity = contentValues.getAsInteger(StoreEntry.QUANTITY);
            if (quantity == null && quantity<0){
                throw new IllegalArgumentException("require valid quantity");
            }
        }
        if (contentValues.containsKey(StoreEntry.PRICE)){
            Integer price = contentValues.getAsInteger(StoreEntry.PRICE);
            if (price != null && price<0){
                throw new IllegalArgumentException("require valid price");
            }
        }
        if (contentValues.size() == 0){
            return 0;
        }
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        int row = database.update(StoreEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (row != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return row;
    }

    @Override
    public int delete(Uri uri, String selection,String[] selectionArgs) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        int rowsDeleted;

        final int match = uriMatcher.match(uri);
        switch (match){
            case STORE:
                rowsDeleted = database.delete(StoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STORE_ID:
                selection = StoreEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(StoreEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match){
            case STORE:
                return StoreEntry.CONTENT_LIST_TYPE;
            case STORE_ID:
                return StoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri + " with match " + match);
        }
    }
}
