package com.wyz.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by wangyongzheng on 2017/5/11.
 */

public class ListViewEx extends ListView {
    private static final String TAG = "ListViewEx";
    HorizontalScroollViewEx2 horizontalScroollViewEx;
    //分别记录上次滑动的坐标
    int mLastX;
    int mLastY;
    boolean flag;
    public ListViewEx(Context context) {
        super(context);
    }

    public ListViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHorizontalScroollViewEx2( HorizontalScroollViewEx2 horizontalScroollViewEx2){
        this.horizontalScroollViewEx = horizontalScroollViewEx2;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int  x = (int) ev.getX();
        int  y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //horizontalScroollViewEx为父容器的引用
                horizontalScroollViewEx.requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                //如果父容器需要此类点击事件flag = true
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                   horizontalScroollViewEx.requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
                default:
                    break;
        }
        mLastX = x;
        mLastY = y;
        return super.dispatchTouchEvent(ev);
    }


}
