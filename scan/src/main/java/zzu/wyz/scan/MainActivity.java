package zzu.wyz.scan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    private Dialog loginDialog;
   // private ImageButton main_pcs,main_query;

    private Button main_pcs,main_query;

    private EditText loginName,loginPsw;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
         setContentView(R.layout.but_main);
        this.main_pcs = (Button)super.findViewById(R.id.main_pcs);
        this.main_query = (Button)super.findViewById(R.id.main_query);
        this.main_pcs.setOnClickListener(new OnClickListenerPcs());
        this.main_query.setOnClickListener(new OnClickListenerQuery());
        //this.loginBut();
    }


    private  class  OnClickListenerPcs implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent _it = new Intent(MainActivity.this,HopperUpdataActivity.class);
            startActivity(_it);
        }
    }

    private  class  OnClickListenerQuery implements View.OnClickListener{

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Intent _it = new Intent(MainActivity.this,ChoiceNumActivity.class);
            startActivity(_it);
        }
    }


    private void loginBut() {

        LayoutInflater factory = LayoutInflater.from(MainActivity.this);

        View loginView = factory.inflate(R.layout.dialog_login, null);

        loginName = (EditText)loginView.findViewById(R.id.login_name);
        loginPsw = (EditText)loginView.findViewById(R.id.login_psw);

        loginDialog = new AlertDialog.Builder(MainActivity.this)
                .setIcon(R.drawable.login_dialog)
                .setTitle(R.string.login_title)
                .setView(loginView)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                     //得到输入的数据判断用户名和密码
                        userName = loginName.getText().toString().trim();
                        if (userName.length()==0){
                            MainActivity.this.loginBut();
                        }
                    }
                })
                .setCancelable(false)
                .create();
        loginDialog.show();
    }



}
