package com.wyz.ipc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ThirdActivity extends AppCompatActivity  implements View.OnClickListener{

    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        initView();
    }

    private void initView() {
        mButton = (Button) super.findViewById(R.id.btn_third);
        mButton.setOnClickListener(this);
    }

    private void initEvent() {
        Intent intent = new Intent();

        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_third:
                initEvent();
                break;
            default:
                break;
        }
    }
}
