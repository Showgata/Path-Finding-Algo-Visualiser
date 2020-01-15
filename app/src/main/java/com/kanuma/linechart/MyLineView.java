package com.kanuma.linechart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PathMeasure;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class MyLineView extends View{

    ValueAnimator animation = null;

    private Paint paint;
    private Paint textPaint;
    private Paint highlightPaint;
    private boolean turn =true;

    private OnSquareClickListener squareClickListener;

    private int screenWidth;
    private int screenHeight;

    public static final float X_PART1=1/3f;
    public static final float Y_PART1=1/3f;

    private boolean touching =false;
    private Pair<Integer,Integer> touchedRectIndex;

    private Rect[][] squares = new Rect[3][3];
    private String[][] squareStrings = new String[3][3];

    public static final String moveX ="X";
    public static final String moveO ="O";

    private boolean shouldAnimate =false;

    private Path path = new Path();
    int val;

    private static final String TAG = "MyLineView";


    public MyLineView(Context context) {
        super(context);
        init(context,null);
    }

    public MyLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public MyLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public MyLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    Path dPath;
    Paint dPaint;

    public void init(Context context,AttributeSet attrs){

        /*

        //dummy
        dPaint = new Paint();
        dPaint.setColor(Color.BLACK);
        dPaint.setStrokeWidth(20);


        dPath = new Path();
        dPath.moveTo(0,0);
        dPath.lineTo(500,500);
        dPath.close();
        */

        DisplayMetrics displayMetrics = new DisplayMetrics();
        initArray(squareStrings);
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight =displayMetrics.heightPixels;

        //border styling
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);

        //text styling
        textPaint =new Paint();
        textPaint.setStrokeWidth(6);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(400f);

        highlightPaint = new Paint();
        highlightPaint.setColor(Color.GRAY);

        //init onsquarelistener
        squareClickListener = (OnSquareClickListener) context;

        /*
        rect = new RectF(screenWidth/4,screenHeight/3,screenWidth/6,screenHeight/2); //predefine shapes


        Point p1 = new Point(0,0);//for defining points
        Point p2 = new Point(screenWidth,0);
        Point p3 = new Point(screenWidth/2,screenHeight/2);

        path = new Path();//for polygonal shapes
        path.moveTo(p1.x,p1.y);
        path.lineTo(p2.x,p2.y);
        path.lineTo(p3.x,p3.y);
        path.close();
        */

        int xUnit = (int) (screenWidth*X_PART1);
        int yUnit = (int) (screenHeight*Y_PART1);

        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                squares[i][j] = new Rect(i*xUnit,j*yUnit,(i+1)*xUnit,(j+1)*yUnit);
            }
        }

    }

    private void initArray(String[][] squareStrings) {
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                squareStrings[i][j]="";
            }
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBorders(canvas);
        drawSquareStates(canvas);

        if(shouldAnimate){
            drawDashedPath(canvas,val);
        }

        if(touching){
            //drawTextInsideRect(canvas,touchedRect,"X");
            drawHighlight(canvas);
        }

    }

    private void drawBorders(Canvas canvas) {

        canvas.drawLine(screenWidth*X_PART1,0f,screenWidth*X_PART1,screenHeight,paint);
        canvas.drawLine(screenWidth*X_PART1*2,0f,screenWidth*X_PART1*2,screenHeight,paint);
        canvas.drawLine(0f,screenHeight*Y_PART1,screenWidth,screenHeight*Y_PART1,paint);
        canvas.drawLine(0f,screenHeight*Y_PART1*2,screenWidth,screenHeight*Y_PART1*2,paint);

    }


    public void drawTextInsideRect(Canvas canvas,Rect rect,String str){

        float xStr =textPaint.measureText(str)* 0.5f;//width of the text
        float yStr =textPaint.getFontMetrics().ascent * -0.5f;//height above the base line

        float textX = rect.exactCenterX()-xStr;
        float textY = rect.exactCenterY()+yStr;

        canvas.drawText(str,textX,textY,textPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touching =true;
                touchedRectIndex=getRect(x,y);
                invalidate(squares[touchedRectIndex.first][touchedRectIndex.second]);
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                touching =false;
                //invalidate(squares[touchedRectIndex.first][touchedRectIndex.second]);
                invalidate();

                Pair<Integer, Integer> finalPos = getRect(x,y);
                if(finalPos.first.equals(touchedRectIndex.first) && finalPos.second.equals(touchedRectIndex.second)) {
                    squareClickListener.onSquareListener(finalPos.first, finalPos.second);

                    if(turn) drawXAtPosition(touchedRectIndex.first,touchedRectIndex.second);
                    else drawOAtPosition(touchedRectIndex.first,touchedRectIndex.second);
                    turn=!turn;
                }

                break;
        }

        return true;
    }

    private Pair<Integer, Integer> getRect(int x, int y){


        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(squares[i][j].contains(x,y)){
                    return new Pair<>(i,j);
                }
            }
        }

        return new Pair<>(-1,-1);
    }


    private void drawHighlight(Canvas canvas){
        canvas.drawRect(squares[touchedRectIndex.first][touchedRectIndex.second],highlightPaint);
    }

    private void drawXAtPosition(int x,int y){
        squareStrings[x][y] = moveX;
        invalidate(squares[x][y]);
    }

    private void drawOAtPosition(int x,int y){
        squareStrings[x][y] = moveO;
        invalidate(squares[x][y]);
    }

    private void drawSquareStates(Canvas canvas){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(!squareStrings[i][j].equals("")){
                    drawTextInsideRect(canvas,squares[i][j],squareStrings[i][j]);
                }
            }
        }
    }



    interface OnSquareClickListener{
        void onSquareListener(int x,int y);
    }




    public void animateWin(int x1,int y1,int x2,int y2){

        //int[] winCoordinates = getWinCoordinates(x1,y1,x2,y2);

        float centerX1 = squares[0][0].exactCenterX();
        float centerY1 = squares[0][0].exactCenterY();
        float centerX2 = squares[2][2].exactCenterX();
        float centerY2 = squares[2][2].exactCenterY();

        shouldAnimate=true;
        startAnimating();



        invalidate();
    }

    public void startAnimating() {

        if(shouldAnimate) {
            ValueAnimator animator = ValueAnimator.ofInt(0, 1000);
            animator.setDuration(10000);
            animator.start();

            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    val = (int) animation.getAnimatedValue();
                    invalidate();
                }
            });

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    shouldAnimate =false;
                }
            });


        }
    }

    private int[] getWinCoordinates(int x1, int y1, int x2, int y2) {

        int[] dummyCoordinates = {0,0,1,1};

        return dummyCoordinates;
    }

    private void measurePath(){
        PathMeasure measure=new PathMeasure(path,false);
        int length = (int) measure.getLength();
    }

    private void drawDashedPath(Canvas canvas,int val){

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);

        PathEffect effect = new DashPathEffect(new float[]{2,val},val);
        paint.setPathEffect(effect);

        canvas.drawLine(0,40f,screenWidth,screenHeight-40f,paint);

    }


}
