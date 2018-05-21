package com.project.polycare_f.fragment;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.piechart.ChartCategory;
import com.project.polycare_f.piechart.ChartState;
import com.project.polycare_f.piechart.ChartUrgence;


public class GrapheFragment extends Fragment implements Button.OnClickListener{

    private static String TAG = "Graphe";
    private View rootview;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    ChartCategory chartCategory;
    ChartUrgence chartUrgence;
    ChartState chartState;
    FragmentTransaction transaction;
    Button urgence, state, category;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rootview = inflater.inflate(R.layout.graphe_fragment_land, container, false);
        } else {
            rootview = inflater.inflate(R.layout.graphe_fragment, container, false);
        }
        Log.d(TAG, "On create char");

        urgence = rootview.findViewById(R.id.urgence);
        state = rootview.findViewById(R.id.state);
        category = rootview.findViewById(R.id.category);

        urgence.setOnClickListener(this);
        state.setOnClickListener(this);
        category.setOnClickListener(this);

        frameLayout = rootview.findViewById(R.id.chart_fragment);
        fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();
        chartUrgence = new ChartUrgence();
        transaction.add(R.id.chart_fragment, chartUrgence);
        transaction.commit();
        urgence.setSelected(true);

        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.graph);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        return rootview;
    }

    @Override
    public void onClick(View view) {
        fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        hideAllFragment(transaction);
        switch (view.getId()){
            case R.id.urgence:
                reTxtSelect();
                urgence.setSelected(true);
                if(chartUrgence==null){
                    chartUrgence = new ChartUrgence();
                    transaction.add(R.id.chart_fragment,chartUrgence);
                }else{
                    transaction.show(chartUrgence);
                }
                break;
            case R.id.state:
                reTxtSelect();
                state.setSelected(true);
                if(chartState==null){
                    chartState = new ChartState();
                    transaction.add(R.id.chart_fragment,chartState);
                }else{
                    transaction.show(chartState);
                }
                break;
            case R.id.category:
                reTxtSelect();
                category.setSelected(true);
                if(chartCategory==null){
                    chartCategory = new ChartCategory();
                    transaction.add(R.id.chart_fragment,chartCategory);
                }else{
                    transaction.show(chartCategory);
                }
                break;

        }

        transaction.commit();
    }

    private void reTxtSelect(){
        state.setSelected(false);
        urgence.setSelected(false);
        category.setSelected(false);
    }


    public void hideAllFragment(FragmentTransaction transaction){
        if(chartState!=null){
            transaction.hide(chartState);
        }
        if(chartCategory!=null){
            transaction.hide(chartCategory);
        }
        if(chartUrgence!=null){
            transaction.hide(chartUrgence);
        }
    }
}