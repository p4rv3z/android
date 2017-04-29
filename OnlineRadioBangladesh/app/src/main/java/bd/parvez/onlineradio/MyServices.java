package bd.parvez.onlineradio;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Environment;

/**
 * Created by Parvez on 6/7/2015.
 */
public class MyServices extends Activity {
    private static SharedPreferences sp;
    private static SharedPreferences.Editor spEditor;

    public static boolean checkConnectivity(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting()
                || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getPath(Context context) {
        sp = context.getSharedPreferences("position", context.MODE_PRIVATE);
        return sp.getString("path", Environment.getExternalStorageDirectory().getPath().toString());
    }

    public static void setPath(Context context, String path) {
        sp = context.getSharedPreferences("position", context.MODE_PRIVATE);
        spEditor = sp.edit();
        spEditor.putString("path", path);
        spEditor.commit();
    }
}
