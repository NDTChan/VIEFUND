package mobile.bts.com.viefund.CustomFont;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by THANH on 4/14/2018.
 */

public class ColorDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "colortable";
    private static final String TAG = ColorDatabaseHelper.class.getSimpleName();

    private static final String TABLE_COLOR = "Custom_Color";
    private static final String TABLE_ID = "Color_Id";
    private static final String COLUMN_COLOR_CONTENT ="Color_Content";
    public ColorDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_COLOR + "("
                + TABLE_ID + " INTEGER PRIMARY KEY," + COLUMN_COLOR_CONTENT + " TEXT" + ")");
        ContentValues values = new ContentValues();
        values.put(TABLE_ID,"1");
        values.put(COLUMN_COLOR_CONTENT,"#31844C");
        db.insert(TABLE_COLOR,null,values);
        Log.d(TAG, "onCreate: "+"CREATE TABLE " + TABLE_COLOR + "("
                + TABLE_ID + " INTEGER PRIMARY KEY," + COLUMN_COLOR_CONTENT + " TEXT" + ")");
        // Execute script.

    }
    public void UpdateColor(String color){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COLOR_CONTENT,color);
//        db.update(TABLE_COLOR,values,TABLE_ID + " = ?",new int[]{1});
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COLOR);


        // Và tạo lại.
        onCreate(db);
    }
}
