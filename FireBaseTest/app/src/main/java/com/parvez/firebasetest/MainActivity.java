package com.parvez.firebasetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
