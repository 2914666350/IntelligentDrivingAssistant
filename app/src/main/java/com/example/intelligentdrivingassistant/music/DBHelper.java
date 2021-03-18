package com.example.intelligentdrivingassistant.music;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBHelper {
    private static final String TAG ="DBHelper";
    Context context;
    public static String path;
    SQLiteDatabase db;
    public DBHelper(Context context){
        this.context=context;
    }
    public boolean open(){
        path=context.getFilesDir()+"/"+"user_info.db";
        db= SQLiteDatabase.openOrCreateDatabase(path,null);
        String sql="create table if not exists user_info"+
                "(id varchar(50) primary key, name varchar(50),"+
                "artist varchar(50),duration int(20))";
        try {
            db.execSQL(sql);
            return true;
        }catch(Exception e){
            Log.e(TAG,"open:error"+e.toString());
            e.printStackTrace();
            return false;
        }
    }
    public boolean intert(String id, String name, String artist, long duration){
        if(db!=null&&db.isOpen()){
            ContentValues values=new ContentValues();
            values.put("id",id);
            values.put("name",name);
            values.put("artist",artist);
            values.put("duration",duration);
            long c=db.insert("user_info",null,values);
            if(c>0)
                return true;
            else return false;
        }else {
            return false;
        }
    }
}
