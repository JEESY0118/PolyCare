package com.project.polycare_f.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class MapFragment extends Fragment implements OnMapReadyCallback{
    private View view;
    SupportMapFragment supportMapFragment;
    private static final String TAG = "MapActivity";
    DBHelper helper;
    List<Event> events;

    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);

        helper = new DBHelper(getContext());
        events = helper.getAllEvent("Tout");
        createMapView();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.map);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        // Setting a custom info window adapter for the google map
        return view;
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
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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