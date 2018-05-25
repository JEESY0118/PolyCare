package com.project.polycare_f.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;

import java.util.List;
import java.util.Vector;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback{
    SupportMapFragment supportMapFragment;
    private static final String TAG = "MapActivity";
    DBHelper helper;
    List<Event> events;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_fragment);
        helper = new DBHelper(this);
        events = helper.getAllEvent("Tout");
        createMapView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.map);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        double latTotal=0;
        double longTotal=0;
        int index=0;
        for (Event e : events) {
            if (e.getLontitude() != null && e.getLatitude() != null&& !e.getLatitude().equals("null") && !e.getLontitude().equals("null") &&
                    !e.getLatitude().equals("") && !e.getLontitude().equals("")) {
                latTotal=latTotal+Double.parseDouble(e.getLatitude());
                longTotal=longTotal+Double.parseDouble(e.getLontitude());
                index=index+1;
                LatLng latLng = new LatLng(Double.parseDouble(e.getLatitude()), Double.parseDouble(e.getLontitude()));
                Marker marker = this.googleMap.addMarker(new MarkerOptions().position(latLng).title(e.getTitle()));
                marker.setTag(e);
                this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(e.getLatitude()), Double.parseDouble(e.getLontitude())), 14.0f));


            }
        }
        latTotal=latTotal/index;
        longTotal=longTotal/index;
        this.googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latTotal,longTotal), 14.0f));
    }


    private void createMapView() {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    private String ifNull(String s){
        if(s==null){
            return "Pas de description de localisation";
        }
        else {
            return s;
        }
    }

}
