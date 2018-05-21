package com.project.polycare_f.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.polycare_f.R;
import com.project.polycare_f.activity.DeclarationActivity;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;
import com.project.polycare_f.adapter.RecyclerViewAdapter;
import com.project.polycare_f.interfaces.myItemMoveCallBack;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class  HomePageFragment extends Fragment {
    List<Event> events;
    List<String> categories = new ArrayList<String>();
    List<String> tris = new ArrayList<String>();
    private DBHelper dbHelper;
    private Spinner cateSpinner;
    private Spinner impSpinner;
    private String cate = "Tout";
    private String sort = "Date";
    private RecyclerView recyclerview;
    private RecyclerViewAdapter viewAdapter;
    private ArrayAdapter<String> adapterCate;
    private ArrayAdapter<String> adapterImp;
    private View rootview;
    public static final String ACTIVITY =  "debug here";
    int numberOfEvents;
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rootview = inflater.inflate(R.layout.homepage_fragment_land, container, false);
        }
        else {
            rootview = inflater.inflate(R.layout.homepage_fragment, container, false);
        }
        dbHelper = new DBHelper(getContext());

        events = dbHelper.getAllEvent(cate);
        numberOfEvents = events.size();

        createCateSpinner();
        FloatingActionButton fab = (FloatingActionButton) rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DeclarationActivity.class);
                intent.putExtra("Number", numberOfEvents);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        toolbar.setTitle(R.string.empty);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);


        recyclerview = rootview.findViewById(R.id.recyclerview);
        viewAdapter= new RecyclerViewAdapter(getContext(), events, dbHelper);
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerview.setAdapter(viewAdapter);
        ItemTouchHelper.Callback callback = new myItemMoveCallBack(viewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerview);

        return rootview;
    }

    private void createCateSpinner(){
        categories.add("Tout");
        categories.addAll(dbHelper.getCategories());

        cateSpinner = rootview.findViewById(R.id.Spinner_cate);

        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        //we have to define an adapter for spinner
        adapterCate = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, categories);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        //set style for spinner
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        //we add adapter on the spinner
        cateSpinner.setAdapter(adapterCate);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        //add listener for spinner
        cateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cate = cateSpinner.getSelectedItem().toString();
                TextView tv = (TextView) arg1;
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(20);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                List<Event> newsEvents= getNewEventsAccordingToCate(events, cate);
                if(!newsEvents.isEmpty()) {
                    viewAdapter.setData(newsEvents);
                    recyclerview.setAdapter(viewAdapter);
                }
                else {
                    viewAdapter.setData(newsEvents);
                    recyclerview.setAdapter(viewAdapter);
                    Toast.makeText(getContext(), "Pas d'évenement dans "+cate, Toast.LENGTH_SHORT).show();
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }



    /**
     * get events according to its category
     * @param events
     * @param cate
     * @return
     */
    public List<Event> getNewEventsAccordingToCate(List<Event> events, String cate) {
        List<Event> eventList = new ArrayList<>();
        if(!cate.equals("Tout")) {
            for (Event event : events) {
                if (event.getCategory().equals(cate)) {
                    eventList.add(event);
                }
            }
            return eventList;
        }
        return events;
    }

    /**
     * sorted by importance,
     * Elevée, Moyenne, Faible
     * @param events
     * @return
     */
    public List<Event> getSortByImportance(List<Event> events){
        List<Event> eventList = new ArrayList<>();
        for (Event e : events) {
            if(e.getImportance().equals("Elevée")){
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if(e.getImportance().equals("Moyenne")){
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if(e.getImportance().equals("Faible")){
                eventList.add(e);
            }
        }

        return eventList;
    }

    public List<Event> getSortByState(List<Event> events){
        List<Event> eventList = new ArrayList<>();
        for (Event e : events) {
            if(e.getState().equals("A faire")){
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if(e.getState().equals("En cours")){
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if(e.getState().equals("Résolu")){
                eventList.add(e);
            }
        }

        return eventList;
    }



    /**
     * sorted by date
     * @param list
     */
    private static void getSortByDate(List<Event> list) {
        Collections.sort(list, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                SimpleDateFormat myFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    Date dt1 = myFmt.parse(e1.getDate());
                    Date dt2 = myFmt.parse(e2.getDate());
                    if (dt1.getTime() < dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() > dt2.getTime()) {
                        return -1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_sort, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.date_sort:
                getSortByDate(events);
                viewAdapter.setData(events);
                recyclerview.setAdapter(viewAdapter);
                break;
            case R.id.state_sort:
                List<Event> list2 = getSortByState(events);
                viewAdapter.setData(list2);
                recyclerview.setAdapter(viewAdapter);
                break;
            case R.id.urgence_sort:
                List<Event> list1 = getSortByImportance(events);
                viewAdapter.setData(list1);
                recyclerview.setAdapter(viewAdapter);
                break;

        }
        return true;
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


