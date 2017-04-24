package com.wyz.ipc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Messenger;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button mButton;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mButton = (Button) super.findViewById(R.id.btn_first);
        mButton.setOnClickListener(this);
        UserManager.sUserId = 2;
        Log.d(TAG, "initView: sUserId = "+ UserManager.sUserId);
    }

    private void initEvent() {
        Intent intent = new Intent();
        intent.setClass(this,SecondActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_first:
                initEvent();
                break;
                default:
                    break;
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onResume() {

        this.persistToFile();
        super.onStart();

    }

    public void persistToFile(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserBean userBean = new UserBean(1,"wyz",true);
                Log.d(TAG, "run: "+userBean.toString());
                File dir = new File(MyConstants.IPC_PATH);
                if (!dir.exists()){
                    dir.mkdirs();
                }
                File file = new File(MyConstants.FILE_PATH);
                ObjectOutputStream objectOutputStream = null;
                try {
                    objectOutputStream = new ObjectOutputStream(new FileOutputStream(file));
                    objectOutputStream.writeObject(userBean);
                    Log.d(TAG, "persistToFile:"+userBean.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    MyUtils.closeFile(objectOutputStream);

                }
            }
        }).start();
    }
}
