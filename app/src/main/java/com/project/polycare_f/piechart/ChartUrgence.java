package com.project.polycare_f.piechart;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
import com.project.polycare_f.R;
import com.project.polycare_f.data.DBHelper;
import com.project.polycare_f.data.Event;

import java.util.ArrayList;
import java.util.List;

public class ChartUrgence extends Fragment implements OnChartGestureListener {
    private float[] urgenceDataY=null;
    private String[] urgenceDataX={"Faible", "Moyenne", "Elevée"};
    View view;
    PieChart pieChart;
    private DBHelper dbHelper;
    List<Event> events;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.urgence_piechart, container, false);

        dbHelper = new DBHelper(getContext());
        events = dbHelper.getAllEvent("Tout");
        getNumberOfEventsUrgence();

        pieChart = view.findViewById(R.id.urgencechart);
        pieChart.setDescription("PieChart d'Urgence");
        pieChart.setDescriptionTextSize(20);
        pieChart.setRotationEnabled(true);

        pieChart.setHoleRadius(25f);
        pieChart.setHoleColor(Color.parseColor("#61c4cf"));
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setCenterText("Urgence");
        pieChart.setCenterTextSize(16);

        pieChart.setDrawEntryLabels(true);

        addDateSet();
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                int pos1 = e.toString().indexOf("(sum): ");
                String number = e.toString().substring(pos1+7);

                for (int i = 0; i < urgenceDataY.length; i++) {
                    if(urgenceDataY[i] == Float.parseFloat(number)){
                        pos1 = i;
                        break;
                    }
                }

                String nom = urgenceDataX[pos1];
                Toast toast= Toast.makeText(getContext(), "Urgence: " + nom+ "\n" + "Nombre d'évenement: "+ number, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundColor(Color.parseColor("#61c4cf"));
                toast.show();

            }

            @Override
            public void onNothingSelected() {

            }
        });
        return view;
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

    private void addDateSet(){
        ArrayList<PieEntry> yEntry = new ArrayList<>();
        ArrayList<String> xEntry = new ArrayList<>();

        for (int i = 0; i < urgenceDataY.length; i++) {
            yEntry.add(new PieEntry(urgenceDataY[i],i));
        }


        for (int i = 0; i < urgenceDataX.length; i++) {
            xEntry.add(urgenceDataX[i]);
        }

        PieDataSet dataSet = new PieDataSet(yEntry, "Urgence");
        dataSet.setSliceSpace(2);
        dataSet.setValueTextSize(14);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#85c309"));
        colors.add(Color.parseColor("#eb9809"));
        colors.add(Color.parseColor("#950707"));
        
        dataSet.setColors(colors);

        Legend legend = pieChart.getLegend();
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        PieData pieData = new PieData(dataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
        
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
        urgenceDataY = f;
    }
}
