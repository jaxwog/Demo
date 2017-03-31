package zzu.wyz.scan;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ChoiceNumActivity extends AppCompatActivity {

    private Button butHopper;

    private EditText operNumber;

    private String orderStr;
    private Button timePicker;
    private Button datePicker;
    private TextView showtxt;
    private RadioGroup urgency,debug;
    private RadioButton urgency_yes,urgency_no,debug_yes,debug_no;
    private int  year, monthOfYear,  dayOfMonth, hourOfDay ,minute;
private String urgencyTemp,debugTemp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_num);
          init();


    }


    private void init(){
        this.butHopper = (Button) super.findViewById(R.id.butHopper);

        this.datePicker = (Button) super.findViewById(R.id.datePicker);
        this.timePicker = (Button) super.findViewById(R.id.timePicker);
        this.showtxt = (TextView)super.findViewById(R.id.showtxt);

       // this.operNumber = (EditText)super.findViewById(R.id.operNumber);

        this.debug = (RadioGroup) super.findViewById(R.id.debug);
        this.debug_yes = (RadioButton) super.findViewById(R.id.debug_yes);
        this.debug_no = (RadioButton) super.findViewById(R.id.debug_no);

        this.urgency = (RadioGroup) super.findViewById(R.id.urgency);
        this.urgency_yes = (RadioButton) super.findViewById(R.id.urgency_yes);
        this.urgency_no = (RadioButton) super.findViewById(R.id.urgency_no);



        this.datePicker.setOnClickListener(new OnClickListenerDate());
        this.timePicker.setOnClickListener(new OnClickListenerTime());

        this.debug.setOnCheckedChangeListener(new OnCheckedChangeListenerDebug());
        this.urgency.setOnCheckedChangeListener(new OnCheckedChangeListenerUrgency());


        this.butHopper.setOnClickListener(new OnClickListenerHopper());

    }


    private class OnClickListenerHopper implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            String timeStr = ChoiceNumActivity.this.longTime(year, monthOfYear,  dayOfMonth, hourOfDay ,minute);
           // String operStr = ChoiceNumActivity.this.operNumber.getText().toString().trim();
            if (timeStr.length()==0) {

                Toast.makeText(ChoiceNumActivity.this, "请输入条件！", Toast.LENGTH_SHORT).show();
            } else {

                Intent _intent = new Intent(ChoiceNumActivity.this,
                        ListShowActivity.class);
                //传递文件命名参数
              // _intent.putExtra("oper", operStr);
               // _intent.putExtra("order", orderStr);
                _intent.putExtra("longTime", timeStr);
                _intent.putExtra("urgency", urgencyTemp);
                _intent.putExtra("debug", debugTemp);
System.out.println("*********************longTime=" + timeStr);
System.out.println("*********************urgency=" + urgencyTemp);
System.out.println("*********************debug=" + debugTemp);

                startActivity(_intent);
            }
        }
    }

//返回时间字符串用于以后文件夹命名
    private String longTime(int  year, int monthOfYear,int  dayOfMonth,int  hourOfDay ,int minute){
        String month,day,hour,minuteStr;
        if (monthOfYear<10){
            month = "0"+monthOfYear;
        }else{
            month = String.valueOf(monthOfYear);
        }

        if (dayOfMonth<10){
            day = "0"+dayOfMonth;
        }else{
            day = String.valueOf(dayOfMonth);
        }

        if (hourOfDay<10){
            hour = "0"+hourOfDay;
        }else{
            hour = String.valueOf(hourOfDay);
        }

        if (minute<10){
            minuteStr = "0"+minute;
        }else{
            minuteStr = String.valueOf(minute);
        }



        String s = year+month+day+hour+minuteStr;
                return s;
    }


    //日期选择器
    private class OnClickListenerDate implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            DatePickerDialog dialog = new DatePickerDialog(ChoiceNumActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    ChoiceNumActivity.this.year = year;
                    ChoiceNumActivity.this.monthOfYear = monthOfYear+1;
                    ChoiceNumActivity.this.dayOfMonth = dayOfMonth;
                    showtxt.append("日期为：" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth +"\n");
                }
            }, 2016, 2, 12); // 默认的年、月、日
            dialog.show();    // 显示对话框
        }

    }

    //时间选择器
    private class OnClickListenerTime implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Dialog dialog = new TimePickerDialog(ChoiceNumActivity.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                    ChoiceNumActivity.this.hourOfDay = hourOfDay;
                    ChoiceNumActivity.this.minute = minute;
                    showtxt.append("时间为：" + hourOfDay + ":" + minute +"\n");

                }
            }, 8, 30, true);
            dialog.show();    // 显示对话框
        }

    }


    private class OnCheckedChangeListenerUrgency implements
            RadioGroup.OnCheckedChangeListener {

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String temp = null; // 保存以后show组件要显示的文本信息
            if (ChoiceNumActivity.this.urgency_yes.getId() == checkedId) { // 现在选中的ID和组件的ID一致
                temp = ChoiceNumActivity.this.urgency_yes.getText().toString(); // 取得信息
                urgencyTemp = "Y";
            }
            if (ChoiceNumActivity.this.urgency_no.getId() == checkedId) { // 现在选中的ID和组件的ID一致
                temp = ChoiceNumActivity.this.urgency_no.getText().toString(); // 取得信息
                urgencyTemp = "N";
            }
            ChoiceNumActivity.this.showtxt.append("是否紧急：" + temp +"\n");// 设置文本组件的内容
        }
    }


    private class OnCheckedChangeListenerDebug implements
            RadioGroup.OnCheckedChangeListener {

        public void onCheckedChanged(RadioGroup group, int checkedId) {
            String temp = null; // 保存以后show组件要显示的文本信息
            if (ChoiceNumActivity.this.debug_yes.getId() == checkedId) { // 现在选中的ID和组件的ID一致
                temp = ChoiceNumActivity.this.debug_yes.getText().toString(); // 取得信息
                debugTemp="Y";
            }
            if (ChoiceNumActivity.this.debug_no.getId() == checkedId) { // 现在选中的ID和组件的ID一致
                temp = ChoiceNumActivity.this.debug_no.getText().toString(); // 取得信息
                debugTemp="N";
            }
            ChoiceNumActivity.this.showtxt.append("调试与否：" + temp +"\n");// 设置文本组件的内容
        }
    }
}
