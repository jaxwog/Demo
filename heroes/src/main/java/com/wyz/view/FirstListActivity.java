package com.wyz.view;


import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wyz.utils.TestButton;
import com.zzu.wyz.R;

public class FirstListActivity extends AppCompatActivity implements View.OnClickListener{

    private TestButton testButton;
    private Button button;
    private static final String TAG = "FirstListActivity";
    private static int num  = 0;
Activity activity;

    private static final int MESSAGE_SCROLL_TO = 1;
    private static final int FRAME_COUNT = 100;
    private static final int DELAYED_TIME = 33;

    private int count = 0 ;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MESSAGE_SCROLL_TO:
                    count++;
                    if (count<=FRAME_COUNT){
                        float fraction = count/(float)FRAME_COUNT;
                        int scrollX = (int)(-fraction*100);
                        button.scrollTo(scrollX,0);
                        mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);
                    }


                break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
      View decorView =   this.getWindow().getDecorView();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_list);
        View view = getWindow().getDecorView().findViewById(android.R.id.content).getRootView();

        testButton = (TestButton) findViewById(R.id.test_button);
        testButton.setOnClickListener(this);
        button = (Button) findViewById(R.id.btn_animal);
        button.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_button:
                Log.d(TAG, "onClick: ######################");
                break;
            case R.id.btn_animal:

                count = 0 ;
                mHandler.sendEmptyMessageDelayed(MESSAGE_SCROLL_TO,DELAYED_TIME);

                 //moveParamsContent();

               // moveAnimatorContent();

                //moveObjectContent();
                break;

                default:
                    break;
        }
    }

    //使用属性动画移动，移动的整个View及事件处理
    private void moveObjectContent(){
        if (num>500){
            num = 0;
        }
        ObjectAnimator.ofFloat(button,"translationX",num,(100+num)).setDuration(1000).start();
        num+=100;

    }

    private void moveAnimatorContent(){
        final int startX = 0;
         final int deltaX = 100;
        final ValueAnimator animator = ValueAnimator.ofInt(0,1).setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
               float fraction = animator.getAnimatedFraction();
               button.scrollTo(startX+(int) (deltaX * fraction),0);
            }
        });
        animator.start();
    }


    private void moveParamsContent(){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
        params.width+=100;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            params.setMarginStart(params.leftMargin+=100);
        }
        button.setLayoutParams(params);
    }

//boolean onInterceptTouchEvent;
//事件分发机制，伪代码
//    public boolean dispatchTouchEvent(MotionEvent event){
//        boolean consume = false;
//        if (onInterceptTouchEvent){
//            consume = onTouchEvent(event);
//        }else {
//            consume = child.dispatchTouchEvent(event);
//        }
//        return consume;
//    }


}
