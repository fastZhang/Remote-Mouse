package com.zcl.board.mouse;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;

import com.zcl.board.service.MyService;
import com.zcl.board.utils.ByteUtils;

/**
 * 发送鼠标指令的工具类，鼠标动作监听类调用 
 * @author ZL
 * @version 2016-5-25 上午11:14:54
 */
public class SendMouseActionUtils {

	public static void sendAction(InetSocketAddress inetSocketAddress,MouseAction action, int x, int y) {
		
		byte[] data = new byte[2];
		
		switch (action) {
		case HEARTBEAT:
			data[0] = -128;
			data[1] = -128;
			break;
		case MOVE: // 移动鼠标
			data = ByteUtils.xyTo2Bytes(x, y);
			break;
		case LEFT_DOWN: // 鼠标左键按下
			data[0] = 1;
			data[1] = -128;
			break;
		case LEFT_UP: // 鼠标左键抬起
			data[0] = 2;
			data[1] = -128;
			break;
		case MIDDLE_DOWN: // 鼠标中键按下
			data[0] = 3;
			data[1] = -128;
			break;
		case MIDDLE_UP:// 鼠标中键抬起
			data[0] = 4;
			data[1] = -128;
			break;
		case RIGHT_DOWN: // 鼠标右键按下
			data[0] = 5;
			data[1] = -128;
			break;
		case RIGHT_UP: // 鼠标右键抬起
			data[0] = 6;
			data[1] = -128;
			break;
		}
		
		try {
			DatagramPacket dp = new DatagramPacket(data, data.length,inetSocketAddress.getAddress(), inetSocketAddress.getPort());
			MyService.mDatagramSocket.send(dp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
