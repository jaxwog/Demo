package com.wyz.ipc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.wyz.bean.UserBean;
import com.wyz.utils.MyConstants;
import com.wyz.utils.MyUtils;
import com.wyz.utils.UserManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class SecondActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "SecondActivity";
    Button mButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    private void initView() {
        mButton = (Button) super.findViewById(R.id.btn_second);
        mButton.setOnClickListener(this);
        Log.d(TAG, "initView:sUserId = "+ UserManager.sUserId);
    }

    private void initEvent() {
        Intent intent = new Intent();
        intent.setClass(this,ThirdActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_second:
                initEvent();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        receveFromFile();
    }

    public void receveFromFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBean userBean = null;
                File file = new File(MyConstants.FILE_PATH);
                ObjectInputStream objectInputStream = null;
                try {
                    objectInputStream = new ObjectInputStream(new FileInputStream(file));
                    userBean = (UserBean) objectInputStream.readObject();
                    Log.d(TAG, "receveFromFile"+userBean.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }finally {
                    MyUtils.closeFile(objectInputStream);
                }

            }
        }).start();
    }
}
