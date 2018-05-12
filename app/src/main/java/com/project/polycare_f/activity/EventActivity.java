package com.project.polycare_f.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;
import com.project.polycare_f.gps.GPSTracker;

import java.util.List;
import java.util.Objects;


public class EventActivity extends AppCompatActivity implements OnMapReadyCallback{
    private TextView titleview, arthorview, categoryview, importanceview, dateview, descriptionview, numberview, stateview;
    public static final String ACTIVITY = "debug here";
    String title, category, description, importance, date, state, phonenumber;
    String arthor;
    int id;
    DBHelper helper;
    SupportMapFragment supportMapFragment;
    String latitude, longtitude;
    GoogleMap mMap;

    /**
     * set informations
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.event_item_land);
        } else {
            setContentView(R.layout.event_item);
        }

        helper = new DBHelper(this);
        titleview = (TextView) findViewById(R.id.title_view);
        arthorview = (TextView) findViewById(R.id.name_view);
        importanceview = (TextView) findViewById(R.id.importance);
        categoryview = (TextView) findViewById(R.id.category_view);
        dateview = (TextView) findViewById(R.id.date);
        descriptionview = (TextView) findViewById(R.id.description_view);
        numberview = (TextView) findViewById(R.id.phone_view);
        stateview = (TextView) findViewById(R.id.state);

        Intent intent = getIntent();
        arthor = intent.getExtras().getString("Name");
        title = intent.getExtras().getString("Title");
        category = intent.getExtras().getString("Category");
        description = intent.getExtras().getString("Description");
        importance = intent.getExtras().getString("Importance");
        date = intent.getExtras().getString("Date");
        state = intent.getExtras().getString("State");
        phonenumber = intent.getExtras().getString("Phone");
        id = intent.getExtras().getInt("Id");
        latitude= intent.getExtras().getString("Latitude");
        longtitude= intent.getExtras().getString("Longtitude");


        arthorview.setText("Arthor : " + arthor);
        titleview.setText("Titre : " + title);
        categoryview.setText("Catégorie : " + category);
        dateview.setText("Date : " + date);
        descriptionview.setText("Description : " + description);
        stateview.setText("État : " + state);
        numberview.setText("Téléphone : " + phonenumber);

        showImportance(importance);
        createMapView();


    }

    private void showImportance(String importance){
        switch (importance){
            case "Faible":
                importanceview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_faible_done, 0, 0);
                break;
            case "Moyenne":
                importanceview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_moyenne_done, 0, 0);
                break;
            case "Elevée":
                importanceview.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_star_elevee_done, 0, 0);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modifier:
                Intent intent = new Intent(this, ChangeEventActivity.class);
                intent.putExtra("Title", title);
                intent.putExtra("Name", arthor);
                intent.putExtra("Category", category);
                intent.putExtra("Importance", importance);
                intent.putExtra("Description", description);
                intent.putExtra("State", state);
                intent.putExtra("Phone", phonenumber);
                intent.putExtra("Id", id);
                intent.putExtra("Date", date);
                intent.putExtra("Latitude", latitude);
                intent.putExtra("Longtitude", longtitude);
                this.startActivity(intent);
                break;
            case R.id.rapeler:
                phone();
                break;
            case R.id.delete:
                //DELETE FROM  events WHERE event_id=0
                delete();
                break;

        }
        return true;
    }

    public void phone() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_call_black_24dp);//设置图标
        builder.setTitle("Validation");//设置对话框的标题
        builder.setMessage("Vous voulez appele " + phonenumber + " ? ");//设置对话框的内容
        builder.setPositiveButton("Appeler", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phonenumber));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(EventActivity.this, "Annuler", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog b = builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理

    }

    public void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
        builder.setTitle("Enlever");//设置对话框的标题
        builder.setMessage("Vous voulez enlever cet évenement ? ");//设置对话框的内容
        builder.setPositiveButton("Enlever", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent1 = new Intent(EventActivity.this, MainActivity.class);
                String sql = "delete from events where event_id= " + "'"+id+"'";
                helper.inertOrUpdateDateBatch(sql);
                startActivity(intent1);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(EventActivity.this, "Annuler", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog b = builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
        mMap.addMarker(new MarkerOptions().position(latLng).title("I am Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setMinZoomPreference(16);
    }

    private void createMapView() {
        if (longtitude != null && latitude != null) {
            supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.myMap);
            supportMapFragment.getMapAsync(this);
        }
    }
}
