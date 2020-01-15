package com.kanuma.linechart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

public class ChartView extends View {

    private Paint paint;
    private Paint textPaint;
    private Paint rectPaint;
    private Paint linePaint;

    private Path path;


    private int screenWidth;
    private int screenHeight;
    private int val=0;
    private Point p1,p2,p3;

    private boolean shouldAnimate =false;
    private GraphCanvasAvailable graphCanvasAvailable;

    public ChartView(Context context) {
        super(context);
        graphCanvasAvailable = (GraphCanvasAvailable) context;
        init(context,null);
    }

    public ChartView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ChartView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public ChartView(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    public void init(Context context,AttributeSet attrs){

        paint = new Paint();
        paint.setColor(Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        textPaint =new Paint();
        textPaint.setStrokeWidth(6);
        textPaint.setColor(Color.GRAY);
        textPaint.setTextSize(50f);

        rectPaint = new Paint();
        rectPaint.setColor(Color.BLUE);
        rectPaint.setStrokeWidth(5);

        linePaint = new Paint();
        linePaint.setColor(Color.MAGENTA);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        screenWidth = displayMetrics.widthPixels;
        screenHeight =displayMetrics.heightPixels;

        p1 = new Point(0,0);//for defining points
        p2 = new Point(screenWidth,0);
        p3 = new Point(screenWidth/2,screenHeight/2);

    }

    //Used for making the bars
    private int[] xAxisLabels ={2,4,6,8,10,12};
    private int[] yAxisLabels ={10,20,30,40,50,60};
    private int originX =250;
    private float sizeY =600;
    private float sizeX;
    private int labelTickSize = 20;
    private float startY ;
    private float endX ;
    private float originY ;
    private float offsetX;
    private float offsetY;
    private Map<Integer,Float> xAxisPoints=new HashMap<>();
    private Map<Integer,Float> yAxisPoints=new HashMap<>();
    private Path paths = new Path();

    void drawBackground(Canvas canvas){
        startY =screenHeight/3f;
        endX =screenWidth-originX;
        originY =startY+ sizeY;

        drawYAxis(canvas,yAxisLabels);
        drawXAxis(canvas,xAxisLabels);
        drawPointsAndPaths(canvas,xAxisPoints,yAxisPoints);

    }

    private void drawPointsAndPaths(Canvas canvas, Map<Integer, Float> xAxisPoints, Map<Integer, Float> yAxisPoints) {

        paths.moveTo(originX,originY);

        for(int i=0;i<=xAxisLabels.length-1;i++){
            for(int j=0;j<=yAxisLabels.length-1;j++){
                if (i == j) {
                    canvas.drawCircle(xAxisPoints.get(xAxisLabels[i]).intValue(),yAxisPoints.get(yAxisLabels[j]).intValue(),20,rectPaint);
                    paths.lineTo(xAxisPoints.get(xAxisLabels[i]).intValue(),yAxisPoints.get(yAxisLabels[j]).intValue());
                    canvas.drawPath(paths,linePaint);
                }
            }
        }
        paths.close();

    }

    private void drawYAxis(Canvas canvas, int[] yAxisLabels) {

        canvas.drawLine(originX,startY, originX,originY,textPaint);

        offsetY = sizeY /yAxisLabels.length;
        float currentPosition=originY;
        yAxisPoints.put(0,currentPosition);

        float xTickStart = originX -labelTickSize;
        //float xTickEnd = originX +labelTickSize;
        float xTickEnd = endX;
        float yAxisLabelPos = xTickStart -80;

        for(int i=0;i<yAxisLabels.length;i++){
            currentPosition=currentPosition -offsetY;
            yAxisPoints.put(yAxisLabels[i],currentPosition);
            canvas.drawLine(xTickStart,currentPosition,xTickEnd ,currentPosition,paint);
            canvas.drawText(String.valueOf(yAxisLabels[i]),yAxisLabelPos,currentPosition+15,textPaint);
        }

    }

    private void drawXAxis(Canvas canvas, int[] xAxisLabels) {

        sizeX = endX-originX;
        canvas.drawLine(originX,originY, endX+20,originY,textPaint);
        offsetX = sizeX /xAxisLabels.length;
        float currentPosition=originX;
        xAxisPoints.put(0,currentPosition);

        //float yTickStart = originY-labelTickSize;
        float yTickStart = startY;
        float yTickEnd = originY+labelTickSize;
        float xAxisLabelPos = yTickEnd +50;

        for(int i=0;i<xAxisLabels.length;i++){
            currentPosition=currentPosition +offsetX;
            xAxisPoints.put(xAxisLabels[i],currentPosition);
            canvas.drawLine(currentPosition ,yTickStart, currentPosition ,yTickEnd,paint);
            canvas.drawText(String.valueOf(xAxisLabels[i]),currentPosition-15,xAxisLabelPos,textPaint);
        }

    }


    void drawPaths(Canvas canvas){

        path = new Path();//for polygonal shapes
        path.moveTo(p1.x,p1.y);
        path.cubicTo(p1.x,p1.y,p2.x,p2.y, p3.x,p3.y);
        //path.lineTo(p3.x,p3.y);
        path.close();

        canvas.drawPath(path,paint);

    }


    private Path rectBar;
    void drawRectangleBars(Canvas canvas){

        rectBar = new Path();
        //top
        rectBar.moveTo(originX,startY);
        rectBar.lineTo(originX+offsetX,startY);

        //bottom
        rectBar.lineTo(originX+offsetX,originY);
        rectBar.lineTo(originX,originY);
        rectBar.close();

        canvas.drawPath(rectBar,rectPaint);

    }



//    void drawRectangle(Canvas canvas){
//
//        rect = new Rect(50,val,screenWidth/5,screenHeight/2);
//        canvas.drawRect(rect,paint);
//
//
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //drawRectangle(canvas);
        drawBackground(canvas);
        //drawRectangleBars(canvas);
    }

    public void startAnimating() {

        if(shouldAnimate) {
            ValueAnimator animator = ValueAnimator.ofInt(0,600);
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y= (int) event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //shouldAnimate =true;
                //startAnimating();
                p2.set(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                p2.set(x,y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


    interface GraphCanvasAvailable{
        void plotPoint(Map xAxisPoints,Map yAxisPoints);
    }
}
