package com.kanuma.linechart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyLineView.OnSquareClickListener {

    private LinearLayout layout;
    private MyLineView lineView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.ll);
        lineView =new MyLineView(this,null);
        layout.addView(lineView);
    }

    @Override
    public void onSquareListener(int x, int y) {

        Toast.makeText(this,"X="+x,Toast.LENGTH_SHORT).show();
        lineView.animateWin(0,0,1,2);

        Intent i =new Intent(this,SecondActivity.class);
        startActivity(i);

    }
}
