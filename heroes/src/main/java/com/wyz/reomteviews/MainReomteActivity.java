package com.wyz.reomteviews;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RemoteViews;

import com.wyz.window.DialogWindowActivity;
import com.zzu.wyz.R;

public class MainReomteActivity extends Activity implements View.OnClickListener{

    Button button1;
    private static final String TAG = "MainReomteActivity";
    Button button2;
    Intent intent;
    private LinearLayout mRemoteViewsContent;
    private BroadcastReceiver mRemoteViewsReceicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: Action=========="+intent.getAction());
            RemoteViews remoteViews = intent.getParcelableExtra(MyConstants.EXTRA_REMOTE_VIEWS);
            if (remoteViews!=null){
                updeteUI(remoteViews);
            }
        }
    };

    private void updeteUI(RemoteViews remoteViews) {
        //View view = remoteViews.apply(this,mRemoteViewsContent);
        int layoutId = getResources().getIdentifier("layout_simulated_notification", "layout", getPackageName());
        View view = getLayoutInflater().inflate(layoutId, mRemoteViewsContent, false);
        remoteViews.reapply(this, view);
        mRemoteViewsContent.addView(view);
        Log.i(TAG, "updeteUI: 更新界面方法已经调用");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_reomte);
        initView();
    }

    private void initView() {
        mRemoteViewsContent = (LinearLayout) findViewById(R.id.remote_views_content);
        IntentFilter filter = new IntentFilter(MyConstants.REMOTE_ACTION);
        registerReceiver(mRemoteViewsReceicer,filter);
        Log.i(TAG, "initView: 广播注册成功");

        button1 = (Button) super.findViewById(R.id.btn_tzl);
        button2 = (Button) super.findViewById(R.id.btn_moni);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tzl:
                intent = new Intent(this, TZLShowActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_moni:
                intent = new Intent(this, TZL2Activity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mRemoteViewsReceicer);
        Log.i(TAG, "onDestroy: 广播已经注销");
        super.onDestroy();
    }
}
