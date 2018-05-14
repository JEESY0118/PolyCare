package com.project.polycare_f.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.polycare_f.R;
import com.project.polycare_f.activity.MainActivity;
import com.project.polycare_f.data.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class EventDetailsFragment extends Fragment implements OnMapReadyCallback, Button.OnClickListener {
    private TextView titleview, arthorview, categoryview, importanceview, dateview, descriptionview, numberview, stateview;
    public static final String ACTIVITY = "debug here";
    String title, category, description, importance, date, state, phonenumber;
    String arthor;
    int id;
    DBHelper helper;
    SupportMapFragment supportMapFragment;
    String latitude, longtitude;
    GoogleMap mMap;
    View view;
    boolean isCreated = false;
    ArrayList<String> data = new ArrayList<>();
    List<String> mTexts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_item, container, false);

        helper = new DBHelper(getContext());
        titleview = (TextView) view.findViewById(R.id.title_view);
        arthorview = (TextView) view.findViewById(R.id.name_view);
        importanceview = (TextView) view.findViewById(R.id.importance);
        categoryview = (TextView) view.findViewById(R.id.category_view);
        dateview = (TextView) view.findViewById(R.id.date);
        descriptionview = (TextView) view.findViewById(R.id.description_view);
        numberview = (TextView) view.findViewById(R.id.phone_view);
        stateview = (TextView) view.findViewById(R.id.state);

        Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(this);

        Intent intent = getActivity().getIntent();
        arthor = intent.getExtras().getString("Name");
        title = intent.getExtras().getString("Title");
        category = intent.getExtras().getString("Category");
        description = intent.getExtras().getString("Description");
        importance = intent.getExtras().getString("Importance");
        date = intent.getExtras().getString("Date");
        state = intent.getExtras().getString("State");
        phonenumber = intent.getExtras().getString("Phone");
        id = intent.getExtras().getInt("Id");
        latitude = intent.getExtras().getString("Latitude");
        longtitude = intent.getExtras().getString("Longtitude");

        arthorview.setText("Arthor : " + arthor);
        titleview.setText("Titre : " + title);
        categoryview.setText("Catégorie : " + category);
        dateview.setText("Date : " + date);
        descriptionview.setText("Description : " + description);
        stateview.setText("État : " + state);
        numberview.setText("Téléphone : " + phonenumber);

        showImportance(importance);
        createMapView();

        setNewTextAfterChange();

        data.add(arthor);
        data.add(title);
        data.add(category);
        data.add(description);
        data.add(importance);
        data.add(date);
        data.add(state);
        data.add(phonenumber);
        data.add(latitude);
        data.add(longtitude);
        data.add(String.valueOf(id));
        return view;
    }


    private void showImportance(String importance) {
        switch (importance) {
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.modifier:
                ChangeEventFragment changeEventFragment = ChangeEventFragment.newInstance(data);

                changeEventFragment.setResultListener(new ChangeEventFragment.OnChooseListener() {
                    @Override
                    public void onChoosed(List<String> texts) {
                        mTexts = texts;
                        isCreated = true;
                    }
                });

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_event, changeEventFragment, null)
                        .addToBackStack(null).commit();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_call_black_24dp);//设置图标
        builder.setTitle("Validation");//设置对话框的标题
        builder.setMessage("Vous voulez appele " + phonenumber + " ? ");//设置对话框的内容
        builder.setPositiveButton("Appeler", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phonenumber));
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "Annuler", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog b = builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理

    }

    public void delete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
        builder.setTitle("Enlever");//设置对话框的标题
        builder.setMessage("Vous voulez enlever cet évenement ? ");//设置对话框的内容
        builder.setPositiveButton("Enlever", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent1 = new Intent(getContext(), MainActivity.class);
                String sql = "delete from events where event_id= " + "'" + id + "'";
                helper.inertOrUpdateDateBatch(sql);
                startActivity(intent1);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "Annuler", Toast.LENGTH_SHORT).show();
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
            supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.myMap);
            supportMapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void setNewTextAfterChange(){
        if (isCreated) {
            arthor = mTexts.get(0);
            title = mTexts.get(1);
            category = mTexts.get(2);
            description =mTexts.get(3);
            importance = mTexts.get(4);
            date = mTexts.get(5);
            state = mTexts.get(6);
            phonenumber = mTexts.get(7);
            latitude = mTexts.get(8);
            longtitude =mTexts.get(9);

            arthorview.setText("Arthor : " + mTexts.get(0));
            titleview.setText("Titre : " + mTexts.get(1));
            categoryview.setText("Catégorie : " + mTexts.get(2));
            descriptionview.setText("Description : " + mTexts.get(3));
            dateview.setText("Date : " + mTexts.get(5));
            stateview.setText("État : " + mTexts.get(6));
            numberview.setText("Téléphone : " + mTexts.get(7));
            latitude = mTexts.get(8);
            longtitude = mTexts.get(9);
            importance = mTexts.get(4);
            showImportance(importance);
            createMapView();
        }

    }
}
