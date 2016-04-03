package com.ys.tvnews.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sks on 2015/12/31.
 */
public class OperateDB {

    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private Context context;

    public OperateDB(Context context){

        System.out.println("执行了OperateDB的初始化工作");

        if(dbHelper==null) {
            dbHelper = new DBHelper(context);
        }
        db = dbHelper.getWritableDatabase();
    }

    /**
     * @作用：执行带占位符的select语句，查询数据，返回Cursor
     * @param sql
     * @param selectionArgs
     * @return Cursor
     */
    public Cursor selectCursor(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }
    /**
     * @作用：执行带占位符的select语句，返回结果集的个数
     * @param sql
     * @param selectionArgs
     * @return int
     */
    public int selectCount(String sql, String[] selectionArgs) {
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        int count = cursor.getCount();
        if (cursor != null) {
            cursor.close();
        }
        return count;
    }
    /**
     * @作用：执行带占位符的update、insert、delete语句，更新数据库，返回true或false
     * @param sql
     * @param bindArgs
     * @return boolean
     */
    public boolean updateData(String sql, String[] bindArgs) {
        boolean flag = false;
        try {
            db.execSQL(sql, bindArgs);
            flag = true;
        } catch (Exception e) {
            System.out.println("执行了异常操作");
            e.printStackTrace();
        }
        return flag;
    }
    /**
     * @作用：执行带占位符的select语句，返回多条数据，放进List集合中。
     * @param sql
     * @param selectionArgs
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> selectData(String sql,String[] selectionArgs) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        String[] cols_names = cursor.getColumnNames();
        boolean flag = cursor.moveToFirst();
        while (flag) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < cols_names.length; i++) {
                String cols_value = cursor.getString(i);
                map.put(cols_names[i], cols_value);
            }
            list.add(map);
            flag = cursor.moveToNext();
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
    /**
     * 作用：将cursor转成list集合
     *
     * @param cursor
     * @return List<Map<String, Object>>
     */
    public List<Map<String, Object>> cursorToList(Cursor cursor) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String[] cols_names = cursor.getColumnNames();
        while (cursor.moveToNext()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 0; i < cols_names.length; i++) {
                Object cols_value = null;
                switch (cursor.getType(i)) {
                    case 1:
                        cols_value = cursor.getInt(cursor.getColumnIndex(cols_names[i]));
                        break;
                    case 2:
                        cols_value = cursor.getFloat(cursor.getColumnIndex(cols_names[i]));
                        break;
                    case 3:
                        cols_value = cursor.getString(cursor.getColumnIndex(cols_names[i]));
                        break;
                    case 4:
                        cols_value = cursor.getBlob(cursor.getColumnIndex(cols_names[i]));
                        break;
                    default:
                        cols_value = null;
                        break;
                }
                map.put(cols_names[i], cols_value);
            }
            list.add(map);
        }
        if (cursor != null) {
            cursor.close();
        }
        return list;
    }
    public void closeDb() {
        if (db != null) {
            db.close();
        }
    }
}
