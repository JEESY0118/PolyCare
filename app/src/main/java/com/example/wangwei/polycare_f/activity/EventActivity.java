package com.example.wangwei.polycare_f.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wangwei.polycare_f.R;


public class EventActivity extends AppCompatActivity {
    private TextView titleview, arthorview, categoryview, importanceview, dateview, descriptionview, numberview, stateview;
    public static final String ACTIVITY = "debug here";
    String prenom, title, category, description, importance, date, state, phonenumber;

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
        prenom = intent.getExtras().getString("Name");
        title = intent.getExtras().getString("Title");
        category = intent.getExtras().getString("Category");
        description = intent.getExtras().getString("Description");
        importance = intent.getExtras().getString("Importance");
        date = intent.getExtras().getString("Date");
        state =intent.getExtras().getString("State");
        phonenumber = intent.getExtras().getString("Phone");


        arthorview.setText("Arthor : " + prenom);
        titleview.setText("Titre : " + title);
        importanceview.setText("Urgence : " +importance);
        categoryview.setText("Catégorie : " + category);
        dateview.setText("Date : " + date);
        descriptionview.setText("Description : " + description);
        stateview.setText( "État : " + state);
        numberview.setText("Téléphone : " + phonenumber);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.modifier:
                Intent intent = new Intent(this, ChangeEventActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("Name", prenom);
                intent.putExtra("Category", category);
                intent.putExtra("Importance", importance);
                intent.putExtra("Description",description);
                intent.putExtra("State", state);
                intent.putExtra("Phone", phonenumber);

                this.startActivity(intent);
                break;
            case R.id.rapeler:
                break;
        }
        return true;
    }

}
