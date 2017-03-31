package zzu.wyz.scan;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/**
 * Created by WYZ on 2015/12/23.
 */
public class LoginActivity extends Activity {

    private static final int REQUEST_TIMEOUT = 5 * 1000;// 设置请求超时5秒钟
    private static final int SO_TIMEOUT = 10 * 1000; // 设置等待数据超时时间10秒钟
    private EditText loginName,loginPsw;
    private Button loginBut;
   private String name,psw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.dialog_login);

        this.loginName = (EditText)super.findViewById(R.id.login_name);
        this.loginPsw = (EditText)super.findViewById(R.id.login_psw);
        this.loginBut = (Button)super.findViewById(R.id.login_but);

        this.loginBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.this.loginisRight();
            }
        });



    }
//判断用户名跟密码
 private void loginisRight(){
     name = this.loginName.getText().toString().trim();
     psw = this.loginPsw.getText().toString().trim();

     if (name.length()==0&&psw.length()==0){
         Toast.makeText(this,"请输入用户名密码",Toast.LENGTH_SHORT).show();
     }else{
         Intent _intent = new Intent(LoginActivity.this,MainActivity.class);
         _intent.putExtra("oper",name);
         startActivity(_intent);
     }

 }


/*
    public static void loginServer(String ip, String idname, String pswname)

    {

        // 使用apache HTTP客户端实现,需要改为服务器的地址
        String urlStr = "http://" + ip + ":8092/JsonHandler.ashx";
        // String SERVER_URL =
        // "http://"+ip+":"+port+"/JsonHandler.ashx?doc=GetUserInfo";
        HttpPost request = new HttpPost(urlStr);// 新建Post请求
        // 如果传递参数多的话，可以对传递的参数进行封装
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加用户名和密码
        // params.add(new BasicNameValuePair("tape",tape));
        params.add(new BasicNameValuePair("doc", "GetUserInfo"));
        params.add(new BasicNameValuePair("Id", idname));
        params.add(new BasicNameValuePair("pwd", pswname));
        System.out.println(params.get(0));
        System.out.println(params.get(1));
        System.out.println(params.get(2));
        try {

            // 设置请求参数项
            request.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            System.out.println("############" + request.toString());
            // HttpClient client = getHttpClient();
            // 执行请求返回响应
            HttpResponse response = new DefaultHttpClient().execute(request);
            System.out.println(response);
            System.out.println("从服务器端返回的"
                    + response.getStatusLine().getStatusCode());

            // 判断是否请求成功
            if (response.getStatusLine().getStatusCode() == 200) {
                // 获得响应信息
                System.out.println("返回为200");
                resultPost = EntityUtils.toString(response.getEntity());
                System.out.println("返回的数据为：" + resultPost);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/



}
