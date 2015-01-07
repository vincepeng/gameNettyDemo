package com.server.java.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.server.java.netty.NettyServer;
import com.server.java.queue.CommonQueue;
import com.server.java.queue.LoginQueue;
import com.server.java.runs.HandleCmdRunnable;

public class GameServer {
	private final static int port = 9090;

	public static void main(String[] args) {
		// 初始化本地处理逻辑线程
		ExecutorService exec = Executors.newCachedThreadPool();
		exec.execute(new HandleCmdRunnable(CommonQueue.getInstance()));// 处理通用命令码,具体用意参考第2条(http://vincepeng.iteye.com/blog/2171581)
		exec.execute(new HandleCmdRunnable(LoginQueue.getInstance()));// 处理登录命令码
		// 初始化netty
		exec.execute(new NettyServer(port));

		// 初始化数据库,redis等其它操作

	}
}
