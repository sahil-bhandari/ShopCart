package com.sahil.shopcart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by ACER_SAHIL on 10-02-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Shop.db";
    public static final String TABLE_NAME = "shop_table";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_MAIL = "price";
    public static final String COL_IMG = "image";
    public static final String COL_BOL = "bool";
    public static final String COL_DES = "description";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 COL_NAME + " TEXT NOT NULL, " +
                 COL_MAIL + " TEXT NOT NULL, " +
                 COL_IMG + " TEXT, " +
                 COL_DES + " TEXT, " +
                 COL_BOL + " boolean NOT NULL default 0);";

        db.execSQL(CREATE_TABLE);

//        db.execSQL("create table " + TABLE_NAME + " " +
//                "(id integer primary key autoincrement, name text,mail text,image text,bool boolean NOT NULL default 0," +
//                " cat text, description text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts" + TABLE_NAME);
        onCreate(db);

    }

    public ArrayList<SetGet> getData(int code, String s){

        String query = "";

        if(code == 1){
            query = "select * from " + TABLE_NAME + "";
        }
        else if (code == 2){
            query = "select * from " + TABLE_NAME + " where bool = 1 ";
        }
        else if (code == 3){
            query = "select * from " + TABLE_NAME + " WHERE cat = '"+s +"'";
        }

        ArrayList<SetGet> arrayList = new ArrayList<SetGet>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resu = db.rawQuery(query ,null);
        resu.moveToFirst();
        if(resu.moveToFirst()){
            do{
                SetGet r = new SetGet();
                r.setId(resu.getString(resu.getColumnIndex(COL_ID)));
                r.setName(resu.getString(resu.getColumnIndex(COL_NAME)));
                r.setMail(resu.getString(resu.getColumnIndex(COL_MAIL)));
                r.setImage(resu.getString(resu.getColumnIndex(COL_IMG)));
                r.setDescrip(resu.getString(resu.getColumnIndex(COL_DES)));

                arrayList.add(r);
            }while (resu.moveToNext());

        }
        db.close();
        return arrayList;
    }

//    public ArrayList<SetGet> getbool1(){
//        ArrayList<SetGet> arrayList1 = new ArrayList<SetGet>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor resu1 = db.rawQuery("select * from " + TABLE_NAME + " where bool = 1 " ,null);
//        resu1.moveToFirst();
//        if(resu1.moveToFirst()){
//            do{
//                SetGet r = new SetGet();
//                r.setId(resu1.getString(resu1.getColumnIndex(COL_ID)));
//                r.setName(resu1.getString(resu1.getColumnIndex(COL_NAME)));
//                r.setMail(resu1.getString(resu1.getColumnIndex(COL_MAIL)));
//                r.setImage(resu1.getString(resu1.getColumnIndex(COL_IMG)));
//
//                arrayList1.add(r);
//            }while (resu1.moveToNext());
//
//        }
//        db.close();
//        return arrayList1;
//    }

    boolean setboolean (String id, int code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (code == 1){
            cv.put(COL_BOL,"1");
        }
        else if (code == 0){
            cv.put(COL_BOL,"0");
        }
        db.update(TABLE_NAME , cv , "id = ?" , new  String[] {id});
        return  true;
    }

//    //set bool=0
//    boolean setbool0 (String id){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put(COL_BOL,"0");
//        db.update(TABLE_NAME , cv , "id = ?" , new  String[] {id});
//        return  true;
//    }


    public int getTaskCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT COUNT (*) FROM " + TABLE_NAME+ " WHERE " + COL_BOL + " = 1";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount;
    }

//    public ArrayList<SetGet> getAllData(String s) {
//        ArrayList<SetGet> arrayList = new ArrayList<SetGet>();
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor resu = db.rawQuery("select * from " + TABLE_NAME + " WHERE cat = '"+s +"'",null);
//        resu.moveToFirst();
//        if(resu.moveToFirst()){
//            do{
//                SetGet r = new SetGet();
//                r.setId(resu.getString(resu.getColumnIndex(COL_ID)));
//                r.setName(resu.getString(resu.getColumnIndex(COL_NAME)));
//                r.setMail(resu.getString(resu.getColumnIndex(COL_MAIL)));
//                r.setImage(resu.getString(resu.getColumnIndex(COL_IMG)));
//                r.setDescrip(resu.getString(resu.getColumnIndex(COL_DES)));
//
//                arrayList.add(r);
//            }while (resu.moveToNext());
//
//        }
//        db.close();
//        return arrayList;
//    }

}
