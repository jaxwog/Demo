package com.zzu.wyz.testmap;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cld.navisdk.routeinfo.RoadInfo;
import com.cld.navisdk.routeinfo.RouteLineInfo;



public class DetailsDemo extends Activity {

	private ListView mLv_Details;
	private List<RoadInfo> mList;// 数据集合
	private WalkDetailsAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitvity_detail_walk);
		initView();
	}

	// 初始化视图
	private void initView() {
		mLv_Details = (ListView) findViewById(R.id.lv_details);
		// 从上一个页面获得传递过来的数据
		Intent intent = getIntent();
		RouteLineInfo info = (RouteLineInfo) intent.getParcelableExtra("info");
		mList = info.getRoadInfos();
		// 设置适配器
		mAdapter = new WalkDetailsAdapter();
		mLv_Details.setAdapter(mAdapter);
		// 找到对应的控件
		TextView tv_Start = (TextView) findViewById(R.id.tv_start);
		TextView tv_End = (TextView) findViewById(R.id.tv_end);
		TextView tv_Distance = (TextView) findViewById(R.id.tv_distance);
		// 设置初始值
		if (mList.size() != 0 && mList.get(0).getName() != null) {
			tv_Start.setText("起点:" + mList.get(0).getName());
		} else {
			tv_Start.setText("起点:地图上的点");
		}
		if (mList.size() != 0 && mList.get(mList.size() - 1).getName() != null) {
			tv_End.setText("终点:" + mList.get(mList.size() - 1).getName());
		} else {
			tv_End.setText("终点:地图上的点");
		}

		tv_Distance.setText(changeLenth(info.getDistance()) + ","
				+ changeTime(info.getTime()));
	}

	/**
	 * 转换时间,把秒转换成分和小时
	 *
	 * @return String
	 * @author Wangxy
	 * @date 2016-3-21 上午9:59:42
	 */
	private String changeTime(long second) {
		String time = "";
		if (second < 3600) {
			time = second / 60 + "分钟";
		} else {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(0);
			time = second / 3600 + "小时" + (second % 3600) / 60 + "分钟";
		}
		return time;
	}

	/**
	 * 转换长度单位,把米转换成公里
	 *
	 * @return String
	 * @author Wangxy
	 * @date 2016-3-21 上午10:00:06
	 */
	private String changeLenth(long length) {
		String distance = "";
		// 如果小于一公里,就用米为单位,如果大于一公里,就以公里为单位
		if (length < 1000) {
			distance = length + "米";
		} else {
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(1);
			distance = df.format(length / 1000.0) + "公里";
		}
		return distance;
	}

	/**
	 * 当前页面的listview的适配器类
	 *
	 * @author Wangxy
	 * @date 2016-3-21 上午10:55:07
	 */
	protected class WalkDetailsAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (mList != null) {
				return mList.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = (TextView) View.inflate(DetailsDemo.this,
					R.layout.item_detail_walk, null);
			// 获得当前条目导航路径信息类
			RoadInfo info = (RoadInfo) mList.get(position);
			// 获得路径详情并显示
			tv.setText(info.getDescribe());
			return tv;
		}
	}
}
