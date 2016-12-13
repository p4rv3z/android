package bd.com.parvez.services;

import android.util.Log;

/**
 * Created by ParveZ on 07-May-16.
 */
public class MyTag {
    public static final String TAG = "CSS";

    /**
     *
     * @param className
     * @param msg
     */
    public static void Tag(String className, String msg) {
        Log.i(TAG, className + " " + msg);
        Log.i(TAG, "-------------------");
    }
}
