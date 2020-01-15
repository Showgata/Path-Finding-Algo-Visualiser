package com.kanuma.linechart;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

enum STATE_NODE{OBSTACLE_NODE,GOAL_NODE,START_NODE,EMPTY};

public class ScatterChartView extends View {

    private static final String TAG = "ScatterChartView";
    public ScatterChartView(Context context) {
        super(context);
        init(context,null);
    }
    public ScatterChartView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    public ScatterChartView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    public ScatterChartView(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }


    private float screenWidth;
    private float screenHeight;
    private float originX;
    private float originY;

    private Paint backgroundPaint;
    private Paint backgroundPaint2;
    private Paint obstaclePaint;
    private Paint sourcePaint;
    private Paint destinationPaint;

    private float offsetX;
    private float offsetY;
    private float divX = 20f;// xcoordinate into x equal part
    private float divY= 20f;// ycoordinate into y equal part
    private ArrayList<ArrayList<RectF>> rectBoxes = new ArrayList<>();
    private STATE_NODE state = STATE_NODE.OBSTACLE_NODE;

    //private STATE_NODE[][] rectStateMat;
    private Node[][] nodes;


    private void init(Context context, AttributeSet attrs) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight =displayMetrics.heightPixels;
        originX = screenWidth/2;
        originY = screenHeight/2;

        offsetX = screenWidth/divX;
        offsetY = screenHeight/divY;

        obstaclePaint = new Paint();
        obstaclePaint.setColor(Color.BLACK);
        obstaclePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        obstaclePaint.setStrokeWidth(1);
        obstaclePaint.setStrokeCap(Paint.Cap.ROUND);

        sourcePaint = new Paint();
        sourcePaint.setColor(Color.GREEN);
        sourcePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        sourcePaint.setStrokeWidth(1);
        sourcePaint.setStrokeCap(Paint.Cap.ROUND);

        destinationPaint = new Paint();
        destinationPaint.setColor(Color.RED);
        destinationPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        destinationPaint.setStrokeWidth(1);
        destinationPaint.setStrokeCap(Paint.Cap.ROUND);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.DKGRAY);
        backgroundPaint.setStyle(Paint.Style.FILL);
        backgroundPaint.setStrokeWidth(5);

        backgroundPaint2 = new Paint();
        backgroundPaint2.setColor(Color.LTGRAY);
        backgroundPaint2.setStyle(Paint.Style.STROKE);
        backgroundPaint2.setStrokeWidth(3);

        //rectStateMat =new STATE_NODE[(int) divY][(int) divX];
        nodes = new Node[(int) divY][(int) divX];
        initStateMatrix();
        initRectBoxes();

    }

    //Initialize stateMatrix to -1
    private void initStateMatrix(){

        for(int i=0;i<divX;i++){
            for(int j=0;j<divY;j++){
                //rectStateMat[i][j] = STATE_NODE.EMPTY;
                nodes[i][j] = new Node(i,j,true,STATE_NODE.EMPTY);
            }
        }
    }

    //Initialize Rect objects for later use
    private void initRectBoxes(){

        ArrayList<RectF> rectRowBoxes;

        for(int i=0;i<divX;i++){
            rectRowBoxes = new ArrayList<>();
            for(int j=0;j<divY;j++){
                RectF unitRectF = new RectF(i * offsetX, j * offsetY, offsetX * (i + 1), offsetY * (j + 1));
                //Log.d(TAG, "initRectBoxes: Rect ="+unitRectF.toShortString());
                rectRowBoxes.add(unitRectF);




            }
            //Log.d(TAG, "initRectBoxes: Row Data = "+rectRowBoxes.toString());
            rectBoxes.add(rectRowBoxes);
        }

    }

    //Get specific rect
    private int rectXIndex;
    private int rectYIndex;
    private RectF getRect(float x, float y){

        for(ArrayList<RectF> eachRowRect:rectBoxes){
            for(RectF rect : eachRowRect){
                if(rect.contains(x,y)){
                    //Log.d(TAG, "getRect: Rect="+rect.toShortString());
                    rectYIndex = eachRowRect.indexOf(rect);
                    rectXIndex = rectBoxes.indexOf(eachRowRect);
                    return rect;
                }

            }
        }

        return null;
    }

    private Pair<Integer,Integer> previouslySelectedStartNode = null;
    private Pair<Integer,Integer> previouslySelectedGoalNode = null;

    private void setState(int rectXIndex, int rectYIndex) {

        Log.d(TAG, "setState: Matrix : "+nodes[rectXIndex][rectYIndex]);
        switch (state){
            case OBSTACLE_NODE:
                //rectStateMat[rectXIndex][rectYIndex]=state;
                nodes[rectXIndex][rectYIndex].setNodeType(state);
                nodes[rectXIndex][rectYIndex].setWalkable(false);
                break;
            case START_NODE:
                //clicking the obstacle doesn't do shit
                if(!nodes[rectXIndex][rectYIndex].isWalkable()){
                    return;
                }

                if(previouslySelectedStartNode !=null){
                    int sx = previouslySelectedStartNode.first;
                    int sy = previouslySelectedStartNode.second;
                    //rectStateMat[sx][sy] = STATE_NODE.EMPTY;
                    nodes[sx][sy].setNodeType(STATE_NODE.EMPTY);
                }
                //rectStateMat[rectXIndex][rectYIndex]=state;
                nodes[rectXIndex][rectYIndex].setNodeType(state);
                previouslySelectedStartNode = new Pair<>(rectXIndex,rectYIndex);
                break;
            case GOAL_NODE:
                //clicking the obstacle doesn't do shit
                if(!nodes[rectXIndex][rectYIndex].isWalkable()){
                    return;
                }

                if(previouslySelectedGoalNode !=null){
                    int sx = previouslySelectedGoalNode.first;
                    int sy = previouslySelectedGoalNode.second;
                    //rectStateMat[sx][sy] = STATE_NODE.EMPTY;
                    nodes[sx][sy].setNodeType(STATE_NODE.EMPTY);
                }
                //rectStateMat[rectXIndex][rectYIndex]=state;
                nodes[rectXIndex][rectYIndex].setNodeType(state);
                previouslySelectedGoalNode = new Pair<>(rectXIndex,rectYIndex);
                break;
            default:
                Log.d(TAG, "setState: Index : "+rectXIndex+","+rectYIndex);
        }

        invalidate();
    }

    private void drawBackground(Canvas canvas){
        //canvas.drawLine(originX,0,originX,screenHeight,backgroundPaint);
        //canvas.drawLine(0,originY,screenWidth,originY,backgroundPaint);


        float currentPos =0;
        for(int i=1;i<=divX;i++){
            currentPos+=offsetX;
            canvas.drawLine(currentPos,0,currentPos,screenHeight,backgroundPaint2);
        }

        currentPos=0;
        for(int i=1;i<=divY;i++){
            currentPos+=offsetY;
            canvas.drawLine(0,currentPos,screenWidth,currentPos,backgroundPaint2);
        }

    }

    void changeState(Canvas canvas){
        int yIndex;
        int xIndex;
        for(ArrayList<RectF> eachRowRect:rectBoxes){
            for(RectF rect : eachRowRect){
                yIndex = eachRowRect.indexOf(rect);
                xIndex = rectBoxes.indexOf(eachRowRect);
                if(nodes[xIndex][yIndex].getNodeType() == STATE_NODE.OBSTACLE_NODE ){
                    canvas.drawRect(rect,obstaclePaint);
                }else if(nodes[xIndex][yIndex].getNodeType() == STATE_NODE.START_NODE ){
                    canvas.drawRect(rect,sourcePaint);
                }else if(nodes[xIndex][yIndex].getNodeType() == STATE_NODE.GOAL_NODE ){
                    canvas.drawRect(rect,destinationPaint);
                }else{

                }

            }
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackground(canvas);
        changeState(canvas);

//        if(isTouching){
//            canvas.drawRect(unitRect,backgroundPaint);
//        }
    }

    private RectF unitRect ;
    private boolean isTouching =false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.i(TAG, "onTouchEvent: "+x+","+y);
                unitRect = getRect(x,y);
                isTouching =true;
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                unitRect = getRect(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isTouching =false;
                break;
        }

        setState(rectXIndex,rectYIndex);

        return true;
    }

    public void setMode(STATE_NODE state){
        this.state =state;
    }

    //clear matrix
    public void clearGrid(){
        initStateMatrix();
    }


    //dummy
    private void drawRect(Canvas canvas){

        for(ArrayList<RectF> eachRowRect:rectBoxes){
            for(RectF rect : eachRowRect){
                canvas.drawRect(rect,backgroundPaint);
            }
        }

    }
}
