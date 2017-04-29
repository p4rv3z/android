package bd.com.parvez.httprequestdata.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by ParveZ on 4/14/2017.
 */

public class NetworkHealper {
    public static boolean checkConnectivity(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return networkInfo.isConnectedOrConnecting() && networkInfo != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}
