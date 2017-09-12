package com.wyz.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Created by wangyongzheng on 2017/5/2.
 */


@SuppressLint("AppCompatCustomView")
public class TestButton extends TextView {

    private static final String TAG = "TestButton";
    private int touchSlopNum;
    Scroller mScroller = new Scroller(getContext());

    private int mLastX = 0;
    private int mLastY = 0;

    public TestButton(Context context) {
        super(context);
        init();
    }



    public TestButton(Context context,  AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TestButton(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        touchSlopNum = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        Log.d(TAG, "最小滑动距离: "+touchSlopNum);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Thread.dumpStack();


        //获取当前坐标的在屏幕中的位置
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();

        //触摸点相对于当前控件的位置
        int touchX = (int) event.getX();
        int touchY = (int)event.getY();

        Log.d(TAG, "onTouchEvent: 相对于屏幕位置：x = "+x+"; y = "+y);
        Log.d(TAG, "onTouchEvent: 相对于当前控件位置：touch = "+touchX+"; touchY = "+touchY);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "手触摸了屏幕: ###########################");

                break;

            case MotionEvent.ACTION_MOVE:
                //获取移动距离
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                Log.d(TAG, "onTouchEvent: 移动距离：deltaX = "+deltaX+"；deltaY = "+deltaY);

                int translationX = (int) (getTranslationX()+deltaX);
                int translationY = (int) (getTranslationY()+deltaY);

                Log.d(TAG, "onTouchEvent: translationX = "+translationX+";translationY = "+translationY);

                setTranslationX(translationX);
                setTranslationY(translationY);

                break;

            case MotionEvent.ACTION_UP:
                Log.d(TAG, "手离开了屏幕: ###########################");
                break;

            default:
                    break;
        }

        mLastX = x;
        mLastY = y;
        //返回TRUE进行事件消耗，无法传递到onclick执行
        return super.onTouchEvent(event);
        //return true;
    }


    private void smoothScrollTo(int dx, int dy) {
        int  scrollX = getScrollX();
        int delta = dx - scrollX;
        //500ms内滑向dx，效果就是慢慢滑动
        mScroller.startScroll(scrollX , 0, delta, 0, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
        super.computeScroll();

    }
}
