package com.example.wangwei.polycare_f.activity;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.wangwei.polycare_f.R;
import com.example.wangwei.polycare_f.fragment.GrapheFragment;
import com.example.wangwei.polycare_f.fragment.HomePageFragment;
import com.example.wangwei.polycare_f.fragment.MapFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private HomePageFragment homePageFragment;
    private MapFragment mapFragment;
    private GrapheFragment grapheFragment;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;

    private TextView txt_homepage;
    private TextView txt_graphe;
    private TextView txt_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI(){
        txt_homepage = (TextView)findViewById(R.id.txt_homepage);
        txt_graphe = (TextView)findViewById(R.id.txt_graphe);
        txt_map = (TextView)findViewById(R.id.txt_map);

        frameLayout = (FrameLayout)findViewById(R.id.main_fragment_container);
        //设置监听器
        txt_homepage.setOnClickListener(this);
        txt_graphe.setOnClickListener(this);
        txt_map.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        homePageFragment = new HomePageFragment();
        transaction.add(R.id.main_fragment_container,homePageFragment);
        transaction.commit();

        txt_homepage.setSelected(true);
    }

    @Override
    public void onClick(View view) {
        //v4包导入getSupportFragmentManager，app包使用getFragmentManager，app包3.0后才使用
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (view.getId()){

            case R.id.txt_homepage:
                reTxtSelect();
                txt_homepage.setSelected(true);
                if(homePageFragment==null){
                    homePageFragment = new HomePageFragment();
                    transaction.add(R.id.main_fragment_container,homePageFragment);
                }else{
                    transaction.show(homePageFragment);
                }
                break;
            case R.id.txt_graphe:
                reTxtSelect();
                txt_graphe.setSelected(true);
                if(grapheFragment==null){
                    grapheFragment = new GrapheFragment();
                    transaction.add(R.id.main_fragment_container,grapheFragment);
                }else{
                    transaction.show(grapheFragment);
                }
                break;
            case R.id.txt_map:
                reTxtSelect();
                txt_map.setSelected(true);
                if(mapFragment==null){
                    mapFragment = new MapFragment();
                    transaction.add(R.id.main_fragment_container,mapFragment);
                }else{
                    transaction.show(mapFragment);
                }
                break;

        }

        transaction.commit();

    }

    //初始化底部菜单选择状态
    private void reTxtSelect(){
        txt_homepage.setSelected(false);
        txt_graphe.setSelected(false);
        txt_map.setSelected(false);
    }

    //隐藏所有Fragment
    public void hideAllFragment(FragmentTransaction transaction){
        if(homePageFragment!=null){
            transaction.hide(homePageFragment);
        }
        if(grapheFragment!=null){
            transaction.hide(grapheFragment);
        }
        if(mapFragment!=null){
            transaction.hide(mapFragment);
        }
    }
}
