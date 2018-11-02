package com.sahil.shopcart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ACER_SAHIL on 10-02-2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Shop.db";
    private static final String TABLE_NAME = "shop_table";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_MAIL = "price";
    private static final String COL_IMG = "image";
    private static final String COL_BOL = "bool";
    private static final String COL_DES = "description";
    private static final String COL_CAT = "category";


    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY, " +
                 COL_NAME + " TEXT NOT NULL, " +
                 COL_MAIL + " TEXT NOT NULL, " +
                 COL_IMG + " TEXT, " +
                 COL_DES + " TEXT, " +
                 COL_CAT + " TEXT, " +
                 COL_BOL + " boolean NOT NULL default 0);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts" + TABLE_NAME);
        onCreate(db);

    }

    ArrayList<SetGet> getData(int code, String s){

        String query = "";
        if(code == 1){
            query = "select * from " + TABLE_NAME + "";
        }
        else if (code == 2){
            query = "select * from " + TABLE_NAME + " where bool = 1 ";
        }
        else if (code == 3){
            query = "select * from " + TABLE_NAME + " WHERE " +COL_CAT+ " = '"+s +"'";
        }

        ArrayList<SetGet> arrayList = new ArrayList<SetGet>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resu = db.rawQuery(query ,null);
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

    List<String> getCategoryData(){
        List<String> categories = new ArrayList<String>();
        String query = "select DISTINCT "+COL_CAT+" from " + TABLE_NAME + "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor resu = db.rawQuery(query ,null);
        if(resu.getCount()>0){
            if(resu.moveToFirst()){
                do{
                   categories.add(resu.getString(resu.getColumnIndex(COL_CAT)));
                }while (resu.moveToNext());
            }
        }
        db.close();
        return categories;
    }

    void setboolean (String id, int code){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        if (code == 1){
            cv.put(COL_BOL,"1");
        }
        else if (code == 0){
            cv.put(COL_BOL,"0");
        }
        db.update(TABLE_NAME , cv , "id = ?" , new  String[] {id});
    }


    int getTaskCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        String count = "SELECT COUNT (*) FROM " + TABLE_NAME+ " WHERE " + COL_BOL + " = 1";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        return icount;
    }

    public void insertdata(String id, String name, String price, String image, String category, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues insertValues = new ContentValues();
        insertValues.put(COL_ID, id);
        insertValues.put(COL_NAME, name);
        insertValues.put(COL_MAIL, price);
        insertValues.put(COL_IMG, image);
        insertValues.put(COL_CAT, category);
        insertValues.put(COL_DES, description);
        db.insert(TABLE_NAME, null, insertValues);
        db.close();
    }

    public void updatedata(String id, String name, String price, String image, String category, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues updateValues = new ContentValues();
        updateValues.put(COL_NAME, name);
        updateValues.put(COL_MAIL, price);
        updateValues.put(COL_IMG, image);
        updateValues.put(COL_CAT, category);
        updateValues.put(COL_DES, description);
        db.update(TABLE_NAME, updateValues, COL_ID+" = " + id, null);
        db.close();
    }
}
