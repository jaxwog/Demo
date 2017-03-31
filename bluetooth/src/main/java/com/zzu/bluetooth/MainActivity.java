package com.zzu.bluetooth;


        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.lang.reflect.Method;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Set;
        import java.util.UUID;

        import android.os.Bundle;
        import android.app.Activity;
        import android.bluetooth.BluetoothAdapter;
        import android.bluetooth.BluetoothDevice;
        import android.bluetooth.BluetoothSocket;
        import android.content.BroadcastReceiver;
        import android.content.Context;
        import android.content.Intent;
        import android.content.IntentFilter;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.view.Window;
        import android.widget.AdapterView;
        import android.widget.AdapterView.OnItemClickListener;
        import android.widget.Button;
        import android.widget.ListView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
    private Button btn_search_devices;
    private Button btn_close_devices;
    private ListView list_bonded_devices;
    private List<BluetoothDevice> bondedDevicesList;
    private MyListAdapter mBondedAdapter;
    private ListView list_search_devices;
    private List<BluetoothDevice> searchDevicesList;
    private MyListAdapter mSearchAdapter;
    private BluetoothAdapter adapter;
    private TextView textView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//        setTitle("搜索蓝牙设备");
        setContentView(R.layout.activity_main);

        btn_search_devices = (Button) findViewById(R.id.btn_search_devices);
        btn_search_devices.setOnClickListener(this);
        btn_close_devices = (Button) findViewById(R.id.btn_close_devices);
        btn_close_devices.setOnClickListener(this);
         textView = (TextView) findViewById(R.id.tv_show);
        progressBar = (ProgressBar) findViewById(R.id.progressBar4);
        //已配对设备列表
        list_bonded_devices = (ListView) findViewById(R.id.list_bonded_devices);
        bondedDevicesList = new ArrayList<BluetoothDevice>();
        //设置适配器
        mBondedAdapter = new MyListAdapter(this, bondedDevicesList);
        list_bonded_devices.setAdapter(mBondedAdapter);
        list_bonded_devices.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               String s = bondedDevicesList.get(position).getName();
                Toast.makeText(MainActivity.this, s+"已经配对", Toast.LENGTH_SHORT).show();
                try {
                    // 连接
                    //connect(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        //搜索到的设备列表
        list_search_devices = (ListView) findViewById(R.id.list_search_devices);
        searchDevicesList = new ArrayList<BluetoothDevice>();
        mSearchAdapter = new MyListAdapter(this, searchDevicesList);
        list_search_devices.setAdapter(mSearchAdapter);
        list_search_devices.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                BluetoothDevice device = searchDevicesList.get(position);
                try {
                    // 配对
                    Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                    createBondMethod.invoke(device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 检查设备是否支持蓝牙,若支持则打开
        checkBluetooth();

        // 获取所有已经绑定的蓝牙设备
        getBondedDevices();

        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播接收器，接收并处理搜索结果
        registerReceiver(receiver, mFilter);

    }

    /**
     * 检查设备是否支持蓝牙,若支持则打开
     */
    private void checkBluetooth() {
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) {
            // 设备不支持蓝牙
            Toast.makeText(this, "设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        }else {
            // 判断蓝牙是否打开，如果没有则打开蓝牙
            // adapter.enable() 直接打开蓝牙，但是不会弹出提示，以下方式会提示用户是否打开
            if (!adapter.isEnabled()) {
                Intent intent = new Intent();
                //打开蓝牙设备
                intent.setAction(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                //是设备能够被搜索
                intent.setAction(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                // 设置蓝牙可见性，最多300秒
                intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(intent);
            }
        }
    }


    /**
     *  获取所有已经绑定的蓝牙设备
     */
    private void getBondedDevices() {
        bondedDevicesList.clear();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        bondedDevicesList.addAll(devices);
        //为listview动态设置高度（有多少条目就显示多少条目）
        setListViewHeight(bondedDevicesList.size());
        mBondedAdapter.notifyDataSetChanged();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 防止重复添加
                    if (searchDevicesList.indexOf(device) == -1)
                        searchDevicesList.add(device);
                    //devicesList.add("未配对 | "+device.getName() + "（"  + device.getAddress()+"）");
                    mSearchAdapter.notifyDataSetChanged();
                }
                // 搜索完成
            } else if (action
                    .equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
                textView.setText("搜索完成");
                progressBar.setVisibility(View.GONE);
//                setTitle("搜索完成");
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                // 状态改变的广播
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = device.getName();
                if (device.getName().equalsIgnoreCase(name)) {
                    int connectState = device.getBondState();
                    switch (connectState) {
                        case BluetoothDevice.BOND_NONE:  //10
                            Toast.makeText(MainActivity.this, "取消配对："+device.getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothDevice.BOND_BONDING:  //11
                            Toast.makeText(MainActivity.this, "正在配对："+device.getName(), Toast.LENGTH_SHORT).show();
                            break;
                        case BluetoothDevice.BOND_BONDED:   //12
                            Toast.makeText(MainActivity.this, "完成配对："+device.getName(), Toast.LENGTH_SHORT).show();
                            getBondedDevices();
                            try {
                                // 连接
                                connect(device);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
            }
        }
    };

    //蓝牙设备的连接（客户端）
    private void connect(BluetoothDevice device) {
        // 固定的UUID
        final String SPP_UUID = "00001101-0000-1000-8000-00805F9B34FB";
        UUID uuid = UUID.fromString(SPP_UUID);
        try {
            BluetoothSocket socket = device.createRfcommSocketToServiceRecord(uuid);
            socket.connect();
//			OutputStream outputStream = socket.getOutputStream();
//	        InputStream inputStream = socket.getInputStream();
//	        outputStream.write("StartOnNet\n".getBytes());
//	        outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search_devices:
                setProgressBarIndeterminateVisibility(true);
//                setTitle("正在扫描....");
                textView.setText("正在扫描....");
                progressBar.setVisibility(View.VISIBLE);
                searchDevicesList.clear();
                mSearchAdapter.notifyDataSetChanged();
                // 如果正在搜索，就先取消搜索
                if (!adapter.isDiscovering()) {
                    adapter.cancelDiscovery();
                }
                // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
                adapter.startDiscovery();
                break;
            case R.id.btn_close_devices:
                // 如果正在搜索，就先取消搜索
                if (adapter.isDiscovering()) {
                    adapter.cancelDiscovery();
                }
                break;
        }
    }

    //为listview动态设置高度（有多少条目就显示多少条目）
    private void setListViewHeight(int count) {
        if (mBondedAdapter==null) {
            return ;
        }
        int totalHeight = 0;
        for (int i = 0; i < count; i++) {
            View listItem = mBondedAdapter.getView(i, null, list_bonded_devices);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = list_bonded_devices.getLayoutParams();
        params.height = totalHeight;
        list_bonded_devices.setLayoutParams(params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除注册
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBondedDevices();
    }

}

