package com.project.polycare_f.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeclarationActivity extends AppCompatActivity implements OnMapReadyCallback, CompoundButton.OnCheckedChangeListener, GoogleMap.OnMarkerDragListener {
    private static final String ACTIVITY_TAG = "LogDemo";
    private DBHelper helper;
    private Spinner spinner, cateSpinner;
    private String cate, urg;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterCate;
    List<String> categories = new ArrayList<String>();
    List<String> urgences = new ArrayList<String>();
    EditText prenom, title, description, number, location, assignee_name, assignee_number;
    ImageButton contact_choose;
    Switch aSwitch, locationCustomer;
    SupportMapFragment supportMapFragment;
    Location currentLocation;
    String assignee, assignee_phone;
    GoogleMap mMap;
    int numberOfEvents;
    ImageView button;
    Marker marker;
    public static final int RESULT_PICK_CONTACT = 1;

    private static final String TAG = "MapActivity";

    private Boolean getLocationPermision = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.declaration_activity);

        helper = new DBHelper(this);
        createUrgenceSpinner();
        createCategorySpinner();

        aSwitch = findViewById(R.id.mapopener);
        aSwitch.setOnCheckedChangeListener(this);


        final ScrollView mainScrollView = (ScrollView) findViewById(R.id.scroll);
        ImageView transparentImageView = (ImageView) findViewById(R.id.transparent_image);

        transparentImageView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        mainScrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        mainScrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }
        });

        locationCustomer = findViewById(R.id.locationCustomer);
        locationCustomer.setOnCheckedChangeListener(this);
        assignee_name = (EditText) findViewById(R.id.assignee_name_input);
        assignee_number = (EditText) findViewById(R.id.assignee_number_input);

        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                onclick(view);
            }
        });

        contact_choose = (ImageButton) findViewById(R.id.import_contact_button);
        contact_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_contact();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Déclaration d'un incident");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        numberOfEvents = intent.getExtras().getInt("Number");
    }

    private void choose_contact() {
        Intent i = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(i, RESULT_PICK_CONTACT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {

                        Uri uri = data.getData();
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        assignee_phone = cursor.getString(phoneIndex);
                        assignee = cursor.getString(nameIndex);

                        assignee_name.setText(assignee);
                        assignee_number.setText(assignee_phone);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } else {
            Log.e("Failed", "Not able to pick contact");
        }
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
        if (avoidEmptyInfo(getInput())) {
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
                            "event_importance,event_state, event_date,event_number, event_latitude, event_longtitude, event_assignee, event_assignee_number, event_location)\n" +
                            "VALUES('" + strings.get(0) + "','" + cate + "','" + strings.get(1) + "','" + strings.get(4) + "','" +
                            urg + "','" + strings.get(5) + "','" + strings.get(3) + "','" + strings.get(2) +
                            "','" + strings.get(6) + "','" + strings.get(7) + "','" + strings.get(8) + "','" +
                            strings.get(9) + "','" + strings.get(10) + "');";
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

        assignee_name = (EditText) findViewById(R.id.assignee_name_input);
        String assignee = assignee_name.getText().toString();

        assignee_number = (EditText) findViewById(R.id.assignee_number_input);
        String assignee_phone = assignee_number.getText().toString();


        description = (EditText) findViewById(R.id.description_input);
        String descriptions = description.getText().toString();

        location = (EditText) findViewById(R.id.location);
        String pos = location.getText().toString();

        String etat = "A faire";

        strings.add(titre);  //0
        strings.add(descriptions);
        strings.add(phone);
        strings.add(date);
        strings.add(name);//4
        strings.add(etat);

        if (marker != null) {
            if (!marker.isVisible()) {
                strings.add(Double.toString(currentLocation.getLatitude()));
                strings.add(Double.toString(currentLocation.getLongitude()));
            } else {
                strings.add(Double.toString(marker.getPosition().latitude));
                strings.add(Double.toString(marker.getPosition().longitude));
            }
        } else {
            if(currentLocation!=null) {
                strings.add(Double.toString(currentLocation.getLatitude()));
                strings.add(Double.toString(currentLocation.getLongitude()));
            }else {
                strings.add("null");
                strings.add("null");
            }
        }
        strings.add(assignee);
        strings.add(assignee_phone);
        strings.add(pos);

        return strings;
    }

    private boolean avoidEmptyInfo(List<String> strings) {
        if (strings.get(0).equals("")) {
            Log.i(ACTIVITY_TAG, strings.get(0));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
            builder.setTitle("Merci de corriger les informations");//设置对话框的标题
            builder.setMessage("Veuillez entrer un titre");//设置对话框的内容
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            AlertDialog b = builder.create();
            b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
            return false;
        } else if (strings.get(4).equals("")) {
            Log.i(ACTIVITY_TAG, strings.get(4));
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
            builder1.setTitle("Merci de corriger les informations!");//设置对话框的标题
            builder1.setMessage("Veuillez-vous entrer votre nom? ");//设置对话框的内容
            builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            AlertDialog b1 = builder1.create();
            b1.show();  //必须show一下才能看到对话框，跟Toast一样的道理
            return false;
        }
        return true;
    }


    /**
     * @param buttonView the button from wich the method is called
     * @param isChecked  the current button value
     *                   if isChecked is true we need to get the location
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.mapopener) {
            if (isChecked) {
                Log.i(ACTIVITY_TAG, "Open");
                getLocationPermission();
                locationCustomer.setVisibility(View.VISIBLE);

            } else {
                mMap.clear();
                locationCustomer.setVisibility(View.GONE);
                Log.i(ACTIVITY_TAG, "Close");
                locationCustomer.setChecked(false);
                if(marker!=null) {
                    marker.setVisible(false);
                }
            }
        }
        if (buttonView.getId() == R.id.locationCustomer) {
            if (isChecked) {
                getLocationPermission();
                mMap.setOnMarkerDragListener(this);
                marker = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
                marker.setVisible(true);

                marker.setDraggable(true);
            } else {
                marker.setVisible(false);
            }
        }
    }

    /**
     * show map ,
     * OnReadyMap will be called asynchronously
     */
    private void initMap() {
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(this);
    }


    /**
     * @param googleMap the GoogleMap object
     *                  Method called when the map called by initMap has finished instanciation.
     *                  <p>
     *                  check the permisions for getting the location
     *                  set the object values if it is permitted
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
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
            if (getLocationPermision) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() { // quand on a eu la position exacte
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            }
                        } else {
                            Toast.makeText(DeclarationActivity.this, "Impossible de trouver la localisation", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.i(ACTIVITY_TAG, e.getStackTrace().toString());
        }
    }

    /**
     * @param latLng
     * @param zoom
     */
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * va voir dans ContextConpat si on a les permissions pour la localisation
     * si oui lance initMap
     */
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocationPermision = true;
            initMap();
        } else {
            ActivityCompat.requestPermissions(DeclarationActivity.this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * @param requestCode  ?
     * @param permissions  the asked permissions
     * @param grantResults the answers
     *                     <p>
     *                     check if every answer is granteed, if they are , pass getLocationPermission to true.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getLocationPermision = false;
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0) {
            for (Integer i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    getLocationPermision = false;
                    return;
                }
            }
            getLocationPermision = true;
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        marker.getPosition();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        marker.getPosition();

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        marker.getPosition();

    }
}
