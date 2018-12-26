package mobile.bts.com.viefund.ConnSqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import mobile.bts.com.viefund.Model.UserModel;

/**
 * Created by DQVu on 8/16/2016.
 */
public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "android_api";

    // Login table name
    private static final String TABLE_USER = "user";

    // Login Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_UID = "uid";
    private static final String KEY_USERID = "userid";
    private static final String KEY_STATUS = "status";
    public static final String KEY_LANG="language";
    private static final String KEY_CREATED_AT = "created_at";
    //status user
    public static final String STATUS_NOLOGIN = "0"; // uset ko daang nhaop
    public static final String STATUS_LOGIN = "1"; // user dang dang nhap
    public static final String STATUS_LOGOUT = "2"; // user cuoi cung khi logout

    private static final String TABLE_COLOR = "Custom_Color";
    private static final String TABLE_ID = "Color_Id";
    private static final String COLUMN_COLOR_CONTENT ="Color_Content";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //create sqlite db
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        sqLiteDatabase.execSQL("DROP TABLE "+TABLE_USER);
        String CREATE_LOGIN_TABLE = "CREATE TABLE "+TABLE_USER+"("
                +KEY_ID+" INTEGER PRIMARY KEY,"
                +KEY_NAME+" TEXT,"
                +KEY_USERID+" TEXT,"
                +KEY_LANG+" TEXT,"
                +KEY_STATUS+" TEXT"+")";
        sqLiteDatabase.execSQL(CREATE_LOGIN_TABLE);
        String CREATE_COLOR_TABLE = "CREATE TABLE " + TABLE_COLOR + "("
                + TABLE_ID + " INTEGER PRIMARY KEY," + COLUMN_COLOR_CONTENT + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_COLOR_TABLE);
        addColor(sqLiteDatabase);
        Log.d(TAG, "Database tables created");
        Log.d(TAG, "onCreate() called with: " + CREATE_LOGIN_TABLE);
        Log.d(TAG, "onCreate() called with: " + CREATE_COLOR_TABLE);

    }

    public void UpdateColor(String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String[] a = new String[]{"1"};
        values.put(COLUMN_COLOR_CONTENT,color);
        db.update(TABLE_COLOR,values,TABLE_ID + " = ?",a);
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_USER);
        onCreate(sqLiteDatabase);
    }

    //add color
    public void addColor(SQLiteDatabase db) {
//        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(TABLE_ID,"1");
        values.put(COLUMN_COLOR_CONTENT,"#31844C");
        db.insert(TABLE_COLOR,null,values);
        Log.d(TAG, "addColor() returned: " + values.get(TABLE_ID)+" - "+values.get(COLUMN_COLOR_CONTENT));
        // Inserting Row
//        long id = db.insert(TABLE_USER, null, values);
//        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public String getColor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_COLOR,new String[]{COLUMN_COLOR_CONTENT }, TABLE_ID + "=?",
                new String[] {"1"}, null, null, null,        null);
        if (cursor != null)
            cursor.moveToFirst();
        String temp = cursor.getString(0);
        // return contact
        return temp;
    }
    //add user
    public void addUser(UserModel obj) {
        SQLiteDatabase db = this.getWritableDatabase();
//        onCreate(db);
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, obj.getName()); // Name
        values.put(KEY_USERID, obj.getId()); // user_id
        values.put(KEY_LANG,obj.getLang());
        values.put(KEY_STATUS, STATUS_LOGIN); // user_id

        Log.d(TAG, "addUser() returned: " + values.get(KEY_USERID)+" - "+values.get(KEY_NAME)+" - "+values.get(KEY_LANG));
        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
        Log.d(TAG, "New user inserted into sqlite: " + id);
    }

    //set status all data status user = 0
    public void setDefaulAllUser() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Log.d(TAG, "setDefaulAllUser returned: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            do{
                Log.d(TAG, "setDefaulAllUser: "+cursor.getString(cursor.getColumnIndex(KEY_ID)));
                ContentValues values = new ContentValues();
                String[] whereArgs = new String[] {cursor.getString(0)};
                values.put(KEY_ID,cursor.getString(0));
                values.put(KEY_NAME, cursor.getString(1));
                values.put(KEY_USERID,cursor.getString(2));
                values.put(KEY_LANG,cursor.getString(3));
                values.put(KEY_STATUS,STATUS_NOLOGIN);
                db.update(TABLE_USER,values,KEY_ID +"=?",whereArgs);
                cursor.moveToNext();
            }while (cursor.isLast());
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
    }
    //set status login data status user = 1
    public void setLoginUser(UserModel obj, String status) {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "+KEY_NAME+" = '"+obj.getName()+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Log.d(TAG, "setLoginUser returned: " + cursor.getCount());
        if (cursor.getCount() > 0) {
                ContentValues values = new ContentValues();
                String[] whereArgs = new String[] {String.valueOf(cursor.getString(0))};
                values.put(KEY_ID,cursor.getString(0));
                values.put(KEY_NAME, cursor.getString(1));
                values.put(KEY_USERID,cursor.getString(2));
                values.put(KEY_LANG,cursor.getString(3));
                values.put(KEY_STATUS,status);
                db.update(TABLE_USER,values,KEY_ID +"=?",whereArgs);
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
    }

    //get current user store by sqlite
    public HashMap<String, String> getUserDetailsLogout() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "+KEY_STATUS+" = "+STATUS_LOGOUT;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row

        cursor.moveToFirst();
        Log.d(TAG, "getUserDetailsLogout() returned: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            user.put("username", cursor.getString(1));
            user.put("user_id",cursor.getString(2));
            user.put("language",cursor.getString(3));
            user.put(KEY_STATUS,cursor.getString(4));
        }

        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());
        return user;
    }

    //get current user store by sqlite
    public UserModel getUserByUserName(String userName) {
        Log.d(TAG, "getUserByUserName: "+userName);
        UserModel result=  new UserModel();
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "+KEY_NAME+" = '"+userName+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Log.d(TAG, "getUserByUserName() returned: " + cursor.getCount());
        if (cursor.getCount() > 0) {
            user.put("username", cursor.getString(1));
            user.put("user_id",cursor.getString(2));
            user.put("language",cursor.getString(3));
            user.put(KEY_STATUS,cursor.getString(4));
            result.setName(cursor.getString(1));
            result.setId(cursor.getString(2));
            result.setLang(cursor.getString(3));
            result.setStatus(cursor.getString(4));
            cursor.close();
            db.close();
            return result;
        }
        else{
            cursor.close();
            db.close();
            return null;
        }
    }


    //delete all user
    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);

        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
}
