package com.parvez.firebasetest;
//https://firebase.google.com/support/guides/firebase-android?utm_source=studio
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SendData extends AppCompatActivity implements View.OnClickListener {
    EditText key, value;
    Button btnSendData;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data);
        database = FirebaseDatabase.getInstance();
        key = (EditText) findViewById(R.id.send_data_key);
        value = (EditText) findViewById(R.id.send_data_value);
        btnSendData = (Button) findViewById(R.id.send_data);
        btnSendData.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == btnSendData) {
            String keyString = key.getText().toString();
            String valueString = value.getText().toString();
            if (!keyString.isEmpty() && !valueString.isEmpty()){
                /* for key value add
                databaseReference = database.getReference("message");
                databaseReference.setValue("Hello, World!");
                */
                //anonymous data add
                //databaseReference.push().child(keyString);
                //specific object and data and value add
                databaseReference = database.getInstance()
                        .getReferenceFromUrl("https://fir-test-18d0c.firebaseio.com/users");
                DatabaseReference child = databaseReference.child(keyString);
                child.setValue(valueString);
            }
        }
    }
}
