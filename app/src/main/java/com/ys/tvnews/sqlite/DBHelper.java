package com.ys.tvnews.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by sks on 2015/12/31.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String database_name = "db_user";
    private static int version = 1;
    private Context context;

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    public DBHelper(Context context) {
        super(context, database_name, null, version);
        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建收藏表
        String sql_str = "create table collect(imgUrl1 text,itemTitle text,detailUrl text)";
        db.execSQL(sql_str);
        Log.e("info", "数据库表创建成功");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
           if(oldVersion!=newVersion){
               Toast.makeText(context,"数据库版本更新",Toast.LENGTH_SHORT).show();
           }
    }


}

