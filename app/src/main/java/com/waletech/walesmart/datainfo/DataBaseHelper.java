package com.waletech.walesmart.datainfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KeY on 2016/4/14.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "walesmart.db";

    private static DataBaseHelper mInstance = null;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DataBaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        onCreateAddrTable(db);

        onCreateBrandTable(db);
    }

    public void onCreateAddrTable(SQLiteDatabase db) {
        StringBuilder CREATE_DB_SQL = new StringBuilder();

        CREATE_DB_SQL.append("CREATE TABLE ");
        CREATE_DB_SQL.append(AddressSet.TABLE_NAME);
        CREATE_DB_SQL.append(" (");
        // 前后均有空格
        CREATE_DB_SQL.append(AddressSet._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, ");
        CREATE_DB_SQL.append(AddressSet.PROVINCE + " TEXT, ");
        CREATE_DB_SQL.append(AddressSet.CITY + " TEXT, ");
        CREATE_DB_SQL.append(AddressSet.COUNTY + " TEXT, ");
        CREATE_DB_SQL.append(AddressSet.ROAD + " TEXT, ");
        CREATE_DB_SQL.append(AddressSet.SHOP + " TEXT");
        CREATE_DB_SQL.append(");");

        db.execSQL(CREATE_DB_SQL.toString());
    }

    public void onCreateBrandTable(SQLiteDatabase db) {
        StringBuilder CREATE_DB_SQL = new StringBuilder();

        CREATE_DB_SQL.append("CREATE TABLE ");
        CREATE_DB_SQL.append(BrandSet.TABLE_NAME);
        CREATE_DB_SQL.append(" (");
        // 前后均有空格
        CREATE_DB_SQL.append(BrandSet._ID + "  INTEGER PRIMARY KEY AUTOINCREMENT, ");
        CREATE_DB_SQL.append(BrandSet.BRAND + " TEXT, ");
        CREATE_DB_SQL.append(BrandSet.PRODUCT_NAME + " TEXT");
        CREATE_DB_SQL.append(");");

        db.execSQL(CREATE_DB_SQL.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            switch (oldVersion) {
                case 0:
                    db.execSQL("");
                case 1:
                    db.execSQL("");
                default:
                    break;
            }
        }
    }

}
