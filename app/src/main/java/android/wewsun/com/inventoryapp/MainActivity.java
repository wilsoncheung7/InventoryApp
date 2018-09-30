package android.wewsun.com.inventoryapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.wewsun.com.inventoryapp.data.StoreContract.StoreEntry;

import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int STORE_LOADER = 0;

    StoreCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fat = findViewById(R.id.fab);
        fat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StockActivity.class);
                startActivity(intent);
            }
        });
        ListView storeListView = findViewById(R.id.list);

        cursorAdapter = new StoreCursorAdapter(this, null);

        View emptyView = findViewById(R.id.empty_view);
        storeListView.setEmptyView(emptyView);

        storeListView.setAdapter(cursorAdapter);

        storeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, StockActivity.class);

                Uri currentStoreUri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, id);

                intent.setData(currentStoreUri);

                startActivity(intent);
            }
        });


        getLoaderManager().initLoader(STORE_LOADER, null, this);
    }

    public void salesButton(long product, int quantity){
       if(quantity>=1){
           quantity--;
           Uri uri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, product);
           ContentValues values = new ContentValues();
           values.put(StoreEntry.QUANTITY, quantity);
           int row = getContentResolver().update(
                   uri,
                   values,
                   null,
                   null
           );
           if(row == 1){
               Toast.makeText(this,"sold", Toast.LENGTH_SHORT).show();
           }
           else {
               Toast.makeText(this,"try again", Toast.LENGTH_SHORT).show();
           }
       }
       else {
           Toast.makeText(this,"out of stock", Toast.LENGTH_SHORT).show();
       }
    }

    public void addButton(long product, int quantity){
        if(quantity>=1){
            quantity++;
            Uri uri = ContentUris.withAppendedId(StoreEntry.CONTENT_URI, product);
            ContentValues values = new ContentValues();
            values.put(StoreEntry.QUANTITY, quantity);
            int row = getContentResolver().update(
                    uri,
                    values,
                    null,
                    null
            );
            if(row == 1){
                Toast.makeText(this,"bought", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"try again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void insertData() {


        ContentValues values = new ContentValues();
        values.put(StoreEntry.PRODUCT_NAME, "Sword Art Online");
        values.put(StoreEntry.PRICE, 16);
        values.put(StoreEntry.QUANTITY, 3);
        values.put(StoreEntry.SUPPLIER_NAME, "Jackie");
        values.put(StoreEntry.SUPPLIER_PHONE_NUMBER, "01186");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                insertData();
                return true;
            case R.id.action_delete:
                deleteAllBooks();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllBooks() {
        int row = getContentResolver().delete(StoreEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", row + " rows deleted from inventory database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                StoreEntry._ID,
                StoreEntry.PRODUCT_NAME,
                StoreEntry.PRICE,
                StoreEntry.QUANTITY,
                StoreEntry.SUPPLIER_NAME,
                StoreEntry.SUPPLIER_PHONE_NUMBER
        };
        return new CursorLoader(this,
                StoreEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}
