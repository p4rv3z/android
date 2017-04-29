package bd.com.parvez.intentservicesexample;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * Created by ParveZ on 4/13/2017.
 */

public class MyIntentServices extends IntentService {
    private static final String TAG = "MyIntentServices :: ";
    public static final String My_SERVICES_MSG = "myservicesmsg";
    public static final String My_SERVICES_PAYLOAD = "myservicespayload";
    public MyIntentServices() {
        super("MyIntentServices");
        Log.d(TAG,"MyIntentServices");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG,"onHandleIntent");
        Uri uri = intent.getData();
        Log.d(TAG,uri.toString());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intentMSG = new Intent(My_SERVICES_MSG);
        intentMSG.putExtra(My_SERVICES_PAYLOAD,"THIS IS FROM INTENT SERVICES");
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        localBroadcastManager.sendBroadcast(intentMSG);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.d(TAG,"setIntentRedelivery");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"onStart");
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand");
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return super.onBind(intent);
    }
}
