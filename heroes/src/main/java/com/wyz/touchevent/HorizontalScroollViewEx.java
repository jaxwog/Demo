package com.wyz.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * 外部拦截法使用
 * Created by wangyongzheng on 2017/5/11.
 */

public class HorizontalScroollViewEx extends ViewGroup{
    private static final String TAG = "HorizontalScroollViewEx";

    private int mChildrenSize;
    private int mChildIndex;
    private int mChildWidth;
    //分别记录上次滑动坐标（onInterceptTouchEvent）
   private int mLastXintercept = 0;
   private int mLastYintercept = 0;
   //分别记录上次滑动坐标
   private int mLastX = 0;
   private  int mLastY = 0;
    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public HorizontalScroollViewEx(Context context) {
        super(context);
        init();
    }

    public HorizontalScroollViewEx(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalScroollViewEx(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }



    //初始化Scroller弹性动画和VelocityTracker滑动速率
    private void init() {
        mScroller = new Scroller(getContext());
        mVelocityTracker = VelocityTracker.obtain();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = 0;
        int measureHeight = 0;
        final int childCount = getChildCount();
        measureChildren(widthMeasureSpec,heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (childCount==0){
            setMeasuredDimension(0,0);
        }else {
            if (widthSpecMode==MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST){
                final View childView = getChildAt(0);
                measureWidth = childView.getMeasuredWidth()*childCount;
                measureHeight = childView.getMeasuredHeight();
                setMeasuredDimension(measureWidth,measureHeight);
            }else if (widthSpecMode == MeasureSpec.AT_MOST){
                final View childView = getChildAt(0);
                measureWidth = childView.getMeasuredWidth()*childCount;
                setMeasuredDimension(measureWidth,heightSpecSize);
            }else if (heightSpecMode == MeasureSpec.AT_MOST){
                final View childView = getChildAt(0);
                measureHeight = childView.getMeasuredHeight();
                setMeasuredDimension(widthSpecSize,measureHeight);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;
        final int childCount = getChildCount();
        mChildrenSize = childCount;

        for (int i = 0; i < childCount; i++) {
            final View childView = getChildAt(i);
            if (childView.getVisibility() != View.GONE){
                final int childWidth = childView.getMeasuredWidth();
                mChildWidth = childWidth;
                childView.layout(childLeft,0,childLeft+childWidth,childView.getMeasuredHeight());
                childLeft += childWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercepted = false;

        //记录触摸点的坐标
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //如果为true事件不会传递给子View
                intercepted = false;
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                    intercepted = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastXintercept;
                int deltaY = y - mLastYintercept;
                //flag为判断条件，父容器是否需要拦截事件自己处理
                if (Math.abs(deltaX)>Math.abs(deltaY)){
                    intercepted = true;
                }else {
                    intercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
            default:
                    break;
        }
        mLastX = x;
        mLastY = y;
        mLastXintercept = x;
        mLastYintercept = y;
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //该方法执行父组件任务，父组件只支持左右滑动，该事件处理左右滑动，不处理子类事件
        mVelocityTracker.addMovement(ev);
        //记录触摸点的坐标
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()){
                    mScroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int delatX = x - mLastX;
                int delatY = y - mLastY;
                scrollBy(-delatX,0);//传递负数则是向指定的方向进行滑动，参考View的滑动
                break;
            case MotionEvent.ACTION_UP:
                //当前view的左上角相对于母视图的左上角的X轴偏移量
                int scrollX = getScrollX();
                int scrollToChildIndex = scrollX / mChildWidth;
                mVelocityTracker.computeCurrentVelocity(1000);
                float xVelocity = mVelocityTracker.getXVelocity();
                if (Math.abs(xVelocity)>=50){
                    mChildIndex = xVelocity>0 ? mChildIndex - 1 : mChildIndex + 1;
                }else {
                    mChildIndex = (scrollX + mChildWidth / 2) / mChildWidth;
                }
                //限制mChildIndex大小，取值范围为0~mChildrenSize-1
                mChildIndex = Math.max(0, Math.min(mChildIndex,mChildrenSize-1));
                int dx = mChildIndex * mChildWidth - scrollX;
                smoothScroller(dx,0);
                mVelocityTracker.clear();
                break;
            default:
                break;
        }
        mLastX = x;
        mLastY = y;
        return true;
    }


    private void smoothScroller(int dx,int dy) {
        mScroller.startScroll(getScrollX(),0,dx,0,500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        mVelocityTracker.recycle();
        super.onDetachedFromWindow();
    }




}
