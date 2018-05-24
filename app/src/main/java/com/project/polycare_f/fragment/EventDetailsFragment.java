package com.project.polycare_f.fragment;


import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EventDetailsFragment extends Fragment implements OnMapReadyCallback, Button.OnClickListener {
    private TextView titleview, arthorview, categoryview, importanceview, dateview, descriptionview, numberview, stateview,locationview, assignee_view, assignee_number_view;
    public static final String ACTIVITY = "debug here";
    String title, category, description, importance, date, state, author_phoneNumber, assignee_phoneNumber, location, assignee, assignee_number;
    String arthor;
    int id;
    DBHelper helper;
    SupportMapFragment supportMapFragment;
    String latitude, longtitude;
    GoogleMap mMap;
    View view;
    ImageButton call_author, call_assignee;
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
        assignee_view = (TextView) view.findViewById(R.id.assignee_view);
        assignee_number_view = (TextView) view.findViewById(R.id.assignee_phone_view);
        locationview = (TextView) view.findViewById(R.id.location);
        call_author = (ImageButton) view.findViewById(R.id.call_author) ;
        call_assignee = (ImageButton) view.findViewById(R.id.call_assignee);


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.details);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

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
        author_phoneNumber = intent.getExtras().getString("Phone");
        id = intent.getExtras().getInt("Id");
        latitude= intent.getExtras().getString("Latitude");
        longtitude= intent.getExtras().getString("Longtitude");
        assignee = intent.getExtras().getString("Assignee");
        assignee_number = intent.getExtras().getString("Assignee_Number");
        location = intent.getExtras().getString("Location");

        arthorview.setText(" Arthor : " + ifNull(arthor));
        titleview.setText(" Titre : " + ifNull(title));
        categoryview.setText(" Catégorie : " + ifNull(category));
        dateview.setText(" Date : " + ifNull(date));
        descriptionview.setText(" Description : " + ifNull(description));
        stateview.setText(" État : " + ifNull(state));
        numberview.setText(" Téléphone : " + ifNull(author_phoneNumber));
        locationview.setText(" Localisation : "+ ifNull(location));
        assignee_view.setText(" Destinataire : " + ifNull(assignee));
        assignee_number_view.setText(" Téléphone du destinataire : "+ifNull(assignee_number));

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
        data.add(author_phoneNumber);
        data.add(latitude);
        data.add(longtitude);
        data.add(assignee);
        data.add(assignee_number);
        data.add(String.valueOf(id));
        data.add(location);

        arthorview.setText("Auteur : " + arthor);
        titleview.setText("Titre : " + title);
        categoryview.setText("Catégorie : " + category);
        dateview.setText("Date : " + date);
        descriptionview.setText("Description : " + description);
        stateview.setText("État : " + state);
        numberview.setText("Téléphone du: " + author_phoneNumber);
        assignee_view.setText("Destinataire : "+assignee);
        assignee_number_view.setText("Numéro du destinataire : "+assignee_number);


        showImportance(importance);
        createMapView();

        call_assignee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone(assignee_number);
            }
        });

        call_author.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone(author_phoneNumber);
            }
        });

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
                        arthorview.setText("Author : " + texts.get(0));
                        titleview.setText("Titre : " + texts.get(1));
                        categoryview.setText("Catégorie : " + texts.get(2));
                        descriptionview.setText("Description : " + texts.get(3));
                        dateview.setText("Date : " + texts.get(5));
                        stateview.setText("État : " + texts.get(6));
                        numberview.setText("Téléphone : " + texts.get(7));
                        latitude = texts.get(8);
                        longtitude = texts.get(9);
                        importance = texts.get(4);
                        assignee_view.setText("Destinataire : "+texts.get(12));
                        assignee_number_view.setText("Numéros du destinataire : "+texts.get(13));
                        id = Integer.parseInt(texts.get(10));
                        locationview.setText(texts.get(11));
                        createMapView();
                        mTexts = texts;
                        isCreated = true;
                    }
                });

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_event, changeEventFragment, null)
                        .addToBackStack(null).commit();
                break;
            //case R.id.rapeler:
               // phone();
                //break;
            case R.id.delete:
                //DELETE FROM  events WHERE event_id=0
                delete();
                break;

        }
        return true;
    }


    public void phone(final String numberToCall) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setIcon(R.drawable.ic_call_black_24dp);//设置图标
        builder.setTitle("Validation");//设置对话框的标题
        builder.setMessage("Vous voulez appele " + numberToCall + " ? ");//设置对话框的内容
        builder.setPositiveButton("Appeler", new DialogInterface.OnClickListener() {  //这个是设置确定按钮
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + numberToCall));
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
        if(!latitude.equals("null") && !latitude.equals("null")) {
            LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longtitude));
            mMap.addMarker(new MarkerOptions().position(latLng).title("I am Here"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.setMinZoomPreference(16);
        }
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

    private void setNewTextAfterChange() {
        if (isCreated) {
            arthor = mTexts.get(0);
            title = mTexts.get(1);
            category = mTexts.get(2);
            description = mTexts.get(3);
            importance = mTexts.get(4);
            date = mTexts.get(5);
            state = mTexts.get(6);
            author_phoneNumber = mTexts.get(7);
            latitude = mTexts.get(8);
            longtitude = mTexts.get(9);
            assignee = mTexts.get(12);
            assignee_number = mTexts.get(13);
            location = mTexts.get(11);

            arthorview.setText("Arthor : " + ifNull(mTexts.get(0)));
            titleview.setText("Titre : " + ifNull(mTexts.get(1)));
            categoryview.setText("Catégorie : " + ifNull(mTexts.get(2)));
            descriptionview.setText("Description : " + ifNull(mTexts.get(3)));
            dateview.setText("Date : " + ifNull(mTexts.get(5)));
            stateview.setText("État : " + ifNull(mTexts.get(6)));
            numberview.setText("Téléphone : " + ifNull(mTexts.get(7)));
            latitude = mTexts.get(8);
            longtitude = mTexts.get(9);
            importance = mTexts.get(4);
            id = Integer.parseInt(mTexts.get(10));
            assignee_view.setText("Destinataire : " + ifNull(mTexts.get(12)));
            assignee_number_view.setText("Numéros du destinataire : " + ifNull(mTexts.get(13)));
            locationview.setText(" Localisation : " + ifNull(mTexts.get(11)));

            showImportance(importance);
            createMapView();

        }
    }

    private String ifNull(String s){
       return s+" ";
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }
}
