package bd.parvez.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import bd.parvez.myservices.MyTag;
import bd.parvez.prepopulateddatabase.Friend;

/**
 * Created by ParveZ on 23-Nov-15.
 */
public class MyDatabaseHelper {
    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    private static final String COL_NAME[] = {MyDatabase.COL_ID, MyDatabase.COL_NAME,
            MyDatabase.COL_MOBILE, MyDatabase.COL_BLOOD_GROUP};

    private static final String className = "MyDatabaseHelper";

    public MyDatabaseHelper(Context context) {
        MyTag.myTag(className, "MyDatabaseHelper() method called.");
        sqLiteOpenHelper = new MyDatabase(context);

    }

    public void openDB() {
        MyTag.myTag(className, "openDB() method called.");
        sqLiteDatabase = sqLiteOpenHelper.getReadableDatabase();

    }

    public void closeDB() {
        sqLiteOpenHelper.close();
        MyTag.myTag(className, "closeDB() method called.");
    }

    public List<Friend> loadAllData() {
        MyTag.myTag(className, "loadAllData() method called.");
        Cursor cursor = sqLiteDatabase.query(MyDatabase.TABLE_NAME, COL_NAME, null, null, null, null, null);
        List<Friend> friendList = cursorToList(cursor);
        return friendList;
    }

    private List<Friend> cursorToList(Cursor cursor) {
        List<Friend> friendList = new ArrayList<>();
        MyTag.myTag(className, "cursorToList() method called.");
        MyTag.myTag(className, "Returns " + cursor.getCount() + " rows.");
        //Log.i(TAG, "Returns " + cursor.getCount() + " rows.");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Friend friend = new Friend();
                friend.setId(cursor.getInt(cursor.getColumnIndex(COL_NAME[0])));
                friend.setName(cursor.getString(cursor.getColumnIndex(COL_NAME[1])));
                friend.setMobile(cursor.getString(cursor.getColumnIndex(COL_NAME[2])));
                friend.setBloodGroup(cursor.getString(cursor.getColumnIndex(COL_NAME[3])));
                friendList.add(friend);
            }
        }
        return friendList;
    }
}
