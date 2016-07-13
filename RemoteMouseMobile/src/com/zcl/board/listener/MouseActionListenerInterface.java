package com.zcl.board.listener;

import com.zcl.board.mouse.MouseAction;

/**
 * 鼠标事件执行监听的接口
 * @author ZL
 * @version 2016-5-25 上午10:59:08
 */
public interface MouseActionListenerInterface {
	void sendMouseAction(MouseAction action, int x, int y);
}
