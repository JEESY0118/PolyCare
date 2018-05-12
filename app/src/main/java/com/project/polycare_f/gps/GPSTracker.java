package com.project.polycare_f.gps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;

import java.util.ArrayList;
import java.util.List;

public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    boolean isGPSEnabled = false;
    boolean isNetWrokEnabled = false;
    boolean canGetLocation = false;

    Location location;
    protected LocationManager locationManager;

    public GPSTracker(Context context) {
        this.context = context;
    }

    //create getLocation
    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(locationManager.GPS_PROVIDER);
            isNetWrokEnabled = locationManager.isProviderEnabled(locationManager.NETWORK_PROVIDER);

            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5*60*1000, 1, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }

                //if location is not found
                if (location == null) {
                    if (isNetWrokEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }

            }

        } catch (Exception e) {
        }
        return location;
    }

    public void onLocationChanged(Location location) {

    }

    public void onStatusChanged(String provider, int status, Bundle bundle) {

    }


    public void onProviderEnabled(String s) {

    }

    public void onProviderDisabled(String s) {

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

