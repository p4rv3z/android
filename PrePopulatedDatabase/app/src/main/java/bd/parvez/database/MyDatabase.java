package bd.parvez.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bd.parvez.myservices.CopyFilesFromAssets;
import bd.parvez.myservices.MyTag;

/**
 * Created by ParveZ on 23-Nov-15.
 */
public class MyDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "friends_info.sqlite";

    public static final String TABLE_NAME = "friend_table";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_MOBILE = "mobile";
    public static final String COL_BLOOD_GROUP = "blood_group";
    public static final int DATABASE_VERSION = 1;
    private static final String className = "MyDatabase";
    public static String databasePath;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        MyTag.myTag(className, "MyDatabase() method called.");
        databasePath = "/data/data/" + context.getPackageName() + "/databases/" + DATABASE_NAME;
        createDatabase(context, DATABASE_NAME, databasePath);
    }


    private void createDatabase(Context context, String fileName, String databasePath) {
        MyTag.myTag(className, "createDatabase() method called.");

        if (!CopyFilesFromAssets.checkPath(databasePath)) {
            this.getReadableDatabase();
            CopyFilesFromAssets.copyFromAssetToDatabase(context, fileName, databasePath);
            SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.OPEN_READWRITE);
            MyTag.myTag(className, "Database created.");
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        MyTag.myTag(className, "onCreate() method called.");


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        MyTag.myTag(className, "onUpgrade() method called.");
        MyTag.myTag(className, "oldVersion " + oldVersion + " newVersion " + newVersion);

    }
}