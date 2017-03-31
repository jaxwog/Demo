package zzu.wyz.scan;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import zzu.wyz.util.LinkMan;
import zzu.wyz.util.MetaDataInterface;
import zzu.wyz.util.MyXMLPullUtil;

public class HopperUpdataActivity extends AppCompatActivity {


    private TextView showExtra;
    private ListView listView;
    private EditText hopperNum;
    private EditText hopperUpdata;
    private Button submit;
    private SimpleAdapter simpleAdapter;
    private File file;
    private ImageButton hopperBack;
    private String orderStr;
    private EditText editOrderNumber;

    private String infoExtra, dataEdit, updataEdit, longTime;
    private List<Map<String, String>> allList = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_hopper);


        this.editOrderNumber = (EditText) super.findViewById(R.id.editOrderNumber);
        this.showExtra = (TextView) super.findViewById(R.id.showExtra);
        this.listView = (ListView) super.findViewById(R.id.listView);
        this.hopperNum = (EditText) super.findViewById(R.id.hopperNum);
        this.hopperUpdata = (EditText) super.findViewById(R.id.hopperUpdata);
        this.submit = (Button) super.findViewById(R.id.submit);
        this.hopperBack = (ImageButton) super.findViewById(R.id.hopperBack);

//
        //orderStr = editOrderNumber.getText().toString().trim();

        this.hopperBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HopperUpdataActivity.this.finish();
            }
        });


       /* Intent intent = getIntent();
        infoExtra = intent.getStringExtra("oper");

       showExtra.setText("订单号：" + infoExtra);*/
        this.isFileHave();
        this.submit.setOnClickListener(new OnClickListenerSubmit());


        HopperUpdataActivity.this.simpleAdapter = new SimpleAdapter(HopperUpdataActivity.this,
                allList, R.layout.list_table,
                new String[]{"orderStr", "dataEdit", "updataEdit"},
                new int[]{R.id.listShowTitle, R.id.listShowHopper, R.id.listShowHopperNum});


        //监听回车键
        hopperNum.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    // 监听ENTER键
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {

                        HopperUpdataActivity.this.saveList();

                        HopperUpdataActivity.this.hopperNum.setText("");
                        HopperUpdataActivity.this.hopperUpdata.setText("1");
                        HopperUpdataActivity.this.hopperNum.requestFocus();
                        HopperUpdataActivity.this.listView.setAdapter(HopperUpdataActivity.this.simpleAdapter);
                    }
                }
                return false;
            }
        });


    }


    private class OnClickListenerSubmit implements View.OnClickListener {

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            HopperUpdataActivity.this.outPutFileToSDCard();
        }
    }


    //得到内容并保存到allList集合里
    private void saveList() {
        orderStr = editOrderNumber.getText().toString().trim();
        dataEdit = this.hopperNum.getText().toString().trim();
        updataEdit = this.hopperUpdata.getText().toString().trim();
        if (orderStr.length()>0){
            showExtra.setText("订单号：" + orderStr);
            editOrderNumber.setEnabled(false);
        }else{
            editOrderNumber.setEnabled(true);
        }
        if (dataEdit.length() == 0) {
            Toast.makeText(this, "请扫描条码", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("orderStr", orderStr);
            map.put("dataEdit", dataEdit);
            map.put("updataEdit", updataEdit);

            this.allList.add(map);


            // this.numdata++;

        }
    }

    //判断文件夹是否存在
    private void isFileHave() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 不存在不操作
            return; // 返回到程序的被调用处
        }
        //  String s = longTime+".xml";
        String s = GetUpString();
        file = new File(MetaDataInterface.FILE_PATH
                + s); // 要输出文件的路径
        if (!file.getParentFile().exists()) { // 文件不存在
            file.getParentFile().mkdirs();    // 创建文件夹
        }
    }





    //获得当前系统时间，并命名文件夹名字
    public static String GetUpString() {
        //  String string2 = "pd";
        SimpleDateFormat strdate = new SimpleDateFormat("yyyyMMddHHmm");//
        java.util.Date currentdate = new java.util.Date();// 当前时间
        String date = strdate.format(currentdate);

        return date + ".xml";
    }


    //删除文件1.首先取得全部文件；2.判断文件创建时间；3.删除时间大于15天的文件
    private String delFile() {
        String path = MetaDataInterface.FILE_PATH + "pd.db";
        File file = new File(path);
        if (!file.exists()) {
            return "NotFile!";
        }

        file.delete();
        return "success";

    }


    private void outPutFileToSDCard() {
        List<LinkMan> all = new ArrayList<LinkMan>();

        for (int i = 0; i < allList.size(); i++) {
            //  System.out.println("###############" + allList.size());
            Map<String, String> map = this.allList.get(i);
            LinkMan man = new LinkMan();
            man.setOrder(map.get("infoExtra"));
            man.setHopper(map.get("dataEdit"));
            man.setNumber(map.get("updataEdit"));
            all.add(man);
        }


        OutputStream output = null;
        try {
            output = new FileOutputStream(file);
            new MyXMLPullUtil(output, all).save();
        } catch (Exception e) {
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }


}
