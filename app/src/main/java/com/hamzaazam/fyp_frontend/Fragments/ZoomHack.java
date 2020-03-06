package com.hamzaazam.fyp_frontend.Fragments;

import android.content.Context;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class ZoomHack extends ViewPager {


    public ZoomHack(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            //uncomment if you really want to see these errors
            //e.printStackTrace();
            return false;
        }
    }
}
