package com.server.java.queue;

import com.server.java.entity.MsgEntity;

/**
 * <p>
 * 处理登录注册,避免同时注册名字或者登录账号冲突,当然也可以使用synchronized将检测的nameCheck(登录名检车)和nickCheck(
 * 昵称检车)方法锁定
 * </p>
 * */
public class LoginQueue extends BaseQueue<MsgEntity> {
	// private BaseQueue<MsgEntity> baseQueue = new BaseQueue<MsgEntity>();
	private static final LoginQueue INSTANCE = new LoginQueue();

	private LoginQueue() {
	}

	public static LoginQueue getInstance() {
		return INSTANCE;
	}

}
