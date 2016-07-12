package com.zcl.board.listener;

import java.net.DatagramPacket;
import java.net.InetAddress;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zcl.board.R;
import com.zcl.board.constant.Constant;
import com.zcl.board.service.MyService;

/**
 * 开启搜索线程
 * 搜索的循环每两秒发送一次搜索广播
 * @author ZL
 * @version 2016-5-25 上午9:50:20
 */
public class BtnSearchClickListener implements OnClickListener {

	private LinearLayout llProgress;
	private boolean isSearch;
	private Context mContext;
	public BtnSearchClickListener(Context context, LinearLayout llProgress) {
		this.llProgress = llProgress;
		mContext = context;
		//搜索的循环每两秒发送一次搜索广播
		new Thread(new Runnable() {
			@Override
			public void run() {
				//当不在控制页面，即主页面时，开启搜索
				while (!Constant.isInControlActivity) {
					// 每2秒发出一次搜索指令
					if (isSearch) {
						search();
					}
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

	/**
	 * 在局域网内发出一次广播用于搜索电脑
	 */
	public void search() {
		try {
			byte[] data = new byte[2];
			data[0] = 0;
			data[1] = (byte) 255;
			InetAddress adds = InetAddress.getByName(Constant.defaultBoartCastIp);
			// 发向电脑端的端口48080
			DatagramPacket dp = new DatagramPacket(data, data.length,adds, Constant.defaultPort);
			MyService.mDatagramSocket.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		Button btn = (Button) v;
		String str = btn.getText().toString();
		//切换显示状态
		if (str.equals(mContext.getString(R.string.click_start_searching))) {
			btn.setText(mContext.getString(R.string.click_stop_searching));
			llProgress.setVisibility(View.VISIBLE);
			isSearch = true;
		} else {
			btn.setText(mContext.getString(R.string.click_start_searching));
			llProgress.setVisibility(View.INVISIBLE);
			isSearch = false;
		}
	}
	
}
