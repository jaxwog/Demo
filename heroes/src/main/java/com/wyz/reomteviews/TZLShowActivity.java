package com.wyz.reomteviews;


import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;


import com.wyz.window.WindowTestActivity;
import com.zzu.wyz.R;

public class TZLShowActivity extends AppCompatActivity implements View.OnClickListener{


    Button button1;
    Button button2;
    private static int sId = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzlshow);
        initView();
    }

    private void initView() {
        button1 = (Button) super.findViewById(R.id.btn_normal);
        button2 = (Button) super.findViewById(R.id.btn_custom);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_normal:
                showNormal();
                break;
            case R.id.btn_custom:
                showCustom();
                break;
            default:

                break;
        }
    }



    @android.support.annotation.RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showNormal() {
        sId++;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, WindowTestActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notify= new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("TickerText:" + "王永政的消息！")// 设置在status
                .setContentTitle("Notification Title")
                .setContentText("This is the notification message")
                .setContentIntent(pendingIntent)
                //.setNumber(1)
                .build();
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(sId, notify);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showCustom() {
        sId++;
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this,WindowTestActivity.class);
        intent.putExtra("sid", "" + sId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.layout_notification);
        remoteViews.setTextViewText(R.id.msg,"王永政");
        remoteViews.setImageViewResource(R.id.icon,R.drawable.icon1);
        PendingIntent open2PendingIntent = PendingIntent.getActivity(this,0,
                new Intent(this,TZL2Activity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.open_activity2,open2PendingIntent);
        Notification notify= new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("TickerText:" + "王永政的消息！")// 设置在status
                .setContentTitle("Notification Title")
                .setContentText("This is the notification message")
                .setContentIntent(pendingIntent)
                .build();
        notify.contentView = remoteViews;
        notify.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(sId, notify);
    }

}
