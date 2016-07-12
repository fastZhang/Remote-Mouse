package com.zcl.board.listener;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zcl.board.ControlActivity;
import com.zcl.board.constant.Constant;
import com.zcl.board.service.MyService;
import com.zcl.board.service.MyService.MyBinder;

/**
 * ListView条目点击监听
 * @author ZL
 * @version 2016-5-25 上午9:35:47
 */
public class LvItemClickListener implements OnItemClickListener {

	private Context mContext;
	private Button mBtn;
	private LinearLayout mll;
	
	public LvItemClickListener(Context context,Button btn,LinearLayout ll) {
		mContext = context;
		mBtn = btn;
		mll = ll;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		InetSocketAddress socketAddress = (InetSocketAddress) parent.getItemAtPosition(position);
		Intent intent = new Intent(mContext, ControlActivity.class);
		intent.putExtra(Constant.computerSocketAddress, socketAddress);
		
		mBtn.setText("点击开始搜索电脑");
		mll.setVisibility(View.INVISIBLE);
		Constant.isSearch = false;
		
		mContext.startActivity(intent);
	}

}
