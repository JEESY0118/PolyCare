package com.example.wangwei.polycare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wangwei.polycare.R;
import com.example.wangwei.polycare.data.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity{
    List<String> urgences = new ArrayList<String>();
    private DBHelper helper;
    private Spinner spinner;
    private Spinner cateSpinner;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterCate;
    List<String> categories = new ArrayList<String>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_announcement);

        helper = new DBHelper(this);
        createUrgenceSpinner();
        createCategorySpinner();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(AnnouncementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void createUrgenceSpinner(){
        urgences.add("*Urgence");
        urgences.addAll(helper.getUrgences());

        spinner = (Spinner)findViewById(R.id.Spinner_urgence);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, urgences);
        //第三步：为适配器设置下拉列表下拉时的菜单样式。
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //第四步：将适配器添加到下拉列表上
        spinner.setAdapter(adapter);
        //第五步：为下拉列表设置各种事件的响应，这个事响应菜单被选中
        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

            }
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        /*下拉菜单弹出的内容选项触屏事件处理*/
        spinner.setOnTouchListener(new Spinner.OnTouchListener(){
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        spinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener(){
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void createCategorySpinner(){
        categories.add("*Catégorie");
        categories.addAll(helper.getCategories());

        cateSpinner = (Spinner)findViewById(R.id.Spinner_category);
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

}
