package com.project.polycare_f.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.project.polycare_f.R;
import com.project.polycare_f.fragment.EventDetailsFragment;

public class EventActivity extends AppCompatActivity{
    FragmentManager fragmentManager;
    EventDetailsFragment eventDetailsFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.event_activity_land);
        } else {
            setContentView(R.layout.event_activity);
        }

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        eventDetailsFragment = new EventDetailsFragment();
        transaction.add(R.id.fragment_event,eventDetailsFragment);
        transaction.commit();
    }


}
