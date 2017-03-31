package zzu.wyz.demo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Map;

public class PopupActivity extends Activity {
    private Button popbut = null;
    private TextView statusinfo = null;
    private View popView = null;
    private PopupWindow popWin = null;
    private RadioGroup changestatus = null;
    private Button cancel = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_popupmain);






        this.popbut = (Button) super.findViewById(R.id.popbut);
        this.statusinfo = (TextView) super.findViewById(R.id.statusinfo);

        this.popbut.setOnClickListener(new OnClickListenerImpl());
    }
      //监听调用PopupWindow
    private class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            LayoutInflater inflater = LayoutInflater.from(PopupActivity.this);
            PopupActivity.this.popView = inflater.inflate(
                    R.layout.popup_table, null);    // 找到了布局文件中的View
            PopupActivity.this.popWin = new PopupWindow(
                    PopupActivity.this.popView, 300, 300, true);//设置弹出窗口的位置
            PopupActivity.this.changestatus = (RadioGroup) PopupActivity.this.popView
                    .findViewById(R.id.changestatus);    // 取得弹出界面中的组件
            PopupActivity.this.cancel = (Button) PopupActivity.this.popView
                    .findViewById(R.id.cancel);
            PopupActivity.this.changestatus
                    .setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
            PopupActivity.this.cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    PopupActivity.this.popWin.dismiss();    // 使PopupWindow不显示
                }
            });
            PopupActivity.this.popWin.showAtLocation(
                    PopupActivity.this.popbut, Gravity.CENTER, 0, 0);
        }
    }

    private class OnCheckedChangeListenerImpl implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton but = (RadioButton) PopupActivity.this.popView.findViewById(group
                    .getCheckedRadioButtonId());    // 取得指定的单选钮被选中
            PopupActivity.this.statusinfo.setText("当前用户状态：" + but.getText().toString());
            PopupActivity.this.popWin.dismiss();
        }

    }
}