package com.zzu.wyz.activity;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zzu.wyz.R;
import com.zzu.wyz.utils.AutoText;
import com.zzu.wyz.utils.PaintValidationView;

public class ValidationActivity extends AppCompatActivity implements View.OnClickListener{


    PaintValidationView paintValidationView;
    Button button;
    AutoText autoText ;

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        paintValidationView.setProgress((float) (msg.arg1));

                    default:
                        break;
                }

            }
        };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);


        paintValidationView = (PaintValidationView) findViewById(R.id.paint_view);
        button = (Button) findViewById(R.id.btn_start);
        autoText = (AutoText) super.findViewById(R.id.tv_auto);
        button.setOnClickListener(this);

        paintValidationView.setProgress(0.0f);


    }

   public void show(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 1;i<101;i++){
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
            case R.id.btn_start:
                show();
                break;
                default:
                    break;
        }
    }
}
