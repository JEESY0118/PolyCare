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
        for (Event e : events) {
            if (e.getLontitude() != null && e.getLatitude() != null) {
                LatLng latLng = new LatLng(Double.parseDouble(e.getLatitude()), Double.parseDouble(e.getLontitude()));
                Marker marker = this.googleMap.addMarker(new MarkerOptions().position(latLng).title(e.getTitle()));
                marker.setTag(e);
                this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                this.googleMap.setMinZoomPreference(16);
            }
        }
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
