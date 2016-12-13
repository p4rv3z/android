package services;

import android.util.Log;

/**
 * Created by ParveZ on 12/11/2016.
 */

public class MyServices {
    public static final String FORWARD_CMD = "3";
    public static final String BACKWARD_CMD = "4";
    public static final String RIGHT_CMD = "6";
    public static final String LEFT_CMD = "5";
    public static final String AUTO_CMD = "9";
    public static final String STOP_CMD = "8";
    public static final String LIGHT_ON_CMD = "1";
    public static final String LIGHT_OFF_CMD = "2";
    public static final String NIGHT_MODE_CMD = "N";
    public static final String PLAY_CMD = "P";
    public static final String TEMPERATURE_CMD = "T";


    private static final String TAG = "MyLog";


    public static void log(String msg){
        Log.d(TAG,"::"+msg);
    }
}
