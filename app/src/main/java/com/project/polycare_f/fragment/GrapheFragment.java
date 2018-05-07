package com.project.polycare_f.fragment;


import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class GrapheFragment extends Fragment implements OnChartGestureListener {

        private static String TAG= "Graphe";
        private List<Event> events;
        private float[] yData=null;
        private String[] xData={"Faible", "Moyenne", "Elevée"};
        PieChart pieChart;
        private DBHelper dbHelper;
        private View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            rootview = inflater.inflate(R.layout.graphe_fragment_land, container, false);
        }
        else {
            rootview = inflater.inflate(R.layout.graphe_fragment, container, false);
        }
        Log.d(TAG, "On create char");

        dbHelper = new DBHelper(getContext());
        events = dbHelper.getAllEvent("Tout");
        getNumberOfEventsUrgence();
        pieChart = rootview.findViewById(R.id.chart1);
        pieChart.setDescription("PieChart d'Urgence");
        pieChart.setDescriptionTextSize(10);
        pieChart.setRotationEnabled(true);

        pieChart.setHoleRadius(25f);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Urgence");
        pieChart.setCenterTextSize(14);
        pieChart.setDrawEntryLabels(true);

        addDateSet();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Log.d(TAG, e.toString());
                Log.d(TAG, h.toString());
                int pos1 = e.toString().indexOf("(sum): ");
                String number = e.toString().substring(pos1+7);

                for (int i = 0; i < yData.length; i++) {
                    if(yData[i] == Float.parseFloat(number)){
                        pos1 = i;
                        break;
                    }
                }

                String nom = xData[pos1];
                Toast.makeText(getContext(), "Urgence: " + nom+ "\n" + "Nombre d'évenement: "+ number, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNothingSelected() {

            }
        });

        return rootview;

    }

    private void addDateSet(){
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for (int i = 0; i < yData.length; i++) {
            yEntry.add(new PieEntry(yData[i],i));
        }


        for (int i = 0; i < xData.length; i++) {
            xEntry.add(xData[i]);
        }

        PieDataSet dataSet = new PieDataSet(yEntry, "urgence");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(14);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GREEN);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);

        dataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();


    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    private void getNumberOfEventsUrgence(){
        float f[] = {0f,0f,0f};
        for (Event e:events) {
            switch (e.getImportance()){
                case "Faible":
                    f[0] += 1f;
                    break;
                case "Moyenne":
                    f[1] += 1f;
                    break;
                case "Elevée":
                    f[2] += 1f;
                    break;
            }
        }
        yData = f;
    }
}