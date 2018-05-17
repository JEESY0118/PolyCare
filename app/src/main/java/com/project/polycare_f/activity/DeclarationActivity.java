package com.project.polycare_f.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;
import com.project.polycare_f.gps.GPSTracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeclarationActivity  extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener{
    private static final String ACTIVITY_TAG = "LogDemo";
    private DBHelper helper;
    private Spinner spinner,cateSpinner;
    private String cate, urg;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterCate;
    List<String> categories = new ArrayList<String>();
    List<String> urgences = new ArrayList<String>();
    EditText prenom, title,description, number;
    private Switch aSwitch;
    SupportMapFragment supportMapFragment;
    private GPSTracker gpsTracker;
    Location currentLocation;
    double latitude, longtitude;
    GoogleMap mMap;
    int numberOfEvents;

    private static final String TAG = "MapActivity";

    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.declaration_activity_land);
        }

        else {
            setContentView(R.layout.declaration_activity);
        }

        helper = new DBHelper(this);
        createUrgenceSpinner();
        createCategorySpinner();
        aSwitch = (Switch) findViewById(R.id.mapopener);
        aSwitch.setOnCheckedChangeListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        numberOfEvents = intent.getExtras().getInt("Number");
    }

    private void createUrgenceSpinner() {
        urgences.addAll(helper.getUrgences());

        spinner = (Spinner) findViewById(R.id.Spinner_urgence);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, urgences);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinner.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                urg = spinner.getSelectedItem().toString();
                Log.i(ACTIVITY_TAG, urg);
            }
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void createCategorySpinner() {
        categories.addAll(helper.getCategories());

        cateSpinner = (Spinner) findViewById(R.id.Spinner_category);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterCate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        cateSpinner.setAdapter(adapterCate);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        cateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cate = cateSpinner.getSelectedItem().toString();
                Log.i(ACTIVITY_TAG, cate);
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void onclick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
        builder.setTitle("Validation de déclaration");//设置对话框的标题
        builder.setMessage("Vous voulez valider？");//设置对话框的内容
        builder.setPositiveButton("Valider", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                List<String> strings = getInput();
                String sql = "\n" +
                        "INSERT INTO events (event_title, event_category, event_description,event_reporter ," +
                        "event_importance,event_state, event_date,event_number, event_latitude, event_longtitude )\n" +
                        "VALUES('"+strings.get(0) + "','" + cate + "','"+ strings.get(1) +"','" + strings.get(4) + "','" +
                        urg + "','" + strings.get(5) + "','" + strings.get(3) + "','" + strings.get(2) +
                        "','"+strings.get(6)+"','"+strings.get(7)+"');";
                Log.i(ACTIVITY_TAG, sql);
                helper.inertOrUpdateDateBatch(sql);
                Toast.makeText(DeclarationActivity.this, "Réussi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(DeclarationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(DeclarationActivity.this, "Annuler", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog b = builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理

    }

    public List<String> getInput() {
        List<String> strings = new ArrayList<>();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String date = myFmt.format(now);
        Log.i(ACTIVITY_TAG, date);

        prenom = (EditText) findViewById(R.id.prenom_input);
        String name = prenom.getText().toString();

        title = (EditText) findViewById(R.id.title_input);
        String titre = title.getText().toString();

        number = (EditText) findViewById(R.id.phone_input);
        String phone = number.getText().toString();


        description = (EditText) findViewById(R.id.description_input);
        String descriptions = description.getText().toString();

        String etat = "A faire";

        strings.add(titre);
        strings.add(descriptions);
        strings.add(phone);
        strings.add(date);
        strings.add(name);
        strings.add(etat);
        strings.add(Double.toString(latitude));
        strings.add(Double.toString(longtitude));

        return strings;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.mapopener:
                if(isChecked){
                    Log.i(ACTIVITY_TAG, "Open");
                    getLocationPermission();
                }
                else {
                    mMap.clear();
                    Log.i(ACTIVITY_TAG, "Close");
                }
                break;
        }
    }

    /**
     * if we have permission, we get the location of the device
     * @param googleMap
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is prête", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    /**
     * get location of device
     */
    private void getDeviceLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DeclarationActivity.this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();

                            if(currentLocation!=null) {
                                latitude = currentLocation.getLatitude();
                                longtitude = currentLocation.getLongitude();
                                moveCamera(new LatLng(latitude,longtitude),
                                        DEFAULT_ZOOM);
                            }

                        } else {
                            Toast.makeText(DeclarationActivity.this, "impossible de to trouver la localisation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
        }
    }

    /**
     *
     * @param latLng
     * @param zoom
     */
    private void moveCamera(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * show map
     */
    private void initMap(){
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }

    /**
     * get permission
     */
    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this,
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(DeclarationActivity.this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(DeclarationActivity.this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    /**
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

}
