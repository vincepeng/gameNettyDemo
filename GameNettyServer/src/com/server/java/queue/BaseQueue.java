package com.server.java.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 所有消息队列的基类,组件优于继承
 * */
public class BaseQueue<T> {

	/** 消息队列 */
	private final BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

	/**
	 * 不阻塞,即刻返回;没有
	 * */
	public T take() {
		return queue.poll();
	}

	/**
	 * 阻塞,消息不能丢掉
	 * */
	public void put(T t) {
		try {
			queue.put(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public int getQueueSize() {
		return queue.size();
	}

}
