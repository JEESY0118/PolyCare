package com.project.polycare_f.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.project.polycare_f.R;
import com.project.polycare_f.adapter.RecyclerViewAdapter;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;
import com.project.polycare_f.interfaces.myItemMoveCallBack;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    List<Event> events;
    List<String> categories = new ArrayList<String>();
    private DBHelper dbHelper;
    private Spinner cateSpinner;
    private String cate = "Tout";
    private RecyclerView recyclerview;
    private RecyclerViewAdapter viewAdapter;
    private ArrayAdapter<String> adapterCate;
    private View rootview;
    public static final String ACTIVITY = "debug here";
    int numberOfEvents;
    Toolbar toolbar;
    ImageView mapView;
    EditText rechercher;
    ImageButton search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage_fragment);

        dbHelper = new DBHelper(this);

        events = dbHelper.getAllEvent(cate);
        numberOfEvents = events.size();

        createCateSpinner();

        mapView = (ImageView) findViewById(R.id.map);
        rechercher = (EditText) findViewById(R.id.rechercher);
        search = (ImageButton) findViewById(R.id.search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mapView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSearch = rechercher.getText().toString();
                List<Event> eventList = new ArrayList<>();
                for (Event event : events) {
                    if (event.getDescription().toString().contains(toSearch) ||
                            event.getAssignee().contains(toSearch) ||
                            event.getCategory().contains(toSearch) ||
                            event.getReporter().contains(toSearch) ||
                            event.getTitle().contains(toSearch)) {
                        eventList.add(event);
                    }
                }
                viewAdapter.setData(eventList);
                recyclerview.setAdapter(viewAdapter);
            }
        });

        toolbar.setTitle(R.string.empty);
        this.setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DeclarationActivity.class);
                intent.putExtra("Number", numberOfEvents);
                startActivity(intent);
            }
        });

        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        viewAdapter = new RecyclerViewAdapter(this, events, dbHelper);
        recyclerview.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerview.setAdapter(viewAdapter);
        ItemTouchHelper.Callback callback = new myItemMoveCallBack(viewAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerview);

    }

    private void createCateSpinner() {
        categories.add("Tout");
        categories.addAll(dbHelper.getCategories());
        categories.add("A assigner");

        cateSpinner =findViewById(R.id.Spinner_cate);

        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        //we have to define an adapter for spinner
        adapterCate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        //set style for spinner
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        //we add adapter on the spinner
        cateSpinner.setAdapter(adapterCate);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        //add listener for spinner
        cateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                cate = cateSpinner.getSelectedItem().toString();
                TextView tv = (TextView) arg1;
                tv.setTextColor(Color.WHITE);
                tv.setTextSize(20);
                tv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                List<Event> newsEvents = getNewEventsAccordingToCate(events, cate);
                if (!newsEvents.isEmpty()) {
                    viewAdapter.setData(newsEvents);
                    recyclerview.setAdapter(viewAdapter);
                } else {
                    viewAdapter.setData(newsEvents);
                    recyclerview.setAdapter(viewAdapter);
                    Toast.makeText(MainActivity.this,"Pas d'évenement dans " + cate, Toast.LENGTH_SHORT).show();
                }
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public List<Event> getNewEventsAccordingToCate(List<Event> events, String cate) {
        List<Event> eventList = new ArrayList<>();
        if (!cate.equals("Tout") && !(cate.equals("A assigner"))) {
            for (Event event : events) {
                if (event.getCategory().equals(cate)) {
                    eventList.add(event);
                }
            }
            return eventList;
        } else if (cate.equals("A assigner")){
            for (Event event : events) {
                Log.d("WEI",event.getAssignee());
                if (event.getAssignee()==null || event.getAssignee().equals("") || event.getAssignee().equals("null")) {
                    eventList.add(event);
                }
            }
            return eventList;
        }
        return events;
    }

    public List<Event> getSortByImportance(List<Event> events) {
        List<Event> eventList = new ArrayList<>();
        for (Event e : events) {
            if (e.getImportance().equals("Elevée")) {
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if (e.getImportance().equals("Moyenne")) {
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if (e.getImportance().equals("Faible")) {
                eventList.add(e);
            }
        }

        return eventList;
    }

    public List<Event> getSortByState(List<Event> events) {
        List<Event> eventList = new ArrayList<>();
        for (Event e : events) {
            if (e.getState().equals("A faire")) {
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if (e.getState().equals("En cours")) {
                eventList.add(e);
            }
        }

        for (Event e : events) {
            if (e.getState().equals("Résolu")) {
                eventList.add(e);
            }
        }

        return eventList;
    }


    /**
     * sorted by date
     *
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return super.onCreateOptionsMenu(menu);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
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
        return true;
    }

    @Override
    public void onClick(View view) {

    }



}
