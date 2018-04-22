package com.example.wangwei.polycare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.wangwei.polycare.R;

public class EventActivity extends AppCompatActivity{
    private TextView titleview;
    public static final String ACTIVITY =  "debug here";

    /**
     * set informations
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_item);


        titleview = (TextView) findViewById(R.id.title_view);
        Log.i(ACTIVITY, "Debug here");

        Intent intent = getIntent();
        String name ="Titre : " +intent.getExtras().getString("Name");
        Log.i(ACTIVITY, name);


        titleview.setText(name);

    }

}
