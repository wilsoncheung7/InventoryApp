package android.wewsun.com.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.wewsun.com.inventoryapp.data.StoreContract.StoreEntry;

public class StoreCursorAdapter extends CursorAdapter {

    public StoreCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        TextView nameTextView = view.findViewById(R.id.name);
        TextView summaryTextView =  view.findViewById(R.id.summary);

        int productColumnIndex = cursor.getColumnIndex(StoreEntry.PRODUCT_NAME);
        int quantityColumnIndex = cursor.getColumnIndex(StoreEntry.QUANTITY);

        String productName = cursor.getString(productColumnIndex);
        String quantity = cursor.getString(quantityColumnIndex);

        nameTextView.setText(productName);
        summaryTextView.setText(quantity);
        if (Integer.parseInt(quantity)<0){
            quantity = String.valueOf(0);
        }

        nameTextView.setText(productName);
        summaryTextView.setText(quantity);
    }
}
