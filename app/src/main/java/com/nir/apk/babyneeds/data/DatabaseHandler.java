package com.nir.apk.babyneeds.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.util.Log;

import androidx.annotation.Nullable;

import com.nir.apk.babyneeds.model.BabyItems;
import com.nir.apk.babyneeds.util.Constants;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "Database Handler";
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DB_NAME,null,Constants.DB_VERSION);
        this.context  = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + Constants.TABLE_NAME + "("
                + Constants.KEY_ID + " INTEGER PRIMARY KEY,"
                + Constants.KEY_GROCERY_ITEM + " TEXT,"
                + Constants.KEY_COLOR + " TEXT,"
                + Constants.KEY_QTY_NUMBER + " INTEGER,"
                + Constants.KEY_ITEM_SIZE + " INTEGER,"
                + Constants.KEY_DATE_NAME + " LONG"
                + ")";

        sqLiteDatabase.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);

        onCreate(sqLiteDatabase);

    }

    //crud operation
    public void addItem(BabyItems items) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, items.getItems());
        values.put(Constants.KEY_COLOR, items.getColor());
        values.put(Constants.KEY_QTY_NUMBER, items.getQuantity());
        values.put(Constants.KEY_ITEM_SIZE, items.getSize());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());

        //insert
        db.insert(Constants.TABLE_NAME,null,values);
        Log.d(TAG, "addItems: ");
    }

    public List<BabyItems> getAllItems() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<BabyItems> items = new ArrayList<>();

        Cursor cursor = db.query(
                Constants.TABLE_NAME,
                new String[] {Constants.KEY_ID,
                        Constants.KEY_GROCERY_ITEM,
                        Constants.KEY_QTY_NUMBER,
                        Constants.KEY_COLOR,
                        Constants.KEY_ITEM_SIZE,
                        Constants.KEY_DATE_NAME},
                null,
                null,
                null,
                null,
                Constants.KEY_DATE_NAME + " DESC"
        );

        if (cursor.moveToFirst()) {
            do {
                DateFormat dateFormat = DateFormat.getDateInstance();
                String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

                BabyItems item = this.getNewBaby(cursor);
//                        new BabyItems(
//                        Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))),
//                        cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)),
//                        cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)),
//                        cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)),
//                        cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)),
//                        formattedDate
//                );
                items.add(item);
            }while (cursor.moveToNext());
        }
        return items;
    }

    private BabyItems getNewBaby(Cursor cursor) {

        DateFormat dateFormat = DateFormat.getDateInstance();
        String formattedDate = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.KEY_DATE_NAME))).getTime());

        return new BabyItems(
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.KEY_ID))),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_GROCERY_ITEM)),
                cursor.getInt(cursor.getColumnIndex(Constants.KEY_QTY_NUMBER)),
                cursor.getString(cursor.getColumnIndex(Constants.KEY_COLOR)),
                cursor.getInt(cursor.getColumnIndex(Constants.KEY_ITEM_SIZE)),
                formattedDate
        );
    }

    //TODO Add update items
    //TODO Add Delete items
    //TODO getItemCount

    public int getItemsCount() {
        String countQuery = "SELECT * FROM " + Constants.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery,null);

        return cursor.getCount();
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                Constants.TABLE_NAME,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(id)}
                );

        db.close();
    }
    public void updateItem(BabyItems item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constants.KEY_GROCERY_ITEM, item.getItems());
        values.put(Constants.KEY_COLOR, item.getColor());
        values.put(Constants.KEY_QTY_NUMBER, item.getQuantity());
        values.put(Constants.KEY_ITEM_SIZE, item.getSize());
        values.put(Constants.KEY_DATE_NAME, java.lang.System.currentTimeMillis());
        //update item
        db.update(Constants.TABLE_NAME,values,
                Constants.KEY_ID + "=?",
                new String[]{String.valueOf(item.getId())}
                );

    }
}
