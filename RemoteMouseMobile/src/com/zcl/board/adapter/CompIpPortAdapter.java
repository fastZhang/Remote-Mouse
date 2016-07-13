package com.zcl.board.adapter;

import java.net.InetSocketAddress;
import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zcl.board.R;
import com.zcl.board.holder.CompIpPortHolder;

/**
 * 电脑的ip和端口的适配器
 * @author ZL
 * @2016-5-25@上午8:42:27
 */
public class CompIpPortAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<InetSocketAddress> computerSockets;

	public CompIpPortAdapter(Context context,ArrayList<InetSocketAddress> computerSockets) {
		this.context = context;
		this.computerSockets = computerSockets;
	}

	@Override
	public int getCount() {
		return computerSockets.size();
	}

	@Override
	public InetSocketAddress getItem(int position) {
		return computerSockets.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		CompIpPortHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.item_computer, null);
			holder = new CompIpPortHolder();
			holder.tv_ip = (TextView) convertView.findViewById(R.id.tv_ip);
			holder.tv_port = (TextView) convertView.findViewById(R.id.tv_port);
			convertView.setTag(holder);
		}
		holder = (CompIpPortHolder) convertView.getTag();

		InetSocketAddress item = getItem(position);
		holder.tv_ip.setText(item.getAddress().getHostAddress());
		holder.tv_port.setText(String.valueOf(item.getPort()));

		return convertView;
	}
}
