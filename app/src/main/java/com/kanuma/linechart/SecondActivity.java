package com.kanuma.linechart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.Map;

public class SecondActivity extends AppCompatActivity implements ChartView.GraphCanvasAvailable {

    private LinearLayout layout;
    private ChartView chartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        layout = findViewById(R.id.ll2);
        chartView = new ChartView(this);
        layout.addView(chartView);
    }


    @Override
    public void plotPoint(Map xAxisPoints, Map yAxisPoints) {

    }
}
