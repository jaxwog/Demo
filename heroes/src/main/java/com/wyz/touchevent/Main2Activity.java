package com.wyz.touchevent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.job.JobScheduler;
import android.os.Build;
import android.os.PowerManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Toast;

import com.zzu.wyz.R;

public class Main2Activity extends AppCompatActivity {


    private Button mButton;
    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
JobScheduler jobScheduler;//android5.0以上使用
PowerManager powerManager;

        mButton = (Button) findViewById(R.id.id_btn);

        mButton.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                int action = event.getAction();

                switch (action)
                {
                    case MotionEvent.ACTION_DOWN:
//                        Log.e(TAG, "onTouch ACTION_DOWN");
                        break;
                    case MotionEvent.ACTION_MOVE:
//                        Log.e(TAG, "onTouch ACTION_MOVE");
                        break;
                    case MotionEvent.ACTION_UP:
//                        Log.e(TAG, "onTouch ACTION_UP");
                        break;
                    default:
                        break;
                }

                return false;
            }
        });
//        mButton.setOnClickListener(new View.OnClickListener()
//        {
//            @SuppressLint("WrongConstant")
//            @Override
//            public void onClick(View v)
//            {
//                Toast.makeText(getApplicationContext(), "onclick",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mButton.setOnLongClickListener(new View.OnLongClickListener()
//        {
//            @SuppressLint("WrongConstant")
//            @Override
//            public boolean onLongClick(View v)
//            {
//                Toast.makeText(getApplicationContext(), "setOnLongClickListener",Toast.LENGTH_SHORT).show();
//                return false;
//            }
//        });

//        @SuppressLint("WrongConstant")
//        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);
//        @SuppressLint("WrongConstant")
//        int heigthMeasureSpec = View.MeasureSpec.makeMeasureSpec(100, View.MeasureSpec.EXACTLY);


        @SuppressLint("WrongConstant")
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1<<30)-1, View.MeasureSpec.AT_MOST);
        @SuppressLint("WrongConstant")
        int heigthMeasureSpec = View.MeasureSpec.makeMeasureSpec((1<<30)-1, View.MeasureSpec.AT_MOST);
        mButton.measure(widthMeasureSpec,heigthMeasureSpec);
        mButton.getHeight();
        mButton.getMeasuredHeight();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            int width = mButton.getMeasuredWidth();
            Log.d(TAG, "onWindowFocusChanged: width=="+width);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        ViewTreeObserver observer = mButton.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mButton.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width = mButton.getMeasuredWidth();
            }
        });

//        mButton.post(new Runnable() {
//            @Override
//            public void run() {
//                int width = mButton.getMeasuredWidth();
//            }
//        });
    }
}
