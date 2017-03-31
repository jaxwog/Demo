package zzu.wyz.demo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ListFileActivity extends AppCompatActivity {
    private List<Map<String,Object>> allFileItems = new ArrayList<Map<String,Object>>() ;
    private SimpleAdapter simple = null ;
    private ListView list = null ;
    private ListFileThread ft = null ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_listfile);
        this.list = (ListView) super.findViewById(R.id.listFile) ;
        File filePath = new File(java.io.File.separator); // 从根目录下开始列出
        this.list.setOnItemClickListener(new OnItemClickListenerImpl()) ;
        this.ft = new ListFileThread() ;
        this.ft.execute(filePath) ;
    }

    private class OnItemClickListenerImpl implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            File currFile = (File) ListFileActivity.this.allFileItems
                    .get(position).get("name");
            if (currFile.isDirectory()) { // 当前是一个目录
                ListFileActivity.this.allFileItems = new ArrayList<Map<String,Object>>() ;
                ListFileActivity.this.ft = new ListFileThread() ;
                ListFileActivity.this.ft.execute(currFile) ;
            }
        }

    }

    private class ListFileThread extends AsyncTask<File, File, String> {

        @Override
        protected void onProgressUpdate(File... values) {
            Map<String,Object> fileItem = new HashMap<String,Object>() ;	// 表示可以返回
            if (values[0].isDirectory()) {
                fileItem.put("img", R.drawable.folder_close); // 文件夹
            } else {	// 是文件
                fileItem.put("img",R.drawable.file) ;
            }
            fileItem.put("name", values[0]) ;
            ListFileActivity.this.allFileItems.add(fileItem) ;
            ListFileActivity.this.simple = new SimpleAdapter(
                    ListFileActivity.this,
                    ListFileActivity.this.allFileItems,
                    R.layout.listfile_table, new String[] { "img", "name" },
                    new int[] { R.id.listfileimg, R.id.listfilename });
            ListFileActivity.this.list
                    .setAdapter(ListFileActivity.this.simple);
        }

        @Override
        protected String doInBackground(File... params) {
            if (!params[0].getPath().equals(java.io.File.separator)) { // 不是根目录
                Map<String,Object> fileItem = new HashMap<String,Object>() ;	// 表示可以返回
                fileItem.put("img",R.drawable.folder_open) ;	// 可以返回
                fileItem.put("name", params[0].getParentFile()) ;
                ListFileActivity.this.allFileItems.add(fileItem) ;
            }
            if (params[0].isDirectory()) {	// 是文件夹
                File tempFile [] = params[0].listFiles() ;
                if(tempFile != null) {
                    for (int x = 0; x < tempFile.length; x++) {
                        this.publishProgress(tempFile[x]) ;
                    }
                }
            }
            return "文件已列出";
        }
    }

}
