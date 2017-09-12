package com.wyz.animation;

import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zzu.wyz.R;

public class GetAndSetActivity extends AppCompatActivity {
    private static final String TAG = "GetAndSetActivity";

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_and_set);
        mButton = (Button) findViewById(R.id.button_move);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // performAnimate2();
                performAnimate3(mButton,mButton.getWidth(),30);
            }
        });

    }



    private void performAnimate2() {
        ViewWrapper2 wrapper = new ViewWrapper2(mButton);
        Log.i(TAG, "performAnimate: mButton.getWidth()"+mButton.getWidth());
        ObjectAnimator.ofInt(wrapper,"width",mButton.getWidth(),700).setDuration(5000).start();
    }

    private static class ViewWrapper2{
        private View mTarget;

        public ViewWrapper2(View target){
            mTarget = target;
        }

        public int getWidth(){
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width){
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

    }


    private void performAnimate3(final View targer, final int start, final int end) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0,100);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            //持有一个IntEvaluator对象，方便下面估值的时候使用
            private IntEvaluator mEvaluator = new IntEvaluator();
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获得当前动画的进度值，整型，1~100之间
                int currentValue = (Integer) animation.getAnimatedValue();
                Log.i(TAG, "onAnimationUpdate: currentValue="+currentValue);
                //获得当前进度占整个动画过程的比例，浮点型，0~1之间
                float fraction = animation.getAnimatedFraction();
                //直接调用整型估值器，通过比例计算出宽度，然后在设置给View
                targer.getLayoutParams().width = mEvaluator.evaluate(fraction,start,end);
                targer.requestLayout();
            }
        });
        valueAnimator.setDuration(5000).start();
    }




}
