package com.project.polycare_f.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.polycare_f.R;
import com.project.polycare_f.gps.GPSTracker;


public class MapFragment extends Fragment implements OnMapReadyCallback{
    private View view;
    SupportMapFragment supportMapFragment;
    private GoogleMap mMap;
    private Location mLastLocation;
    GPSTracker gpsTracker;
    double latitude, longtitude;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.map_fragment, container, false);

        gpsTracker = new GPSTracker(getContext());
        mLastLocation = gpsTracker.getLocation();

        latitude = mLastLocation.getLatitude();
        longtitude = mLastLocation.getLatitude();


        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng latLng = new LatLng(latitude, longtitude);
        mMap.addMarker(new MarkerOptions().position(latLng).title("I am Here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}