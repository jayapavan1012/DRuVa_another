package com.druva.app.activity;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databasehelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "server";
    private static final String TABLE_PRODUCT = "address";


    private static final String CREATE_TABLE_INSTRUCTOR = "create table "
            + TABLE_PRODUCT
            + " (id integer primary key autoincrement,"
            + " ip_address varchar(40) );";

    public databasehelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_TABLE_INSTRUCTOR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);

        onCreate(db);

    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ip_address",product.getIp_address());
        db.insert(TABLE_PRODUCT, null, values);
        db.close();
    }


    //    public ArrayList<Object> getAllProduct() {
    public String getProduct(){
        //ArrayList<Object> productArrayList = new ArrayList<Object>();
        // Select All Query
        //String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT+" ORDER BY id DESC";
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT+";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //cursor.moveToFirst();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            return cursor.getString(1);
        }
        else{
            return "false";
        }
        // return contact list
    }
    public void updateProduct(String s){
        String query="update "+TABLE_PRODUCT+" set ip_address "+ "=\""+s+"\""+"  where id=1;" ;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(query);
    }

}
// Getting contacts Count

