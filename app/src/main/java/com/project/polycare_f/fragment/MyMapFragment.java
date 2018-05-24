/*package com.example.pc_rayan.polyincidents.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pc_rayan.polyincidents.R;
import com.example.pc_rayan.polyincidents.activities.DetailsActivity;
import com.example.pc_rayan.polyincidents.mishap.Category;
import com.example.pc_rayan.polyincidents.mishap.Importance;
import com.example.pc_rayan.polyincidents.mishap.Mishap;
import com.example.pc_rayan.polyincidents.mishap.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MyMapFragment extends Fragment implements OnMapReadyCallback {


    private List<Mishap> mishaps;
    private static final String ARG_SECTION_NUMBER = "section_number";

    private GoogleMap mMap;

    public static MyMapFragment newInstance(int sectionNumber, List<Mishap> mishaps) {
        MyMapFragment fragment = new MyMapFragment();
        fragment.setMishaps(mishaps);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public void setMishaps(List<Mishap> mishaps) {
        this.mishaps = mishaps;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Mishap mishap = new Mishap("23/04/18","Titre","desc", Place.Learning_Centre, Category.Autre, Importance.Moyenne, new LatLng(44.338580, 1.209014), null);
        Marker marker = mMap.addMarker(new MarkerOptions().position(mishap.getLatLng()).title("Bienvenue à Montcuq"));
        marker.setTag(mishap);
        Mishap mishap1 = new Mishap("2/04/18","Titre","desc", Place.Learning_Centre, Category.Autre, Importance.Moyenne, new LatLng(44.35, 1.2), null);
        Marker marker1 = mMap.addMarker(new MarkerOptions().position(mishap1.getLatLng()).title("Au secours!!!"));
        marker1.setTag(mishap1);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mishap.getLatLng()));
        mMap.setMinZoomPreference(14);
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                Mishap mishap = (Mishap) marker.getTag();
                intent.putExtra("title", mishap.getTitle());
                intent.putExtra("description", mishap.getDescription());
                intent.putExtra("date", mishap.getDate());
                intent.putExtra("category", mishap.getCategory().toString());
                intent.putExtra("place", mishap.getPlace().toString());
                intent.putExtra("importance", mishap.getImportance().toString());
                startActivity(intent);
                }
                });
        //LatLng latLng = new LatLng(Double.parseDouble(), Double.parseDouble());
//        for (Mishap mishap : mishaps) {
//            double latitude = Double.parseDouble(mishap.getLatitude());
//            double longtitude = Double.parseDouble(mishap.getLontitude());
//            LatLng latLng = new LatLng(latitude, longtitude);
//            mMap.addMarker(new MarkerOptions().position(latLng).title(mishap.getTitle()));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//            mMap.setMinZoomPreference(16);
//        }
    }


}
*/