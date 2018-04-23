package com.example.wangwei.polycare.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.wangwei.polycare.R;
import com.example.wangwei.polycare.adapter.RecyclerViewAdapter;
import com.example.wangwei.polycare.data.DBHelper;
import com.example.wangwei.polycare.data.Event;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    List<Event> events;
    List<String> categories = new ArrayList<String>();
    List<String> tris = new ArrayList<String>();
    private DBHelper dbHelper;
    private Spinner cateSpinner;
    private Spinner impSpinner;
    private ArrayAdapter<String> adapterCate;
    private ArrayAdapter<String> adapterImp;
    public static final String ACTIVITY =  "debug here";

    /**
     * add action for bottom navigator
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_declaration:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, AnnouncementActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigation_graphe:
                    return true;
                case R.id.navigation_location:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        events = dbHelper.getAllEvent();

        createCateSpinner();
        createImpSpinner();

        RecyclerView myview = (RecyclerView) findViewById(R.id.recyclerview);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, events);
        myview.setLayoutManager(new GridLayoutManager(this, 1));
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        myview.setAdapter(adapter);
    }

    private void createCateSpinner(){
        categories.add("Tout");
        categories.addAll(dbHelper.getCategories());

        cateSpinner = (Spinner)findViewById(R.id.Spinner_cate);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterCate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterCate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        cateSpinner.setAdapter(adapterCate);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        cateSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

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

        impSpinner = (Spinner)findViewById(R.id.Spinner_tri);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterImp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tris);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapterImp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        impSpinner.setAdapter(adapterImp);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        impSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

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


}
