package com.googlemapmarker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Senani Recebov on 12/22/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DB_VERSION = 3;
    private static final String DB_NAME = "map";
    private static final String DB_TABLE_NAME = "markers";

    //table variables
    private static final String KEY_ID = "id";
    private static final String KEY_LAT = "latitude";
    private static final String KEY_LONG = "longitude";

    public DatabaseHandler(Context context) {
        super(context, DB_TABLE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String create_table = " CREATE TABLE " + DB_TABLE_NAME + " ("
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_LAT + " TEXT, "
                + KEY_LONG + " TEXT" +" )";
        sqLiteDatabase.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addLatAndLong(double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LAT, lat);
        values.put(KEY_LONG, lon);
        db.insert(DB_TABLE_NAME, null, values);
        db.close();
    }

    public ArrayList<Model> getList() {
        ArrayList<Model> list = new ArrayList<>();
        String query = "SELECT * FROM " + DB_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                Model model = new Model();
                model.setLat(Double.parseDouble(cursor.getString(1)));
                model.setLon(Double.parseDouble(cursor.getString(2)));

                list.add(model);
            }while(cursor.moveToNext());
        }

        return list;
    }
}
