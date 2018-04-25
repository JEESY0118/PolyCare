package com.example.wangwei.polycare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.wangwei.polycare.R;
import com.example.wangwei.polycare.fragment.FragmentHomePage;
import com.example.wangwei.polycare.fragment.FragmentTest;

import static com.example.wangwei.polycare.R.id.fragment_container;

public class TestAvtivity extends AppCompatActivity {

    private FrameLayout main_frameLayout;
    //Fragment管理
    private FragmentManager fragmentManager;
    //Fragment类
    private FragmentHomePage homePage;
    private FragmentTest test;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initUI();
    }

    private void initUI(){
        main_frameLayout = (FrameLayout) findViewById(R.id.fragment_container);
        fragmentManager = getFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        homePage = new FragmentHomePage();
        transaction.add(R.id.fragment_container, homePage);
        transaction.commit();

         BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                         if(homePage == null){
                             homePage = new FragmentHomePage();
                             transaction.add(R.id.fragment_container, homePage);
                         }
                         else {
                             transaction.show(homePage);
                         }
                        return true;
                    case R.id.navigation_declaration:
//                        Intent intent = new Intent();
//                        intent.setClass(this, AnnouncementActivity.class);
                    //    startActivity(intent);
                        return true;
                    case R.id.navigation_graphe:
                        if(test == null){
                            test = new FragmentTest();
                            transaction.add(R.id.fragment_container, test);
                        }
                        else {
                            transaction.show(test);
                        }
                        return true;
                    case R.id.navigation_location:
                        return true;
                }
                return false;
            }
        };
    }

    public void hideFragment(FragmentTransaction transaction){
        if(homePage!=null){
            transaction.hide(homePage);
        }

        if(test != null){
            transaction.hide(test);
        }
    }




}
