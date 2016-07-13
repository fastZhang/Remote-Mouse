package com.zcl.board.utils;

/**
 * 字节和int的转换工具类
 * @author ZL
 * @version 2016-5-25 上午11:17:52
 */
public class ByteUtils {

	/**
	 * 将2个整数转成由两个字节组成的有符号的数据类型
	 * @param x  x偏移量
	 * @param y  y偏移量
	 * @return 长度为2的byte数组
	 */
	public static byte[] xyTo2Bytes(int x, int y) {
		byte[] res = new byte[2];
		if (x < -127)
			x = -127;
		if (x > 127)
			x = 127;
		if (y < -127)
			y = -127;
		if (y > 127)
			y = 127;
		res[0] = (byte) ((x & 0x7f) | ((x & 0x80000000) >> 24));// 取最后7位，取出第一位
		res[1] = (byte) ((y & 0x7f) | ((y & 0x80000000) >> 24));// 取最后7位，取出第一位
		return res;
	}

	/**
	 * 动作转成字节表示指令
	 * @param action
	 * @return
	 */
	public static byte[] actionTo2Bytes(int action) {
		byte[] res = new byte[2];
		res[0] = (byte) action;// 无符号的数据类型,其他情况都默认为0；
		res[1] = -128;// 标记特殊含义
		return res;
	}

}
