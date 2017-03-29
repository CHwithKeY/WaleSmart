package com.waletech.walesmart.datainfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by KeY on 2016/4/14.
 */
public class DataBaseAction {

    private static DataBaseHelper helper;
    private static DataBaseAction action;

    public DataBaseAction() {

    }

    public static DataBaseAction onCreate(Context context) {
//        helper = new DataBaseHelper(context);
        helper = DataBaseHelper.getInstance(context);
        action = new DataBaseAction();
        return action;
    }

//    public void create(Context context) {
//        helper = new DataBaseHelper(context);
//    }

    public void insert(String tableName, String[] columnName_set, JSONArray array) throws JSONException {
        switch (tableName) {
            case AddressSet.TABLE_NAME:
                helper.onCreateAddrTable(helper.getWritableDatabase());
                break;

            case BrandSet.TABLE_NAME:
                helper.onCreateBrandTable(helper.getWritableDatabase());
                break;

            default:
                return;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ContentValues cv = new ContentValues();
            for (int k = 0; k < columnName_set.length; k++) {
                cv.put(columnName_set[k], obj.getString(columnName_set[k]));
            }
            helper.getWritableDatabase().insertOrThrow(tableName, null, cv);
        }

        // helper.close();
    }

    public void insertDirect(String tableName, String[] columnName_set, JSONArray array) throws JSONException {
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ContentValues cv = new ContentValues();
            for (int k = 0; k < columnName_set.length; k++) {
                cv.put(columnName_set[k], obj.getString(columnName_set[k]));
            }
            helper.getWritableDatabase().insertOrThrow(tableName, null, cv);
        }

        // helper.close();
    }

    public void insertDirect(String tableName, String[] columnName_set, HashMap<String, String> columnValue_map) {
        ContentValues cv = new ContentValues();
        for (int k = 0; k < columnName_set.length; k++) {
            cv.put(columnName_set[k], columnValue_map.get(columnName_set[k]));
        }
        helper.getWritableDatabase().insertOrThrow(tableName, null, cv);
    }

    public void delete(String tableName) {
        String CHECK_SQL = "SELECT count(*) FROM sqlite_master WHERE type='table' AND name=" + "'" + tableName + "'" + ";";
        Cursor cursor = helper.getReadableDatabase().rawQuery(CHECK_SQL, null);

        if (cursor.moveToNext()) {
            String DEL_SQL = "DROP TABLE " + tableName + ";";
            helper.getWritableDatabase().execSQL(DEL_SQL);
        }

        cursor.close();
        // helper.close();
    }

    public boolean deleteItem(String tableName, String columnName, String columnValue) {
        String delete_SQL = "DELETE FROM " + tableName + " WHERE " + columnName + "=" + "'" + columnValue + "'";

        helper.getWritableDatabase().execSQL(delete_SQL);

        return !check(tableName, columnName, columnValue);
    }

    private boolean check(String tableName, String columnName, String columnValue) {
        String check_SQL = "SELECT * FROM " + tableName + " WHERE " + columnName + "=?";
        Cursor cursor = helper.getReadableDatabase().rawQuery(check_SQL, new String[]{columnValue});

        if (cursor.moveToFirst()) {
            cursor.close();
            // helper.close();
            return true;
        } else {
            cursor.close();
            // helper.close();
            return false;
        }
    }

    /**
     * @param tableName         要查询的表名
     * @param queryColumnName   要查询的，条件的列名
     * @param map               要查询的，条件的列名和值
     * @param queryResultColumn 要查询的，结果的列名
     * @return 封装好的唯一的一个HashSet
     */
    public HashSet<String> query(String tableName, String[] queryColumnName, HashMap<String, String> map, String queryResultColumn, String orderByColumn) {
        // 构造查询语句
        StringBuilder where_name = new StringBuilder("");
        ArrayList<String> where_List = new ArrayList<>();
        int len = map.size() - 1;

        StringBuilder QUERY_SQL = new StringBuilder("SELECT * FROM " + tableName);

        for (int i = 0; i <= len; i++) {
            if (map.get(queryColumnName[i]).equals("")) {
                break;
            }

            where_name.append(queryColumnName[i]).append("=?");

            if ((i + 1 <= len) && map.get(queryColumnName[i + 1]).equals("")) {
                where_List.add(map.get(queryColumnName[i]));
                break;
            }

            if (i != len) {
                where_name.append(" and ");
            }

            where_List.add(map.get(queryColumnName[i]));
        }

        if (!map.get(queryColumnName[0]).equals("")) {
            QUERY_SQL.append(" WHERE ").append(where_name);
        }

        if (!orderByColumn.equals(""))  {
            QUERY_SQL.append(" ORDER BY ").append(orderByColumn).append(" ASC");
        }

        // Log.i("Result", "sql is : " + QUERY_SQL.toString());

        String[] where_value = where_List.toArray(new String[where_List.size()]);

        Cursor cursor = helper.getReadableDatabase().rawQuery(QUERY_SQL.toString(), where_value);

        // 封装查询结果
        HashSet<String> result_set = new HashSet<>();

        if (queryResultColumn.equals(AddressSet.ROAD)) {
            if (cursor.moveToFirst()) {
                String result = getFullAddress(cursor) + cursor.getString(cursor.getColumnIndexOrThrow(queryResultColumn));
                result_set.add(result);
                while (cursor.moveToNext()) {
                    result = getFullAddress(cursor) + cursor.getString(cursor.getColumnIndexOrThrow(queryResultColumn));
                    result_set.add(result);
                }
            }
        } else {
            if (cursor.moveToFirst()) {
                result_set.add(cursor.getString(cursor.getColumnIndexOrThrow(queryResultColumn)));
                while (cursor.moveToNext()) {
                    result_set.add(cursor.getString(cursor.getColumnIndexOrThrow(queryResultColumn)));
                }
            }
        }

        cursor.close();
        // helper.close();

        return result_set;
    }

    public ArrayList<String> query(String tableName, String[] queryColumnName, String[] queryColumnValue, String queryResultColumn, String orderByColumn) {
        HashMap<String, String> map = new HashMap<>();

        for (int i = 0; i < queryColumnName.length; i++) {
            map.put(queryColumnName[i], queryColumnValue[i]);
        }

        HashSet<String> result_set = query(tableName, queryColumnName, map, queryResultColumn, orderByColumn);
        Iterator<String> iterator = result_set.iterator();
        ArrayList<String> result_List = new ArrayList<>();

        while (iterator.hasNext()) {
            result_List.add(iterator.next());
        }

        return result_List;
    }

    private String getFullAddress(Cursor cursor) {
        String result = cursor.getString(cursor.getColumnIndexOrThrow(AddressSet.PROVINCE))
                + cursor.getString(cursor.getColumnIndexOrThrow(AddressSet.CITY))
                + cursor.getString(cursor.getColumnIndexOrThrow(AddressSet.COUNTY));

        return result;
    }

    // 普通查询
    public Cursor query(String SQL) {
        Cursor cursor = helper.getReadableDatabase().rawQuery(SQL, new String[]{});
        return cursor;
    }

    public static void close() {
         helper.close();
    }

}
