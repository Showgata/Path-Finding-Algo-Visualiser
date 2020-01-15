package com.kanuma.linechart;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

public class OptionView extends View {



    public OptionView(Context context) {
        super(context);
        init(context,null);
    }

    public OptionView(Context context,AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public OptionView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    public OptionView(Context context,AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    void init(Context context,AttributeSet attrs){


    }
}
