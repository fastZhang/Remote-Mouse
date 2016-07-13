package com.zcl.board.service;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import com.zcl.board.constant.Constant;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 后台主服务，用于打开端口，接收数据
 * @author ZL
 * @version 2016-5-24 下午9:11:28
 */
public class MyService extends Service {

	/** 收发数据的UDPsocket */
	public static DatagramSocket mDatagramSocket;
	/** 开始发送心跳包 ，在MouseActionListener每次发送心跳包后会更新isStartHeartbeat=true；*/
	public static boolean isStartHeartbeat = false;

	/** 用于接收到心跳包数据后，计算下一次心跳包接收的间隔时间，超过10秒，程序断线 */
	public static int nextHeartBeatTime = 10;
	
	private MyBinder myBinder;
	
	@Override
	public void onCreate() {
		super.onCreate();
		createSocket();
		startAppSocketListener();
	}

	@Override
	public IBinder onBind(Intent intent) {
		myBinder = new MyBinder();
		return myBinder;
	}

	/** 创建binder对象 */
	public class MyBinder extends Binder  {
		//扩展对象调用service的方法
	};

	private void createSocket() {
		try {
			// 只在程序刚开始时创建，只能被创建一次，全局使用，端口指定为0，自动分配
			mDatagramSocket = new DatagramSocket(Constant.autoPort);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private void startAppSocketListener() {
		// 开启接收线程，整个程序生命周期内只能开启一次
		new Thread(new Runnable() {
			@Override
			public void run() {

				byte data[] = new byte[2];
				// 数据报
				DatagramPacket recPacket = new DatagramPacket(data, 2);
				while (true) {
					try {

						mDatagramSocket.receive(recPacket);
						// 如果为00，则为电脑端心跳包反馈00，发送为-128 -128
						if ((data[0] == 0) && (data[1] == 0)) {
							synchronized (this) {
								// 从新倒计时反馈，如果超过10秒，短线
								nextHeartBeatTime = 10;
							}

						} else {
							// 添加意图过滤器action
							Intent intent = new Intent(
									Constant.intentActionOnLine);
							// 可传递接收到的数据，此处不需要
							// intent.putExtra("data", data);
							// 获取电脑的ip和端口
							SocketAddress socketAddress = recPacket.getSocketAddress();
							//强转为SocketAddress的实现类
							InetSocketAddress computerSocketAddress = (InetSocketAddress) socketAddress;
							intent.putExtra(Constant.computerSocketAddress, computerSocketAddress);
							sendBroadcast(intent);
						}

					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}

}
