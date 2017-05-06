package com.parvez.firebasetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().subscribeToTopic("FCM");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("PARVEZ::::",token+"");
    }

    public void sendData(View view) {
        Intent intent = new Intent(this, SendData.class);
        startActivity(intent);
    }

    public void readData(View view) {
        Intent intent = new Intent(this, ReadData.class);
        startActivity(intent);
    }

    public void listView(View view) {
        Intent intent = new Intent(this, ShowInList.class);
        startActivity(intent);
    }
}
