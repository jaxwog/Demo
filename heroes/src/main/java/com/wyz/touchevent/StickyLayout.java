package com.wyz.touchevent;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by wangyongzheng on 2017/5/16.
 */

public class StickyLayout extends LinearLayout {
    private static final String TAG = "StickyLayout";
    private  int mTouchSlop;
    //记录上一次滑动坐标
    private int mLastX = 0;
    private int mLastY = 0;
    //记录上一次滑动坐标（Intercept）
    private int mLastXIntercept = 0;
    private int mLastYIntercept = 0;
    private int mHeaderHeight;
    private int mOriginalHeaderHeight;

    private boolean mIsSticky = true;
    private boolean mDisallowInterceptTouchEventOnHeader = true;

    private int mStatus = STATUS_EXPANDED;
    public static final int STATUS_EXPANDED = 1;
    public static final int STATUS_COLLAPSED = 2;
    private int headerHeight;



    public interface OnGiveUpTouchEventListener {
        public boolean giveUpTouchEvent(MotionEvent event);
    }

    private OnGiveUpTouchEventListener mGiveUpTouchEventListener;

    public void setOnGiveUpTouchEventListener(OnGiveUpTouchEventListener l) {
        mGiveUpTouchEventListener = l;
    }

    public StickyLayout(Context context) {
        super(context);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public StickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int intercept = 0;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mLastXIntercept = x;
                mLastYIntercept = y;
                intercept = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                int delatX = x - mLastXIntercept;
                int delatY  = y - mLastYIntercept;
                //当点击事件在heard顶部时（不在触摸范围）不会拦截事件
                if (mDisallowInterceptTouchEventOnHeader && y <= getHeaderHeight()){
                    intercept = 0;
                //当水平方向滑动时，不会拦截事件
                }else if (Math.abs(delatX) >= Math.abs(delatY)){
                    intercept = 0;
                    //当heard处于展开状态，并且是向上滑动
                }else if (mStatus == STATUS_EXPANDED && delatY <= -mTouchSlop){
                    intercept = 1;
                }else if (mGiveUpTouchEventListener!=null){
                    //ListView滑动到顶部了，并且向下滑动时；giveUpTouchEvent是一个接口方法，用来判断lsitview是否滑动到顶端
                    if (mGiveUpTouchEventListener.giveUpTouchEvent(ev) && delatY >= mTouchSlop){
                        intercept = 1;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                intercept = 0;
                mLastXIntercept = mLastYIntercept = 0;
                break;
            default:
                break;
        }
        return intercept != 0 && mIsSticky;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mIsSticky) {
            return true;
        }
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int delatX = x - mLastX;
                int delatY = y - mLastY;
                //根据滑动距离设置Head的高度
                mHeaderHeight += delatY;
                setHeaderHeight(mHeaderHeight);
                break;
            case MotionEvent.ACTION_UP:
                int destHeight = 0;
                //根据滑动的距离判断是否显示或隐藏head
                if (mHeaderHeight <= mOriginalHeaderHeight * 0.5){
                    destHeight = 0;
                    mStatus = STATUS_COLLAPSED;
                }else {
                    destHeight = mOriginalHeaderHeight;
                    mStatus = STATUS_EXPANDED;
                }
                //弹性动画效果实现显示与隐藏
                this.smoothSetHeaderHeight(mHeaderHeight,destHeight,500);
                break;
            default:
                break;
        }
        mLastY = y;
        mLastX = x;
        return true;
    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }
    public void setHeaderHeight(int headerHeight) {

    }
    public void smoothSetHeaderHeight(final int from, final int to, long duration) {

    }


}
