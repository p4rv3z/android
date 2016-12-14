package bd.parvez.myservices;

import android.util.Log;

/**
 * Created by ParveZ on 23-Nov-15.
 */
public class MyTag {
    public static final String TAG = "Pre Populated Database";

    /**
     *
     * @param className
     * @param msg
     */
    public static void myTag(String className, String msg) {
        Log.i(TAG, className + " " + msg);
        Log.i(TAG, "-------------------");
    }
}
