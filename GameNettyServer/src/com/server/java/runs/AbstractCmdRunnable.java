package com.server.java.runs;

import java.util.concurrent.TimeUnit;

import com.server.java.entity.MsgEntity;
import com.server.java.queue.BaseQueue;

public abstract class AbstractCmdRunnable implements Runnable {
	private BaseQueue<MsgEntity> INSTANCE;

	public AbstractCmdRunnable(BaseQueue<MsgEntity> INSTANCE) {
		this.INSTANCE = INSTANCE;
	}

	@Override
	public void run() {
		for (;;) {
			try {
				MsgEntity msgEntity = null;
				while (INSTANCE.getQueueSize() > 0) {
					msgEntity = INSTANCE.take();
					if (msgEntity != null) {
						handleMsg(msgEntity);
					}
				}
				TimeUnit.MILLISECONDS.sleep(50);// 如果已经取完则让给其他线程一些时间片
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public abstract void handleMsg(MsgEntity msgEntity);

}
