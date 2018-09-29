package android.wewsun.com.inventoryapp;

import android.app.AlertDialog;
import android.app.LoaderManager;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.wewsun.com.inventoryapp.data.StoreContract.StoreEntry;
import android.widget.TextView;
import android.widget.Toast;

public class StockActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_STORE_LOADER = 0;

    private Uri currentStoreUri;

    private EditText productEditText;

    private EditText priceEditText;

    private EditText quantityEditText;

    private EditText supplierEditText;

    private EditText phoneEditText;

    private boolean storeHasChanged = false;

    private Button button;

    private Button saleButton;

    private Button buyButton;

    private Toast myToast;

    private int quantity;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            storeHasChanged = true;
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_stock);

        Intent intent = getIntent();
        currentStoreUri = intent.getData();

        if (currentStoreUri == null) {
            setTitle("Add a book");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the book");
            getLoaderManager().initLoader(EXISTING_STORE_LOADER, null, this);
        }
        productEditText = findViewById(R.id.product_edit_name);
        quantityEditText = findViewById(R.id.quantity_edit_name);
        priceEditText = findViewById(R.id.price_edit_name);
        supplierEditText = findViewById(R.id.supplier_edit_name);
        phoneEditText = findViewById(R.id.phone_edit_name);

        productEditText.setOnTouchListener(mTouchListener);
        quantityEditText.setOnTouchListener(mTouchListener);
        priceEditText.setOnTouchListener(mTouchListener);
        supplierEditText.setOnTouchListener(mTouchListener);
        phoneEditText.setOnTouchListener(mTouchListener);

        button = findViewById(R.id.buttonCall);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneEditText.getText().toString()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        saleButton = findViewById(R.id.sale);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sales();
            }
        });
    }

        public void sales(){
            quantity = Integer.parseInt(quantityEditText.getText().toString());
            quantity--;
        }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButton) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editing");
        builder.setPositiveButton("Discard", discardButton);
        builder.setNegativeButton("Keep Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
                saveInventory();
                finish();
                return true;
            case R.id.action_delete:

                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!storeHasChanged) {
                    NavUtils.navigateUpFromSameTask(StockActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonOnClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(StockActivity.this);
                    }
                };
                showUnsavedChangesDialog(discardButtonOnClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!storeHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void saveInventory() {
        String nameString = productEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String supplierString = supplierEditText.getText().toString().trim();
        String phoneString = phoneEditText.getText().toString().trim();

        if (currentStoreUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString
        ) && TextUtils.isEmpty(supplierString) && TextUtils.isEmpty(phoneString)) {
            return;
        }

        ContentValues contentValues = new ContentValues();
        // Check if product name field is empty and if yes, display a toast message to the user
        if (TextUtils.isEmpty(nameString)) {

         myToast =  Toast.makeText(this, "Product name is required", Toast.LENGTH_LONG);
         myToast.show();
         return;
        }
        // If product name is filled, save it to the database
        else {
            contentValues.put(StoreEntry.PRODUCT_NAME, nameString);
        }

        // Check if product name field is empty and if yes, display a toast message to the user
        if (TextUtils.isEmpty(priceString)) {
        myToast = Toast.makeText(this, "Price is required", Toast.LENGTH_LONG);
        myToast.show();
        return;
        }
        // If product name is filled, save it to the database
        else {
            contentValues.put(StoreEntry.PRICE, nameString);
        }
        // Check if product name field is empty and if yes, display a toast message to the user
        if (TextUtils.isEmpty(quantityString)) {
            myToast = Toast.makeText(this, "Quantity name is required", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }
        // If product name is filled, save it to the database
        else {
            contentValues.put(StoreEntry.QUANTITY, nameString);
        }
        // Check if product name field is empty and if yes, display a toast message to the user
        if (TextUtils.isEmpty(supplierString)) {
           myToast = Toast.makeText(this, "Supplier name is required", Toast.LENGTH_SHORT);
           myToast.show();
           return;
        }
        // If product name is filled, save it to the database
        else {
            contentValues.put(StoreEntry.SUPPLIER_NAME, nameString);
        }
        // Check if product name field is empty and if yes, display a toast message to the user
        if (TextUtils.isEmpty(phoneString)) {
            myToast = Toast.makeText(this, "Supplier's phone number is required", Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }
        // If product name is filled, save it to the database
        else {
            contentValues.put(StoreEntry.SUPPLIER_PHONE_NUMBER, nameString);
        }
        contentValues.put(StoreEntry.PRODUCT_NAME, nameString);
        contentValues.put(StoreEntry.PRICE, priceString);
        contentValues.put(StoreEntry.QUANTITY, quantityString);
        contentValues.put(StoreEntry.SUPPLIER_NAME, supplierString);
        contentValues.put(StoreEntry.SUPPLIER_PHONE_NUMBER, phoneString);

        if (currentStoreUri == null) {
            Uri uri = getContentResolver().insert(StoreEntry.CONTENT_URI, contentValues);

            if (uri == null) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        } else {
            int row = getContentResolver().update(currentStoreUri, contentValues, null, null);
            if (row == 0) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentStoreUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this book?");
        builder.setPositiveButton("Sale", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteInventory();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        if (currentStoreUri != null) {
            int row = getContentResolver().delete(currentStoreUri, null, null);
            if (row == 0) {
                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "deleted", Toast.LENGTH_SHORT).show();
            }
        }
        finish();
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
                currentStoreUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (cursor.moveToFirst()) {
            int productColumnIndex = cursor.getColumnIndex(StoreEntry.PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(StoreEntry.PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.QUANTITY);
            int supplierColumnIndex = cursor.getColumnIndex(StoreEntry.SUPPLIER_NAME);
            int phoneColumnIndex = cursor.getColumnIndex(StoreEntry.SUPPLIER_PHONE_NUMBER);

            String product = cursor.getString(productColumnIndex);
            String price = cursor.getString(priceColumnIndex);
            String quantity = cursor.getString(quantityColumnIndex);
            String supplier = cursor.getString(supplierColumnIndex);
            String phone = cursor.getString(phoneColumnIndex);

            productEditText.setText(product);
            priceEditText.setText(price);
            quantityEditText.setText(quantity);
            supplierEditText.setText(supplier);
            phoneEditText.setText(phone);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        productEditText.setText("");
        priceEditText.setText(String.valueOf(0));
        quantityEditText.setText(String.valueOf(1));
        supplierEditText.setText("");
        phoneEditText.setText("");
    }
}
