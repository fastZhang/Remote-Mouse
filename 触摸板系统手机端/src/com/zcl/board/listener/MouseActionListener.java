package com.zcl.board.listener;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.zcl.board.mouse.MouseAction;
import com.zcl.board.mouse.SendMouseActionUtils;
import com.zcl.board.service.MyService;

/**
 * 心跳包控制的发送和指令发送
 * sendMouseAction每次发送重置count = 5
 * heartbeatSend判断超过5秒就开始发送心跳包
 * @author ZL
 * @version 2016-5-25 上午11:05:21
 */
public class MouseActionListener implements MouseActionListenerInterface {
	
	/**发送指令是频繁事件需要开启线程池任务。*/
	private ExecutorService fixedThreadPool;
	/** 电脑端口 */
	private InetSocketAddress mRemote;
	/** 停止发送鼠标指令5秒之后或者刚初始化5秒之后开始发送心跳包，电脑端检测停止接收指令时间为10秒 */
	private int count = 5;

	public MouseActionListener(InetSocketAddress remote) {
		mRemote = remote;
		fixedThreadPool = Executors.newFixedThreadPool(1);
		// 心跳包的线程，判断发送指令间的间隔时间超过5秒开始发送心跳包
		heartbeatSend();
	}
	
	/**
	 * 心跳包的线程，判断发送指令间的间隔时间超过5秒开始发送心跳包
	 */
	private void heartbeatSend(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (count > 0) {
						synchronized (this) {
							count--;
						}
					} else {
						// 发送心跳包
						fixedThreadPool.execute(new Runnable() {
							@Override
							public void run() {
								MyService.isStartHeartbeat = true;
								SendMouseActionUtils.sendAction(mRemote,MouseAction.HEARTBEAT, 0, 0);
							}
						});

					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	@Override
	public void sendMouseAction(final MouseAction action, final int x,final int y) {
		// 单线程顺序任务
		fixedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				SendMouseActionUtils.sendAction(mRemote, action, x, y);
				// 恢复计时器
				synchronized (this) {
					count = 5;
					// 更新心跳包标志是否开始
					MyService.isStartHeartbeat = false;
				}
			}
		});
	}

}
