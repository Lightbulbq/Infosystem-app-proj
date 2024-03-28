package com.example.campuscourier;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RequestDetails extends AppCompatActivity {

    Requests r;
    TextView itemName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        itemName = findViewById(R.id.itemName);

        if (getIntent().hasExtra(MainActivity.NEXT_SCREEN)) {
            // get the Serializable data model class with the details in it
            r = (Requests) getIntent().getSerializableExtra(MainActivity.NEXT_SCREEN);
        }
        // it the emplist is not null the it has some data and display it
        if (r != null) {
            itemName.setText(r.getItem());  // displaying name
        }
    }
}