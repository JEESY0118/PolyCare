package com.project.polycare_f.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChangeEventActivity extends AppCompatActivity{
    List<String> urgences = new ArrayList<>();
    List<String> categories = new ArrayList<>();
    List<String> states = new ArrayList<>();
    private ArrayAdapter<String> adapterUr;
    private ArrayAdapter<String> adapterCate;
    private ArrayAdapter<String> adapterSta;
    EditText prenom, title,description, number;
    Spinner urg, cate, state;
    String arthor, titre, des, phone, importance, etat, category,date;
    public static final String ACTIVITY = "debug here";
    int id;
    DBHelper helper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.change_event_item_land);
        }
        else {
            setContentView(R.layout.change_event_item);
        }

        helper = new DBHelper(this);

        prenom = (EditText) findViewById(R.id.prenom_input);
        title = (EditText) findViewById(R.id.title_input);
        description = (EditText) findViewById(R.id.description_input);
        number = (EditText) findViewById(R.id.phone_input);

        Intent intent = getIntent();
        arthor = intent.getExtras().getString("Name");
        titre = intent.getExtras().getString("Title");
        des =intent.getExtras().getString("Description");
        phone = intent.getExtras().getString("Phone");
        importance = intent.getExtras().getString("Importance");
        category = intent.getExtras().getString("Category");
        etat = intent.getExtras().getString("State");
        id = intent.getExtras().getInt("Id");
        date = intent.getExtras().getString("Date");


        prenom.setText(arthor);
        title.setText(titre);
        description.setText(des);
        number.setText(phone);

        createCategorySpinner();
        createUrgenceSpinner();
        createStateSpinner();
    }


    private void createUrgenceSpinner() {
        setUrgences();

        urg = (Spinner) findViewById(R.id.Spinner_urgence);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterUr = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, urgences);
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
        /*下拉菜单弹出的内容选项触屏事件处理*/
        urg.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        urg.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void createCategorySpinner() {

        setCategories();
        cate = (Spinner) findViewById(R.id.Spinner_category);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterCate = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
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
        /*下拉菜单弹出的内容选项触屏事件处理*/
        cate.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        cate.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

    private void createStateSpinner() {

        setEtat();
        state = (Spinner) findViewById(R.id.Spinner_state);
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        adapterSta = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);
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
        /*下拉菜单弹出的内容选项触屏事件处理*/
        state.setOnTouchListener(new Spinner.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                return false;
            }
        });
        /*下拉菜单弹出的内容选项焦点改变事件处理*/
        state.setOnFocusChangeListener(new Spinner.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

            }
        });
    }

    public void onclick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_add_alert_black_24dp);//设置图标
        builder.setTitle("Validation de la modification");//设置对话框的标题
        builder.setMessage("Vous voulez applique la modification？");//设置对话框的内容
        builder.setPositiveButton("Appliquer", new DialogInterface.OnClickListener() {  //这个是设置确定按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                List<String> strings = getInput();
                String sql =
                        "UPDATE events set event_title= " + "'"+strings.get(0)+"',"+
                        " event_category = " + "'"+category+"',"+
                        " event_description= " + "'"+strings.get(2)+"',"+
                        " event_reporter= " + "'"+strings.get(3)+"',"+
                        " event_importance= " +"'"+importance+"',"+
                        " event_state= " +"'"+etat+"',"+
                        " event_number= " +"'"+strings.get(6)+"'"+
                        " where event_id = "+"'"+id+"'";
                inertOrUpdateDateBatch(sql);
                Toast.makeText(ChangeEventActivity.this, "Réussi", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
//                intent.putExtra("Id", id);
//                intent.putExtra("Title", strings.get(0));
//                intent.putExtra("Name", strings.get(3));
//                intent.putExtra("Category", category);
//                intent.putExtra("Importance", importance);
//                intent.putExtra("Description", strings.get(2));
//                intent.putExtra("State", etat);
//                intent.putExtra("Date", date);
//                intent.putExtra("Phone", strings.get(6));

                intent.setClass(ChangeEventActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {  //取消按钮

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(ChangeEventActivity.this, "Annuler", Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog b = builder.create();
        b.show();  //必须show一下才能看到对话框，跟Toast一样的道理

    }

    private void setCategories(){
        List<String> cates = new ArrayList<>();
        cates.add("Matériel");
        cates.add("Organisation");
        cates.add("Parking");
        cates.add("Technique");
        cates.add("Autre");

        categories.add(category);
        for (String s: cates) {
            if(!s.equals(category)){
                categories.add(s);
            }
        }
    }

    private void setUrgences(){
        List<String> urs = new ArrayList<>();
        urs.add("Faible");
        urs.add("Moyenne");
        urs.add("Elevée");

        urgences.add(importance);
        for (String s: urs) {
            if(!s.equals(importance)){
                urgences.add(s);
            }
        }
    }

    private void setEtat(){
        List<String> etats = new ArrayList<>();
        etats.add("A faire");
        etats.add("En cours");
        etats.add("Résolu");

        states.add(etat);
        for (String s: etats) {
            if(!s.equals(etat)){
                states.add(s);
            }
        }
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

    public List<String> getInput() {
        List<String> strings = new ArrayList<>();

        prenom = (EditText) findViewById(R.id.prenom_input);
        String name = prenom.getText().toString();

        title = (EditText) findViewById(R.id.title_input);
        String titre = title.getText().toString();

        number = (EditText) findViewById(R.id.phone_input);
        String phone = number.getText().toString();


        description = (EditText) findViewById(R.id.description_input);
        String descriptions = description.getText().toString();

        strings.add(titre);
        strings.add(category);
        strings.add(descriptions);
        strings.add(name);
        strings.add(importance);
        strings.add(etat);
        strings.add(phone);

        Log.i(ACTIVITY, strings.get(0));
        Log.i(ACTIVITY, strings.get(1));
        Log.i(ACTIVITY, strings.get(2));
        Log.i(ACTIVITY, strings.get(3));
        Log.i(ACTIVITY, strings.get(4));
        Log.i(ACTIVITY, strings.get(5));
        Log.i(ACTIVITY, strings.get(6));
        return strings;
    }

}
