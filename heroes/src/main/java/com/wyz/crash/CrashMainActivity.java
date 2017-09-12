package com.wyz.crash;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.wyz.window.WindowTestActivity;
import com.zzu.wyz.R;
import com.zzu.wyz.activity.WaitUploadActivity;

public class CrashMainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;
    private static final String TAG = "CrashMainActivity";
    private static Context mContext;
    private static View mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crash_main);
        //mContext = this;
        //mView = new View(this);
        initView();
       // SystemClock.sleep(30*1000);

//        for(int i=0;i<Integer.MAX_VALUE;i++){
//            Log.i(TAG, "onCreate: i = "+i);
//        }
    }

    private void initView() {
//        ((ViewStub)findViewById(R.id.stub_import)).setVisibility(View.VISIBLE);
//        View importPanel = ((ViewStub)findViewById(R.id.stub_import)).inflate();
        button = (Button) super.findViewById(R.id.crash_test);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==button){

            // startActivity(new Intent(this, WindowTestActivity.class));
          // throw new RuntimeException("自定义异常：自己抛出的异常");
        }
    }

}
