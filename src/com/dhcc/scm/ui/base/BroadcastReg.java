package com.dhcc.scm.ui.base;

/**
 * 规范Activity中广播接受者注册的接口协议<br>
 * 
 * <b>创建时间</b> 2015-12-14
 *
 * @version 1.0
 */
public interface BroadcastReg {
	/**
	 * 注册广播
	 */
	void registerBroadcast();

	/**
	 * 解除注册广播
	 */
	void unRegisterBroadcast();
}
