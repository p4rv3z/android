package bd.parvez.savedatainfiles;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ParveZ on 14-Nov-15.
 */
public class MyFiles {
    private static File externalPath;
    private static FileOutputStream fos;
    private static FileInputStream fis;
    private static BufferedInputStream bis;
    private static StringBuffer sb;
    private static final String TAG = "FilePath: ";

    /**
     * Save data to Internal Storage
     * Path: data/data/bd.parvez.savedatainfiles/files/
     *
     * @param key     File name
     * @param x       your data
     * @param context passing your current activity or context
     */
    public static void saveFileInternalStorage(String key, String x, Context context) throws IOException {
        fos = context.openFileOutput(key, context.MODE_PRIVATE);
        fos.write(x.getBytes());
        fos.close();
        Log.i(TAG, context.getFilesDir().getAbsolutePath());
    }

    /**
     * Load data from Internal Storage
     *
     * @param key     File name
     * @param context passing your current activity or context
     * @return String value
     */
    public static String loadFileInternalStorage(String key, Context context) throws IOException {
        fis = context.openFileInput(key);
        bis = new BufferedInputStream(fis);
        Log.i(TAG, context.getFilesDir().getAbsolutePath());
        return readData(bis);
    }

    /**
     * Add permission in Manifest
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
     *
     * @param key     file name
     * @param x       your value
     * @param context passing your current activity or context
     */
    public static void saveFileExternalStorage(String key, String x, Context context) throws IOException {
        if (checkPath()) {

            fos = new FileOutputStream(filePath(context, key));
            fos.write(x.getBytes());
            fos.close();

            Log.i(TAG, context.getExternalFilesDir(null).getAbsolutePath());
        } else {
            return;
        }
    }

    /**
     * @param context
     * @param key
     * @return absolute path
     */
    private static File filePath(Context context, String key) {
        externalPath = context.getExternalFilesDir(null);///path where file save its returns root directory
        String path = externalPath.getAbsolutePath();
        return new File(path, key);
    }

    /**
     * @return check external path is available or not
     */
    private static boolean checkPath() {
        String state = Environment.getExternalStorageState();
        String sd = Environment.MEDIA_MOUNTED;
        if (state.equals(sd)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param key     file name
     * @param context passing your current activity or context
     * @return String value
     */
    public static String loadFileExternalStorage(String key, Context context) throws IOException {
        fis = new FileInputStream(filePath(context, key));
        bis = new BufferedInputStream(fis);
        Log.i(TAG, context.getExternalFilesDir(null).getAbsolutePath());
        return readData(bis);
    }

    /**
     * Read data from external storage path
     *
     * @param bis BufferedInputStream
     * @return String value
     */
    private static String readData(BufferedInputStream bis) throws IOException {
        sb = new StringBuffer();
        while (bis.available() != 0) {
            char c = (char) bis.read();
            sb.append(c);
        }
        bis.close();
        ;
        fis.close();
        return sb.toString();
    }
}