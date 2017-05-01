package com.parvez.firebasetest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ReadData extends AppCompatActivity {
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView readData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);
        readData = (TextView) findViewById(R.id.read_data);
        database = FirebaseDatabase.getInstance();
        //databaseReference = database.getReference("message");
        databaseReference = database.getInstance()
                .getReferenceFromUrl("https://fir-test-18d0c.firebaseio.com/users");
        // Read from the database
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                HashMap<String,String> hashMap = (HashMap<String,String>)dataSnapshot.getValue();
                String name = hashMap.get("Name");
                String age = hashMap.get("Age");
                readData.setText("Name: "+name+"\nAge: "+age);
                Log.d("Firebase", "Value is: " + hashMap);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.d("Firebase", "Failed to read value.", error.toException());
            }
        });
    }
}
