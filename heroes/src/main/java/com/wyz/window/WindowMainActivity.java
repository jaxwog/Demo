package com.wyz.window;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import com.zzu.wyz.R;

public class WindowMainActivity extends AppCompatActivity implements View.OnClickListener{

    Button button1;
    Button button2;
    Intent intent;
    ServiceConnection serviceConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_window_main);
        button1 = (Button) super.findViewById(R.id.win_add);
        button2 = (Button) super.findViewById(R.id.win_toast);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        serviceConnection= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {

            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.win_add:
                intent = new Intent(this,WindowTestActivity.class);
                startActivity(intent);
                break;
            case R.id.win_toast:
                intent = new Intent(this,DialogWindowActivity.class);
                startActivity(intent);
                break;

                default:

                    //启动、绑定服务
                    //intent = new Intent(this,MyService.class);
                    //startService(intent);
                    //bindService(intent,serviceConnection,BIND_AUTO_CREATE);

                    //动态注册广播
//                    IntentFilter filter = new IntentFilter();
//                    filter.addAction("com.wyz.receiver.LAUNCH");
//                    registerReceiver(new MyReceiver(),filter);
//
//                    intent = new Intent();
//                    intent.setAction("com.wyz.receiver.LAUNCH");
//                    intent.setFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
//                    intent.setFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
//                    sendBroadcast(intent);


                    break;
        }
    }
}
