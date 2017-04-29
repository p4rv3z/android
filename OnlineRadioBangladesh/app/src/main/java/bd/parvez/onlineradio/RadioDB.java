package bd.parvez.onlineradio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Parvez on 6/4/2015.
 */
public class RadioDB extends SQLiteOpenHelper {
    public static String DB_PATH;
    private SQLiteDatabase database;
    private Context context;
    private static RadioDB radioDB;
    public static final String KEY_ROWID = "id";
    public static final String DB_NAME = "radio_list.sqlite";
    public static final String TABLE_NAME = "bangladesh";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String FAV = "fav";
    private static final String TAG = "Online Radio Bangladesh";

    private RadioDB(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        Log.v(TAG, "Contructor of DatabaseHelper");
        // database path /data/data/pkg-name/databases/
        String packageName = context.getPackageName();
        DB_PATH = "/data/data/" + packageName + "/databases/";
        this.database = openDatabase();
    }

    public static RadioDB getInstance(Context context) {
        if (radioDB == null) {
            radioDB = new RadioDB(context);
        }
        return radioDB;
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    public SQLiteDatabase openDatabase() {
        String path = DB_PATH + DB_NAME;
        if (database == null) {
            createDatabase();
            database = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE);
        }
        return database;
    }

    private void createDatabase() {
        boolean dbExists = checkDB();
        if (!dbExists) {
            this.getReadableDatabase();
            Log.v(TAG, "Database doesn't exist. Copying database from assets...");
            copyDatabase();
        } else {
            Log.v(TAG, "Database already exists");
        }
    }

    private void copyDatabase() {
        try {
            InputStream dbInputStream = context.getAssets().open(DB_NAME);
            String path = DB_PATH + DB_NAME;
            OutputStream dbOutputStream = new FileOutputStream(path);
            byte[] buffer = new byte[4096];
            int readCount = 0;
            while ((readCount = dbInputStream.read(buffer)) > 0) {
                dbOutputStream.write(buffer, 0, readCount);
            }

            dbInputStream.close();
            dbOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private boolean checkDB() {
        String path = DB_PATH + DB_NAME;
        File file = new File(path);
        if (file.exists()) {
            Log.v(TAG, "Database already exists");
            return true;
        }
        Log.v(TAG, "Database does not exists");
        return false;
    }

    public synchronized void close() {
        if (this.database != null) {
            this.database.close();
        }
    }

    public ArrayList<RadioClass> getAllData() {
        ArrayList<RadioClass> allData = new ArrayList<RadioClass>();

        // String[] columns={NAME_FIELD, EMAIL_FIELD, PHONE_FIELD};
        // SELECT * FROM EMPLOYEE;
        Cursor cursor = this.database.query(TABLE_NAME, null, null, null,
                null, null, null);

        // Cursor cursor = db.rawQuery("SELECT * FROM EMPLOYEE", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {

                String name = cursor.getString(cursor.getColumnIndex(NAME));
                String url = cursor.getString(cursor.getColumnIndex(URL));
                int fav = cursor.getInt(cursor.getColumnIndex(FAV));
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ROWID));
                RadioClass radioClass = new RadioClass(name, url, fav, id);

                allData.add(radioClass);
                cursor.moveToNext();
            }
        }
        cursor.close();

        return allData;
    }

    ///Update database
    public int updateFav(int id, int fav) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FAV, fav);
        try {
            //int updated = db.update(TABLE_NAME, values, KEY_ROWID + " =? ", new String[]{("" + id)});
            int updated = db.update(TABLE_NAME, values, KEY_ROWID + " = " + (id), null);
            Log.v(TAG, "id:" + (id + 1) + "done " + updated);
            return updated;
        } catch (Exception e) {
            Log.v(TAG, e.getMessage());
            return 0;
        } finally {
            db.close();
            Log.v(TAG, "DB close");
        }
    }

    ///Delete database
    public int deleteRow(int id) {
        if (getAllData().size() > 1) {
            SQLiteDatabase db = this.getWritableDatabase();
            try {
                //int updated = db.update(TABLE_NAME, values, KEY_ROWID + " =? ", new String[]{("" + id)});
                int del = db.delete(TABLE_NAME, KEY_ROWID + " = " + id, null);
                Log.v(TAG, "id:" + (id + 1) + "done " + del);
                return del;
            } catch (Exception e) {
                Log.v(TAG, e.getMessage());
                return 0;
            } finally {
                db.close();
                Log.v(TAG, "DB close");
            }
        } else {
            Toast.makeText(context, "You can't delete last one data.", Toast.LENGTH_LONG).show();
            return 0;
        }
        //int updated = db.update(TABLE_NAME, values, KEY_ROWID + "=" + id, null);// _id=eID
        //String sql = "UPDATE bangladesh SET fav = "+"\'"+fav+"\'"+" WHERE id = "+id;
        //db.execSQL(sql);


    }

    //insert Radio in DB
    public long insertRadio(RadioClass radio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NAME, radio.getName());
        values.put(URL, radio.getUrl());
        values.put(FAV, radio.getFav());
        long inserted = db.insert(TABLE_NAME, null, values);
        db.close();
        return inserted;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
