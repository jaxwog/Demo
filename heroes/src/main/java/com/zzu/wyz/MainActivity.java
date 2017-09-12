package com.zzu.wyz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.zzu.wyz.adapter.MainAdapter;

import java.util.ArrayList;
import java.util.List;
/*
RecyclerView的使用方法
 */
public class MainActivity extends AppCompatActivity implements IActivity, View.OnClickListener {

    private RecyclerView recyclerView;
    private MainAdapter mainAdapter;
    private List<String> mList;
    private Spinner spinner;
    private Button addButton;
    private Button delButton;
    private RecyclerView.LayoutManager mLayoutManager;
    private SpinnerAdapter spinnerAdapter;

    //    private String[] aear = new String[]{"LayoutManager","GridLayoutManager"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        registerListener();
    }

    @Override
    public void initView() {
        recyclerView = (RecyclerView) super.findViewById(R.id.recyclerView);
        spinner = (Spinner) findViewById(R.id.spiner);
        addButton = (Button) findViewById(R.id.addItem);
        delButton = (Button) findViewById(R.id.delItem);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        initData();
        mainAdapter = new MainAdapter(this, mList);
        recyclerView.setAdapter(mainAdapter);

//        spinnerAdapter = new ArrayAdapter<String>( MainActivity.this,
//                android.R.layout.simple_dropdown_item_1line,
//                MainActivity.this.aear);
//                spinner.setAdapter(spinnerAdapter);

    }

    @Override
    public void initData() {

        mList = new ArrayList<String>();
        mList.add("发现");
        mList.add("发现");
        mList.add("发现");

        spinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view,
                                               int position,
                                               long id) {
                        if (position == 0) {
                            recyclerView.setLayoutManager(
                                    // 设置为线性布局
                                    new LinearLayoutManager(
                                            MainActivity.this));
                        } else if (position == 1) {
                            recyclerView.setLayoutManager(
                                    // 设置为表格布局
                                    new GridLayoutManager(
                                            MainActivity.this, 3));
                        } else if (position == 2) {
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }

    @Override
    public void registerListener() {
        addButton.setOnClickListener(this);
        delButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addItem:
                add();
                break;
            case R.id.delItem:
                del();
                break;
            default:
                break;
        }
    }

    void add() {
        mList.add("增加");
        int position = mList.size();
        if (position > 0) {
            mainAdapter.notifyDataSetChanged();
        }
    }

    void del() {
        int position = mList.size();
        if (position > 0) {
            mList.remove(position - 1);
            mainAdapter.notifyDataSetChanged();
        }
    }

}
