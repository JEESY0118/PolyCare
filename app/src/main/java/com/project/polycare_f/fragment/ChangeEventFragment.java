package com.project.polycare_f.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.project.polycare_f.R;
import com.project.polycare_f.activity.EventActivity;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ChangeEventFragment extends Fragment implements OnMapReadyCallback,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private String mArgument;
    public static final String ARGUMENT = "argument";
    private View view;
    public static final String ACTIVITY = "debug here";
    List<String> urgences = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> states = new ArrayList<>();
    private ArrayAdapter<String> adapterUr, adapterCate, adapterSta;
    EditText prenom, title, description, number, mLocation, assignee_view, assignee_number_view;
    ImageButton contact_choose;
    Spinner urg, cate, state;
    String arthor, titre, des, phone, importance, etat, category, date, latitude, longtitude, location, assignee, assignee_number;
    int id;
    DBHelper helper;
    SupportMapFragment supportMapFragment;
    Location currentLocation;
    GoogleMap mMap;
    boolean isLocated = false;
    LatLng latLng;
    private Switch aSwitch;
    private OnChooseListener mlistener;
    ArrayList<String> texts = new ArrayList<>();
    Activity mActivity;
    static  final int RESULT_PICK_CONTACT = 1;

    private static final String TAG = "MapActivity";
    private boolean isValided = false;

    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.change_event_item, container, false);


        helper = new DBHelper(getContext());
        aSwitch = (Switch) view.findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(this);

        prenom = (EditText) view.findViewById(R.id.prenom_input);
        title = (EditText) view.findViewById(R.id.title_input);
        description = (EditText) view.findViewById(R.id.description_input);
        number = (EditText) view.findViewById(R.id.phone_input);
        mLocation = (EditText) view.findViewById(R.id.location);
        assignee_view = (EditText) view.findViewById(R.id.assignee_name_input);
        assignee_number_view = (EditText) view.findViewById(R.id.assignee_phone_input);

        contact_choose = (ImageButton) view.findViewById(R.id.import_contact_button);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.modifie);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ImageView button = (ImageView) view.findViewById(R.id.button);
        button.setOnClickListener(this);

        if (getArguments() != null) {
            texts = getArguments().getStringArrayList("data");
            prenom.setText(texts.get(0));
            title.setText(texts.get(1));
            category = texts.get(2);
            description.setText(texts.get(3));
            importance = texts.get(4);
            date = texts.get(5);
            etat = texts.get(6);
            number.setText(texts.get(7));
            latitude = texts.get(8);
            longtitude = texts.get(9);
            id = Integer.parseInt(texts.get(12));
            mLocation.setText(texts.get(13));
            assignee_view.setText(texts.get(10));
            assignee_number_view.setText(texts.get(11));
        }

        createCategorySpinner();
        createUrgenceSpinner();
        createStateSpinner();

        contact_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choose_contact();
            }
        });

        return view;
    }

    private void choose_contact() {
        Intent i=new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        i.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(i,RESULT_PICK_CONTACT);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    Cursor cursor = null;
                    try {

                        Uri uri = data.getData();
                        cursor = getContext().getContentResolver().query(uri, null, null, null, null);
                        cursor.moveToFirst();
                        int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                        assignee_number = cursor.getString(phoneIndex);
                        assignee = cursor.getString(nameIndex);

                        assignee_view.setText(assignee);
                        assignee_number_view.setText(assignee_number);
                        Log.e("Name and Contact :",assignee+","+assignee_number);

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
        setUrgences();
        urg = (Spinner) view.findViewById(R.id.Spinner_urgence);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterUr = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, urgences);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterUr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        urg.setAdapter(adapterUr);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        urg.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                importance = urg.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void createCategorySpinner() {

        setCategories();
        cate = (Spinner) view.findViewById(R.id.Spinner_category);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterCate = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        cate.setAdapter(adapterCate);
        //：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        cate.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                category = cate.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void createStateSpinner() {
        setEtat();
        state = (Spinner) view.findViewById(R.id.Spinner_state);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterSta = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, states);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterSta.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        state.setAdapter(adapterSta);
        //：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        state.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                etat = state.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void onclick() {
        if (avoidEmptyInfo(getInput())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
            builder.setTitle("Validation de la modification");//设置对话框的标题
            builder.setMessage("Vous voulez applique la modification？");//设置对话框的内容
            builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    List<String> strings = getInput();
                    String sql =
                            "UPDATE events set event_title= " + "'" + strings.get(1) + "'," +
                                    " event_category = " + "'" + category + "'," +
                                    " event_description= " + "'" + strings.get(3) + "'," +
                                    " event_reporter= " + "'" + strings.get(0) + "'," +
                                    " event_importance= " + "'" + importance + "'," +
                                    " event_state= " + "'" + etat + "'," +
                                    " event_number= " + "'" + strings.get(7) + "'," +
                                    " event_latitude= " + "'" + strings.get(8) + "'," +
                                    " event_longtitude= " + "'" + strings.get(9) + "'," +
                                    " event_location= " + "'" + strings.get(11) + "'," +
                                    " event_assignee= " + "'" + strings.get(12)+ "'," +
                                    " event_assignee_number= " + "'" + strings.get(13)+ "'" +
                                    " where event_id = " + "'" + id + "'";
                    helper.inertOrUpdateDateBatch(sql);
                    mlistener.onChoosed(strings);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            });
            builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            AlertDialog b = builder.create();
            b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
        }
    }

    private void setCategories() {
        List<String> cates = new ArrayList<>();
        cates.add("Matériel");
        cates.add("Organisation");
        cates.add("Parking");
        cates.add("Technique");
        cates.add("Autre");

        categories.add(category);
        for (String s : cates) {
            if (!s.equals(category)) {
                categories.add(s);
            }
        }
    }

    private void setUrgences() {
        List<String> urs = new ArrayList<>();
        urs.add("Faible");
        urs.add("Moyenne");
        urs.add("Elevée");

        urgences.add(importance);
        for (String s : urs) {
            if (!s.equals(importance)) {
                urgences.add(s);
            }
        }
    }

    private void setEtat() {
        List<String> etats = new ArrayList<>();
        etats.add("A faire");
        etats.add("En cours");
        etats.add("Résolu");

        states.add(etat);
        for (String s : etats) {
            if (!s.equals(etat)) {
                states.add(s);
            }
        }
    }

    public List<String> getInput() {
        List<String> strings = new ArrayList<>();

        prenom = (EditText) view.findViewById(R.id.prenom_input);
        String name = prenom.getText().toString();

        title = (EditText) view.findViewById(R.id.title_input);
        String titre = title.getText().toString();

        number = (EditText) view.findViewById(R.id.phone_input);
        String phone = number.getText().toString();

        description = (EditText) view.findViewById(R.id.description_input);
        String descriptions = description.getText().toString();

        mLocation = (EditText) view.findViewById(R.id.location);
        String location = mLocation.getText().toString();

        assignee_view = (EditText) view.findViewById(R.id.assignee_name_input);
        assignee = assignee_view.getText().toString();

        assignee_number_view = (EditText) view.findViewById(R.id.assignee_phone_input);
        assignee_number = assignee_number_view.getText().toString();

        strings.add(name);
        strings.add(titre);
        strings.add(category);
        strings.add((descriptions));
        strings.add(importance);
        strings.add(date);
        strings.add(etat);
        strings.add((phone));
        if (currentLocation != null) {
            strings.add((Double.toString(currentLocation.getLatitude())));
            strings.add((Double.toString(currentLocation.getLongitude())));
            strings.add(String.valueOf(id));
            strings.add((location));
        } else {
            strings.add((latitude));
            strings.add((longtitude));
            strings.add(String.valueOf(id));
            strings.add((location));
        }
        strings.add((assignee));
        strings.add((assignee_number));

        Log.d("ChangeEvent", " "+strings);
        return strings;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switch1:
                if (isChecked) {
                    getLocationPermission();
                    isLocated = true;
                } else {
                    mMap.clear();
                    isLocated = false;
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(getContext(), "Map est prête", Toast.LENGTH_SHORT).show();
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            currentLocation = (Location) task.getResult();

                            if (currentLocation != null) {
                                moveCamera(new LatLng(currentLocation.getAltitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }

                        } else {
                            Toast.makeText(getContext(), "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void initMap() {
        supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.myMap);
        supportMapFragment.getMapAsync(this);
    }

    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(getContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                onclick();
                break;
        }
    }

    public static ChangeEventFragment newInstance(ArrayList<String> texts) {
        ChangeEventFragment changeEventFragment = new ChangeEventFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", texts);
        changeEventFragment.setArguments(bundle);
        return changeEventFragment;
    }


    public interface OnChooseListener {
        void onChoosed(List<String> texts);
    }

    public void setResultListener(OnChooseListener listener) {
        mlistener = listener;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }


    private boolean avoidEmptyInfo(List<String> strings) {
        if (strings.get(1).equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
            builder.setTitle("Merci de corriger les informations!");//设置对话框的标题
            builder.setMessage("Veuillez-vous entrer un titre? ");//设置对话框的内容
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            AlertDialog b = builder.create();
            b.show();  //必须show一下才能看到对话框，跟Toast一样的道理
            return false;
        } else if (strings.get(0).equals("")) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
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


}
