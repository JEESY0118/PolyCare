package com.example.wangwei.polycare.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.wangwei.polycare.R;

public class AnnouncementActivity extends AppCompatActivity{
    private TextView titleview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_announcement);

        titleview = (TextView) findViewById(R.id.announce);

        String announce = "I will announce an event!";
        titleview.setText(announce);

    }
}
