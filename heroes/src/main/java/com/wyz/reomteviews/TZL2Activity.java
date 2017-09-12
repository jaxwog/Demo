package com.wyz.reomteviews;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.wyz.window.WindowTestActivity;
import com.zzu.wyz.R;

public class TZL2Activity extends AppCompatActivity {

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tzl2);
        Toast.makeText(this, getIntent().getStringExtra("sid"),
                Toast.LENGTH_SHORT).show();
        button = (Button) super.findViewById(R.id.btn_xbj);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initClick();
            }
        });
    }




    private void initClick() {

        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.layout_simulated_notification);
        remoteViews.setTextViewText(R.id.msg,"信息来自进程"+ Process.myPid());
        remoteViews.setImageViewResource(R.id.icon,R.drawable.icon1);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,0,new Intent(this, WindowTestActivity.class)
                ,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent open2PendingIntent = PendingIntent.getActivity(this,0
                ,new Intent(this,TZL2Activity.class),PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.item_holder,pendingIntent);
        remoteViews.setOnClickPendingIntent(R.id.open_activity2,open2PendingIntent);
        Intent intent = new Intent(MyConstants.REMOTE_ACTION);
        intent.putExtra(MyConstants.EXTRA_REMOTE_VIEWS,remoteViews);
        sendBroadcast(intent);

    }


}
