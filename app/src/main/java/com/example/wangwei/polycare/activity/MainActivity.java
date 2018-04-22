package com.example.wangwei.polycare.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.wangwei.polycare.R;
import com.example.wangwei.polycare.adapter.RecyclerViewAdapter;
import com.example.wangwei.polycare.data.Event;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Event> events;
    public static final String ACTIVITY =  "debug here";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_declaration:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AnnouncementActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_graphe:
                    return true;
                case R.id.navigation_location:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        events = new ArrayList<>();

        events.add(new Event("1"));
        events.add(new Event("2"));
        events.add(new Event("3"));
        events.add(new Event("4"));
        events.add(new Event("5"));
        events.add(new Event("6"));
        events.add(new Event("7"));
        events.add(new Event("8"));
        events.add(new Event("9"));

        Log.i(ACTIVITY, "Debug here");

        RecyclerView myview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, events);
        myview.setLayoutManager(new GridLayoutManager(this, 1));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        myview.setAdapter(adapter);
    }

}
