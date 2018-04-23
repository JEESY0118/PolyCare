package com.example.wangwei.polycare.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangwei.polycare.R;
import com.example.wangwei.polycare.data.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AnnouncementActivity extends AppCompatActivity {
    List<String> urgences = new ArrayList<String>();
    private static final String ACTIVITY_TAG = "LogDemo";
    private DBHelper helper;
    private Spinner spinner;
    private Spinner cateSpinner;
    private String cate;
    private String urg;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapterCate;
    List<String> categories = new ArrayList<String>();
    EditText nom, prenom, title, category, urgence, description, number;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_announcement);

        helper = new DBHelper(this);
        createUrgenceSpinner();
        createCategorySpinner();

    }

    private void createUrgenceSpinner() {
        urgences.add("*Urgence");
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
        /*下拉菜单弹出的内容选项触屏事件处理*/
        spinner.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        spinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void createCategorySpinner() {
        categories.add("*Catégorie");
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
        /*下拉菜单弹出的内容选项触屏事件处理*/
        cateSpinner.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        cateSpinner.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

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
                        "event_importance,event_state, event_date,event_number )\n" +
                        "VALUES('"+strings.get(0) + "','" + cate + "','"+ strings.get(1) +"','" + strings.get(4) + "','" +
                        urg + "','" + strings.get(5) + "','" + strings.get(3) + "','" + strings.get(2) + "');";
                Log.i(ACTIVITY_TAG, sql);
                inertOrUpdateDateBatch(sql);
                Toast.makeText(AnnouncementActivity.this, "Réussi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(AnnouncementActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(AnnouncementActivity.this, "Annuler", Toast.LENGTH_SHORT).show();

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

        return strings;
    }

    public void inertOrUpdateDateBatch(String sqls) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.execSQL(sqls);
// 设置事务标志为成功，当结束事务时就会提交事务
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
// 结束事务
            db.endTransaction();
            db.close();
        }
    }

}
