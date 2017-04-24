package com.zzu.wyz.activity;

import android.app.ActivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zzu.wyz.IActivity;
import com.zzu.wyz.R;

public class HandlerActivity extends AppCompatActivity implements IActivity,View.OnClickListener{
    private static final String TAG = "HandlerActivity";
    Button mButton;
 Handler handler;
   Looper looper;
   MessageQueue messageQueue;
   Message message;
   private ThreadLocal<Boolean> mBooleanThreadLocal = new ThreadLocal<Boolean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        initView();
        registerListener();

    }



    @Override
    public void initView() {
        mButton = (Button) super.findViewById(R.id.btn_handler);
    }

    @Override
    public void initData() {

        Log.d(TAG, "当前线程："+Thread.currentThread().getId()+"\n当前线程："+Thread.currentThread().getName());

        mBooleanThreadLocal.set(true);
        Log.d(TAG, "ThreadMain="+mBooleanThreadLocal.get());

        new Thread("Thread#1"){
            @Override
            public void run() {
                mBooleanThreadLocal.set(false);
                Log.d(TAG, "Thread#1="+mBooleanThreadLocal.get());
            }
        }.start();

        new Thread("Thread#2"){
            @Override
            public void run() {

                Log.d(TAG, "Thread#2="+mBooleanThreadLocal.get());
            }
        }.start();

    }

    @Override
    public void registerListener() {
        mButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_handler:
initData();
                break;
            default:
                break;
        }
    }
}
