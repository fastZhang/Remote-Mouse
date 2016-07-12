package com.zcl.board.constant;

/**
 * 常量
 * @author  ZL 
 * @version 2016-5-24 下午9:44:42
 */
public class Constant {
	
	/** 接收到电脑回复在线的action */
	public static final String intentActionOnLine = "com.zcl.board.online";
	/** 断线的action */
	public static final String intentActionOffLine = "com.zcl.board.offline";
	
	/** 电脑端广播接收socket的默认端口 */
	public static final int defaultPort = 48080;
	/** 设置appsocket的端口为0，自动分配手机端口 */
	public static final int autoPort = 0;
	/** 通用的广播地址 */
	public static final String defaultBoartCastIp = "255.255.255.255";
	/** 用于intent传电脑端ip的,键 */
	public static final String computerSocketAddress = "socketAddress";
	
	
	/** 记录当前应用在哪个页面进入控制页面 */
	public static boolean isInControlActivity = false;
	/** 记录是否在搜索 */
	public static boolean isSearch = false;
		
}
