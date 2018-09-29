package android.wewsun.com.inventoryapp;

import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.wewsun.com.inventoryapp.data.StoreContract.StoreEntry;

public class StoreCursorAdapter extends CursorAdapter {

    public StoreCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {


        TextView nameTextView = view.findViewById(R.id.name);
        TextView quantityTextView = view.findViewById(R.id.quantity);
        TextView priceTextView = view.findViewById(R.id.price);

        int productColumnIndex = cursor.getColumnIndex(StoreEntry.PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.QUANTITY);
        int priceColumnIndex = cursor.getColumnIndex(StoreEntry.PRICE);
        int phoneColumnIndex = cursor.getColumnIndex(StoreEntry.SUPPLIER_PHONE_NUMBER);

        String productName = cursor.getString(productColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);
        String price = cursor.getString(priceColumnIndex);
        String phoneNumber = cursor.getString(phoneColumnIndex);

        nameTextView.setText(productName);
        quantityTextView.setText(quantity);
        priceTextView.setText(price);
        try {
            if (Integer.parseInt(quantity) < 0) {
                quantity = String.valueOf(0);
            }
        }
        catch (NumberFormatException nf){
            System.out.print("Not a number");
        }
        final long id = cursor.getInt(cursor.getColumnIndexOrThrow(StoreEntry._ID));
        try {
            final int sales = Integer.parseInt(quantity);

            if(quantity == null){
                return;
            }

            view.findViewById(R.id.sale).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.salesButton(id, sales);
                }
            });
            view.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity mainActivity = (MainActivity) context;
                    mainActivity.addButton(id, sales);
                }
            });


        }
        catch (NumberFormatException nf){
            System.out.print("Not a number");
        }
    }
}
