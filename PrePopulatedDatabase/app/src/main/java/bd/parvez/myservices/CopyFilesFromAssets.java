package bd.parvez.myservices;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by ParveZ on 23-Nov-15.
 */
public class CopyFilesFromAssets {
    private static final String TAG = "CopyFilesFromAssets";

    /**
     * @param path
     * @return boolean
     */
    public static boolean checkPath(String path) {
        File file = new File(path);
        if (file.exists()) {
            MyTag.myTag(TAG, "Database already exists");
            return true;
        } else {
            MyTag.myTag(TAG, "Database does not exists");
            return false;
        }
    }

    /**
     * @param context
     * @param fileName
     * @param toPath
     */
    public static void copyFromAssetToDatabase(Context context, String fileName, String toPath) {
        MyTag.myTag(TAG, "Database coping....");
        try {
            InputStream dbInputStream = context.getAssets().open(fileName);
            OutputStream dbOutputStream = new FileOutputStream(toPath);
            byte[] buffer = new byte[4096];
            int readCount = 0;
            while ((readCount = dbInputStream.read(buffer)) > 0) {
                dbOutputStream.write(buffer, 0, readCount);
            }

            dbInputStream.close();
            dbOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            MyTag.myTag(TAG, "Database copy failed.");
        }
    }
}
