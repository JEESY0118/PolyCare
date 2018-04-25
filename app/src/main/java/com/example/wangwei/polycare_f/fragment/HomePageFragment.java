package com.example.wangwei.polycare_f.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wangwei.polycare_f.activity.DeclarationActivity;
import com.example.wangwei.polycare_f.data.DBHelper;
import com.example.wangwei.polycare_f.data.Event;
import com.example.wangwei.polycare_f.R;
import com.example.wangwei.polycare_f.adapter.RecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class HomePageFragment extends Fragment {
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.homepage_fragment, container, false);
        dbHelper = new DBHelper(getContext());

        events = dbHelper.getAllEvent(cate);

        FloatingActionButton fab = rootview.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), DeclarationActivity.class));
            }
        });

        createCateSpinner();
        createImpSpinner();

        recyclerview = rootview.findViewById(R.id.recyclerview);
        viewAdapter= new RecyclerViewAdapter(getContext(), events);
        recyclerview.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerview.setAdapter(viewAdapter);

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
        /*下拉菜单弹出的内容选项触屏事件处理*/
        cateSpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        cateSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void createImpSpinner(){
        tris.add("Date");
        tris.add("Urgence");
        tris.add("État");

        impSpinner = rootview.findViewById(R.id.Spinner_tri);
        adapterImp = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, tris);
        adapterImp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        impSpinner.setAdapter(adapterImp);
        impSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sort = impSpinner.getSelectedItem().toString();
                switch (sort){
                    case "Date":
                        getSortByDate(events);
                        viewAdapter.setData(events);
                        recyclerview.setAdapter(viewAdapter);
                        break;
                    case "Urgence":
                        List<Event> list1 = getSortByImportance(events);
                        viewAdapter.setData(list1);
                        recyclerview.setAdapter(viewAdapter);
                        break;
                    case "État":
                        List<Event> list2 = getSortByState(events);
                        viewAdapter.setData(list2);
                        recyclerview.setAdapter(viewAdapter);
                        break;
                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        impSpinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        impSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

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
                    if (dt1.getTime() > dt2.getTime()) {
                        return 1;
                    } else if (dt1.getTime() < dt2.getTime()) {
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

}


