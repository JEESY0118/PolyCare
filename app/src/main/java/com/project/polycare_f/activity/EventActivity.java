package com.project.polycare_f.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.wangwei.polycare_f.R;


public class EventActivity extends AppCompatActivity {
    private TextView titleview, arthorview, categoryview, importanceview, dateview, descriptionview, numberview, stateview;
    public static final String ACTIVITY = "debug here";

    /**
     * set informations
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.event_item_land);
        }
        else {
            setContentView(R.layout.event_item);
        }


        titleview = (TextView) findViewById(R.id.title_view);
        arthorview = (TextView) findViewById(R.id.name_view);
        importanceview = (TextView) findViewById(R.id.importance_view);
        categoryview = (TextView) findViewById(R.id.category_view);
        dateview = (TextView) findViewById(R.id.date_view);
        descriptionview = (TextView) findViewById(R.id.description_view);
        numberview = (TextView) findViewById(R.id.phone_view);
        stateview = (TextView) findViewById(R.id.state_view);


        Intent intent = getIntent();
        String arthor = "Arthor : " + intent.getExtras().getString("Name");
        String title = "Titre : " + intent.getExtras().getString("Title");
        String category = "Catégorie : " + intent.getExtras().getString("Category");
        String description = "Description : " + intent.getExtras().getString("Description");
        String importance = "Urgence : " + intent.getExtras().getString("Importance");
        String date = "Date : " + intent.getExtras().getString("Date");
        String state = "État : " + intent.getExtras().getString("State");
        String phonenumber = "Téléphone : " + intent.getExtras().getString("Phone");


        titleview.setText(title);
        arthorview.setText(arthor);
        importanceview.setText(importance);
        categoryview.setText(category);
        dateview.setText(date);
        descriptionview.setText(description);
        stateview.setText(state);
        numberview.setText(phonenumber);

    }

}
