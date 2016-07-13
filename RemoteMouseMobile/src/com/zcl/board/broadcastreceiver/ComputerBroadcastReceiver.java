package com.zcl.board.broadcastreceiver;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

import com.zcl.board.adapter.CompIpPortAdapter;
import com.zcl.board.constant.Constant;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 电脑回复后，更新电脑列表的接收者
 * @author ZL
 * @version 2016-5-24 下午10:44:38
 */
public class ComputerBroadcastReceiver extends BroadcastReceiver {
	
	private ArrayList<InetSocketAddress> computerSockets;
	private CompIpPortAdapter mAdapter;

	public ComputerBroadcastReceiver(ArrayList<InetSocketAddress> computerSockets, CompIpPortAdapter mAdapter) {
		this.computerSockets = computerSockets;
		this.mAdapter = mAdapter;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		//处理搜索反馈，已经在列表中的电脑，不再添加显示
		InetSocketAddress computerSocketAddress = (InetSocketAddress) intent.getSerializableExtra(Constant.computerSocketAddress);
		if (computerSocketAddress != null) {
			//判断新地址是否存在
			boolean isRepeat = false;
			//遍历当前集合，查询新反馈的电脑是不是已经存在
			for (int i = 0; i < computerSockets.size(); i++) {
				InetSocketAddress item = mAdapter.getItem(i);
				if (item.equals(computerSocketAddress)) {
					//是重复的
					isRepeat = true;
					break;
				}
			}
			
			//没有重复更新集合
			if (!isRepeat) {
				computerSockets.add(computerSocketAddress);
				mAdapter.notifyDataSetChanged();
			}
		}

	}
}
