package com.example.wangwei.polycare.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.wangwei.polycare.R;
import com.example.wangwei.polycare.adapter.RecyclerViewAdapter;
import com.example.wangwei.polycare.data.Event;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Event> events;
    public static final String ACTIVITY =  "debug here";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        events = new ArrayList<>();

        events.add(new Event("1"));
        events.add(new Event("2"));
        events.add(new Event("3"));
        events.add(new Event("4"));
        events.add(new Event("5"));
        events.add(new Event("6"));

        Log.i(ACTIVITY, "Debug here");

        RecyclerView myview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, events);
        myview.setLayoutManager(new GridLayoutManager(this, 1));
        myview.setAdapter(adapter);
    }

}
