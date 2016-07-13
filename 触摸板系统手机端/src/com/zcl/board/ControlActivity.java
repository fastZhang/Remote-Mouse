package com.zcl.board;

import java.net.InetSocketAddress;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.zcl.board.constant.Constant;
import com.zcl.board.listener.MouseActionListener;
import com.zcl.board.service.MyService;
import com.zcl.board.view.ControlView;

/**
 * 控制页面
 * 进入控制页面打开了心跳包的thread
 * @author ZL
 * @version 2016-5-25 上午9:42:31
 */
public class ControlActivity extends Activity {

	private ControlView v_control;
	/** 电脑端口和ip */
	private InetSocketAddress mSocketAddress;	
	/** 标志电脑端在线*/
	private boolean isOnLine = true;
	//MouseActionListener打开了心跳包的thread
	private MouseActionListener mMouseActionListener;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			offLine();
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_control);
		// 获取序列化的文件类型
		Intent intent = getIntent();
		mSocketAddress = (InetSocketAddress) intent.getSerializableExtra(Constant.computerSocketAddress);
		//标志进入，用于停止BtnSearchClickListener搜索的循环
		Constant.isInControlActivity = true;

		initView();
		initData();
		startNextHeartBeatTimeListener();
	}
	
	@SuppressLint("NewApi") @Override
	public void onBackPressed() {
	 	mMouseActionListener = null;
    		isOnLine =false;
		super.onBackPressed();
	}
	
	private void initView() {
		v_control = (ControlView) findViewById(R.id.v_control);
	}
	
	private void initData() {
		mMouseActionListener = new MouseActionListener(mSocketAddress);
		v_control.setOnMouseActionListener(mMouseActionListener);
	}

	/**
	 * 检测心跳包的反馈，判断下一次心跳包的发送
	 */
	private void startNextHeartBeatTimeListener() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (isOnLine) {
					//每次接到电脑端的信息nextHeartBeatTime重置为10，递减10秒判断，10秒之后电脑断线
					timeTask();	
				}
			}
		}).start();	
	}
	
	/**
	 * 时间判断
	 */
	private void timeTask() {
		synchronized (this) {
			if (MyService.isStartHeartbeat) {
				if (MyService.nextHeartBeatTime > 0) {
					MyService.nextHeartBeatTime--;
				} else {
					//电脑端退出了
					isOnLine =false;
					mHandler.sendEmptyMessage(0);
				}
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 提示电脑端短线
	 */
	private void offLine() {

        AlertDialog ad=new AlertDialog.Builder(this).create();  
        ad.setTitle("断开提示");  
        ad.setMessage("电脑端断开");  
        ad.setButton("确定", new DialogInterface.OnClickListener() {                
            @Override  
            public void onClick(DialogInterface dialog, int which) {  
            	mMouseActionListener = null;
            	isOnLine =false;
            	//退出程序
            	android.os.Process.killProcess(android.os.Process.myPid());    //获取PID 
            	System.exit(0);   //常规java、c#的标准退出法，返回值为0代表正常退出
            }  
        });  
        ad.show();	
	}
	
}
