package com.zzu.wyz.activity;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.zzu.wyz.R;
import com.zzu.wyz.utils.WheelIndicatorItem;
import com.zzu.wyz.utils.WheelIndicatorView;

public class WaitUploadActivity extends AppCompatActivity implements View.OnClickListener{

    Button button;
    WheelIndicatorView wheelIndicatorView;
    WheelIndicatorItem runningActivityIndicatorItem;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    wheelIndicatorView.setFilledPercent(msg.arg1);
                    wheelIndicatorView.addWheelIndicatorItem(runningActivityIndicatorItem);
                default:
                    break;
            }

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_upload);


        button = (Button) findViewById(R.id.btn_upstart);
        button.setOnClickListener(this);

        wheelIndicatorView = (WheelIndicatorView) findViewById(R.id.wheel_indicator_view);

        runningActivityIndicatorItem = new WheelIndicatorItem(0.1f, getResources().getColor(R.color.own));



    }


    public void show(){
        runningActivityIndicatorItem.setColor(getResources().getColor(R.color.own));
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1;i<101;i++){
                    if (i==100){
                        runningActivityIndicatorItem.setColor(Color.GREEN);
                    }
                    SystemClock.sleep(100);
                    Message message = new Message();
                    message.what = 1;
                    message.arg1 = i;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_upstart:
                show();

                break;
                default:
                    break;
        }
    }




    private void initView() {

        //WheelIndicatorView wheelIndicatorView = new WheelIndicatorView(this);

        // dummy data
        float dailyKmsTarget = 4.0f; // 4.0Km is the user target, for example
        float totalKmsDone = 3.0f; // User has done 3 Km
        int percentageOfExerciseDone = (int) (totalKmsDone/dailyKmsTarget * 100); //




        // wheelIndicatorView.setFilledPercent(percentageOfExerciseDone);

        WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(1.8f, Color.parseColor("#ff9000"));
        WheelIndicatorItem walkingActivityIndicatorItem = new WheelIndicatorItem(0.9f, Color.argb(255, 194, 30, 92));
        WheelIndicatorItem runningActivityIndicatorItem = new WheelIndicatorItem(0.3f, getResources().getColor(R.color.my_wonderful_blue_color));

        wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
        wheelIndicatorView.addWheelIndicatorItem(walkingActivityIndicatorItem);
        wheelIndicatorView.addWheelIndicatorItem(runningActivityIndicatorItem);

        // Or you can add it as
        //wheelIndicatorView.setWheelIndicatorItems(Arrays.asList(runningActivityIndicatorItem,walkingActivityIndicatorItem,bikeActivityIndicatorItem));

        wheelIndicatorView.startItemsAnimation(); // Animate!
    }

}
