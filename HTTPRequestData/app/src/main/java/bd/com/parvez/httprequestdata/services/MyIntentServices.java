package bd.com.parvez.httprequestdata.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import bd.com.parvez.httprequestdata.model.Item;
import bd.com.parvez.httprequestdata.utils.HttpHelper;

/**
 * Created by ParveZ on 4/13/2017.
 */

public class MyIntentServices extends IntentService {
    private static final String TAG = "MyIntentServices :: ";
    public static final String My_SERVICES_MSG = "myservicesmsg";
    public static final String My_SERVICES_PAYLOAD = "myservicespayload";
    public static final String My_SERVICES_EXCEPTION = "myservicesexception";

    public MyIntentServices() {
        super("MyIntentServices");
        Log.d(TAG, "MyIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");
        Uri uri = intent.getData();
        Log.d(TAG, uri.toString());
        String response;
        try {
            //response = HttpHelper.downloadUrl(uri.toString());// for normal
            response = HttpHelper.downloadUrl(uri.toString(), "parvez", "p4rv3z");//for authorization
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        //Gson gson = new Gson();
        //Item[] items = gson.fromJson(response, Item[].class);

        if (response != null) {
            Item[] items = XMLParser.parseFeed(response);
            Intent intentMSG = new Intent(My_SERVICES_MSG);
            intentMSG.putExtra(My_SERVICES_PAYLOAD, items);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            localBroadcastManager.sendBroadcast(intentMSG);
        } else {
            Intent intentMSG1 = new Intent(My_SERVICES_MSG);
            intentMSG1.putExtra(My_SERVICES_EXCEPTION, "Authorization Failed.");
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
            localBroadcastManager.sendBroadcast(intentMSG1);
        }

    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.d(TAG, "setIntentRedelivery");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "onStart");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        return super.onBind(intent);
    }
}
