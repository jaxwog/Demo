package zzu.wyz.scan;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListShowActivity extends AppCompatActivity {


    private ListView gridView;

    //private ListView listView;
    private EditText listEdit;
    private SimpleAdapter simpleAdapter;
    private List<Map<String, String>> allList = new ArrayList<Map<String, String>>();
    private String dataEdit;
    private static int numdata = 0;
    private ImageButton scanBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_list);


        this.listEdit = (EditText) super.findViewById(R.id.listEdit);
        this.gridView = (ListView) super.findViewById(R.id.gridView);

        this.scanBack = (ImageButton)super.findViewById(R.id.scanBack);
        this.scanBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListShowActivity.this.finish();
            }
        });





        ListShowActivity.this.simpleAdapter = new SimpleAdapter(ListShowActivity.this, allList, R.layout.grid_table,
                new String[]{"tableShow","image"}, new int[]{R.id.tableShow,R.id.imageShow});

        //监听回车键
        listEdit.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    // 监听ENTER键
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {

                        ListShowActivity.this.saveList();
                        ListShowActivity.this.listEdit.setText("");

                        ListShowActivity.this.gridView.setAdapter(ListShowActivity.this.simpleAdapter);
                    }
                }
                return false;
            }
        });


    }


    //得到内容并保存到allList集合里
    private void saveList() {
        dataEdit = this.listEdit.getText().toString().trim();
        if (dataEdit.length()==0) {
            Toast.makeText(this, "请扫描条码", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, String> map = new HashMap<String, String>();
            map.put("tableShow", dataEdit);
            map.put("image",String.valueOf(R.drawable.gridright));
            this.allList.add(map);
            System.out.println("#######################" + map.get("tableShow"));
            // this.numdata++;

        }
    }
}
