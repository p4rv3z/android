package bd.parvez.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ParveZ on 15-Nov-15.
 */
public class MyDatabase extends SQLiteOpenHelper {

    private static final String TAG = "SQLite SQLiteOpenHelper";

    private static final String DATABASE_NAME = "student_info.db";
    private static final int DATABASE_VERSION = 2;

    public static final String STUDENT_TABLE_NAME = "student";
    public static final String COL_STUDENT_NAME = "name";
    public static final String COL_STUDENT_ID = "student_id";
    public static final String COL_INSTITUTION = "institution";
    public static final String COL_DEPARTMENT = "department";

    private static final String CREATE_STUDENT_TABLE = "CREATE TABLE " + STUDENT_TABLE_NAME + " (" + COL_STUDENT_ID + " TEXT PRIMARY KEY, " +
            COL_STUDENT_NAME + " TEXT, " + COL_INSTITUTION + " TEXT, " + COL_DEPARTMENT + " TEXT " + ")";

    public static final String FAVOURITE_TABLE_NAME = "favourite";
    public static final String COL_FAV_ID = "favourite_id";

    private static final String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + FAVOURITE_TABLE_NAME + " (" + COL_FAV_ID + " TEXT PRIMARY KEY " + ")";

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_FAVOURITE_TABLE);
        Log.i(TAG, "Student table has benn created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVOURITE_TABLE_NAME);
        Log.i(TAG, "Student table has been deleted.");
        onCreate(db);
    }
}
