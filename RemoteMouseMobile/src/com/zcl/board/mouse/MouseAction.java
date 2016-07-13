package com.zcl.board.mouse;

/**
 * 事件列表
 * @author ZL
 * @version 2016-5-25 上午11:03:05
 */
public enum MouseAction {
	HEARTBEAT, // 空指令，心跳包
	MOVE, // 移动鼠标
	LEFT_DOWN, // 鼠标左键按下
	LEFT_UP, // 鼠标左键抬起
	RIGHT_DOWN, // 鼠标右键按下
	RIGHT_UP, // 鼠标右键抬起
	MIDDLE_DOWN, // 鼠标中键按下
	MIDDLE_UP // 鼠标中键抬起
}
