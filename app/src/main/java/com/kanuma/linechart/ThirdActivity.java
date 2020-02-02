package com.kanuma.linechart;


import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.kanuma.linechart.Algorithm.Algo;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


public class ThirdActivity extends AppCompatActivity {

    private static final String TAG = "ThirdActivity";
    private LinearLayout layout;
    private ScatterChartView chartView;
    private RadioGroup optionGroup;
    private Button startButton;
    private Button clearButton;
    private Spinner spinner;

    private String[] algo ={"BFS","DFS","A-Star"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        loadData();

        layout = findViewById(R.id.ll3);

        spinner =findViewById(R.id.algolist_spinner);
        ArrayAdapter aa = new ArrayAdapter(this,R.layout.spinner_row,algo);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(aa);
        spinner.setOnItemSelectedListener(onItemSelectedListener);

        chartView= new ScatterChartView(this);
        layout.addView(chartView);

        optionGroup = findViewById(R.id.radio_group);
        optionGroup.setOnCheckedChangeListener(changeListener);

        startButton =findViewById(R.id.startBtn);
        startButton.setOnClickListener(startButtonListener);

        clearButton =findViewById(R.id.clearBtn);
        clearButton.setOnClickListener(clearButtonListener);

        //addOptionView();

    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if(algo[position].equals("BFS")){
                chartView.setCurrentAlgoName(Algo.Name.BFS);
            }else if(algo[position].equals("DFS")){
                chartView.setCurrentAlgoName(Algo.Name.DFS);
            }else if(algo[position].equals("A-Star")){
                chartView.setCurrentAlgoName(Algo.Name.A_STAR);
            }else{

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            chartView.setCurrentAlgoName(Algo.Name.BFS);
        }
    };

    RadioGroup.OnCheckedChangeListener changeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {

            switch (checkedId){
                case R.id.obstacles_option:
                    chartView.setMode(STATE_NODE.OBSTACLE_NODE);
                    break;
                case R.id.source_option:
                    chartView.setMode(STATE_NODE.START_NODE);
                    break;
                case R.id.destination_option:
                    chartView.setMode(STATE_NODE.GOAL_NODE);
                    break;
                default:
                    Log.e(TAG, "onCheckedChanged: ERROR");
            }

        }
    };


    View.OnClickListener startButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chartView.startAnimating();
        }
    };

    View.OnClickListener clearButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chartView.clearGrid();
        }
    };



    /*
    private void addOptionView(){
        View v = LayoutInflater.from(this).inflate(R.layout.ui_cardview_container,layout,false);
        ((ConstraintLayout) layout.getParent()).addView(v);

        ConstraintSet set =new ConstraintSet();
        set.connect(v.getId(), ConstraintSet.LEFT,
                ConstraintSet.PARENT_ID, ConstraintSet.LEFT, 0);
        set.connect(v.getId(), ConstraintSet.RIGHT,
                ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, 0);
        set.connect(v.getId(), ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP, 0);
        set.connect(v.getId(), ConstraintSet.BOTTOM,
                ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM, 16);

        //set.clear(v.getId(), ConstraintSet.TOP);

        set.applyTo((ConstraintLayout) layout.getParent());

    }

    */

    private List loadData(){
        List data=null;
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.top50);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream));
            data = reader.readAll();
        } catch (IOException e) {
            Log.d("Data : ","Error");
            e.printStackTrace();
        }
        return data;
    }
}
