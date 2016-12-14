package bd.parvez.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import bd.parvez.sqlite.Student;

/**
 * Created by ParveZ on 15-Nov-15.
 */
public class MyDatabaseHelper {

    SQLiteOpenHelper sqLiteOpenHelper;
    SQLiteDatabase sqLiteDatabase;
    private static final String TAG = "SQLite MyDatabaseHelper";

    private static final String[] COL_NAME = {MyDatabase.COL_STUDENT_NAME, MyDatabase.COL_STUDENT_ID,
            MyDatabase.COL_INSTITUTION, MyDatabase.COL_DEPARTMENT};

    public MyDatabaseHelper(Context context) {
        sqLiteOpenHelper = new MyDatabase(context);
    }

    public void openDB() {
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
        Log.i(TAG, "DATABASE OPENED");
    }

    public void closeDB() {
        sqLiteOpenHelper.close();
        Log.i(TAG, "DATABASE CLOSED");
    }

    /**
     * @param student insert it into database
     * @return long value or database position
     */
    public boolean insertInDB(Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME[0], student.getName());
        contentValues.put(COL_NAME[1], student.getId());
        contentValues.put(COL_NAME[2], student.getInstitution());
        contentValues.put(COL_NAME[3], student.getDepartment());
        long insertId = sqLiteDatabase.insert(MyDatabase.STUDENT_TABLE_NAME, null, contentValues);
        Log.i(TAG, "Data insert in Database and id is: " + insertId);
        return (insertId != -1);
    }

    /**
     * @return a list of all Student's data
     */
    public List<Student> loadAllData() {
        Cursor cursor = sqLiteDatabase.query(MyDatabase.STUDENT_TABLE_NAME, COL_NAME, null, null, null, null, null);
        List<Student> studentList = cursorToList(cursor);
        return studentList;
    }

    /**
     * @return a list of all Student's data ordered by ASC
     */
    public List<Student> loadAllASC() {
        Cursor cursor = sqLiteDatabase.query(MyDatabase.STUDENT_TABLE_NAME, COL_NAME, null, null, null, null, COL_NAME[0] + " ASC");
        List<Student> studentList = cursorToList(cursor);
        return studentList;
    }

    /**
     * @return a list of all Student's data ordered by ASC
     */
    public List<Student> loadAllDESC() {
        //Cursor cursor = sqLiteDatabase.query(MyDatabase.STUDENT_TABLE_NAME, COL_NAME, COL_NAME[3] + "= \"CSE\"", null, null, null, COL_NAME[0] + " DESC");
        Cursor cursor = sqLiteDatabase.query(MyDatabase.STUDENT_TABLE_NAME, COL_NAME, null, null, null, null, COL_NAME[0] + " DESC");
        List<Student> studentList = cursorToList(cursor);
        return studentList;
    }

    /**
     * @return a list of all Favourite Student's data ordered by ASC
     */
    public List<Student> loadAllFav() {
        String rawQuery = ("SELECT " + MyDatabase.STUDENT_TABLE_NAME + ".* FROM " + MyDatabase.STUDENT_TABLE_NAME
                + " JOIN " + MyDatabase.FAVOURITE_TABLE_NAME + " ON " + MyDatabase.STUDENT_TABLE_NAME + "."
                + MyDatabase.COL_STUDENT_ID + " = " + MyDatabase.FAVOURITE_TABLE_NAME + "." + MyDatabase.COL_FAV_ID);
        Cursor cursor = sqLiteDatabase.rawQuery(rawQuery, null);
        List<Student> studentList = cursorToList(cursor);
        return studentList;
    }


    @NonNull
    private List<Student> cursorToList(Cursor cursor) {
        List<Student> studentList = new ArrayList<Student>();
        Log.i(TAG, "Returns " + cursor.getCount() + " rows.");
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Student student = new Student();
                student.setName(cursor.getString(cursor.getColumnIndex(COL_NAME[0])));
                student.setId(cursor.getString(cursor.getColumnIndex(COL_NAME[1])));
                student.setInstitution(cursor.getString(cursor.getColumnIndex(COL_NAME[2])));
                student.setDepartment(cursor.getString(cursor.getColumnIndex(COL_NAME[3])));
                studentList.add(student);
            }
        }
        return studentList;
    }

    public boolean addToFavourite(Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDatabase.COL_FAV_ID, student.getId());
        long insertId = sqLiteDatabase.insert(MyDatabase.FAVOURITE_TABLE_NAME, null, contentValues);
        Log.i(TAG, "Data insert in Favourite Database and id is: " + insertId);
        return (insertId != -1);
    }

    /**
     * @param student delete raw from Favourite Table
     * @return
     */
    public boolean delFromFavTable(Student student) {
        String where = (MyDatabase.COL_FAV_ID + "=" + "\"" + student.getId() + "\"");
        int result = sqLiteDatabase.delete(MyDatabase.FAVOURITE_TABLE_NAME, where, null);
        return (result == 1);
    }

    /**
     * @param id delete raw from Student Table
     * @return
     */
    public boolean delFromStdTable(String id) {
        String where = (MyDatabase.COL_STUDENT_ID + "=" + "\"" + id + "\"");
        int result = sqLiteDatabase.delete(MyDatabase.STUDENT_TABLE_NAME, where, null);
        Log.i(TAG, "Delete raw from Student Table: " + result + "\n" + where);
        return (result == 1);
    }

    public boolean isAvailable(String id) {
        String where = MyDatabase.COL_FAV_ID + "=" + "\"" + id + "\"";
        boolean res = sqLiteDatabase.query(MyDatabase.FAVOURITE_TABLE_NAME, null, where, null, null, null, null).getCount() > 0;
        Log.i(TAG, "Data is Available: " + res);
        return res;
    }

    public boolean updateDB(Student student) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NAME[0], student.getName());
        contentValues.put(COL_NAME[1], student.getId());
        contentValues.put(COL_NAME[2], student.getInstitution());
        contentValues.put(COL_NAME[3], student.getDepartment());
        return sqLiteDatabase.update(MyDatabase.STUDENT_TABLE_NAME, contentValues, COL_NAME[1] + "=" + "\"" + student.getId() + "\"", null) > 0;
    }

    /**
     * @return single student data
     */
    public Student loadSingleDataFromStdTable(String id) {
        Cursor cursor = sqLiteDatabase.query(MyDatabase.STUDENT_TABLE_NAME, COL_NAME, COL_NAME[1] + "=" + "\"" + id + "\"", null, null, null, null);
        List<Student> studentList;
        if (cursor.getCount() > 0) {
            studentList = cursorToList(cursor);
            Log.i(TAG, "Student: " + studentList.get(0).toString());
            return studentList.get(0);
        } else {
            Log.i(TAG, "Student: No data Found.");
            return null;
        }

    }

}
