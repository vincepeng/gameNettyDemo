package com.server.java.runs;

import java.util.ArrayList;
import java.util.List;

import com.server.java.CmdHandler;
import com.server.java.entity.MsgEntity;
import com.server.java.handler.DemoHanlder;
import com.server.java.queue.BaseQueue;

public class HandleCmdRunnable extends AbstractCmdRunnable {

	public HandleCmdRunnable(BaseQueue<MsgEntity> INSTANCE) {
		super(INSTANCE);
	}

	private static CmdHandler CmdHandler = new DemoHanlder();// 这里命令码较少.暂时放置到同一个handler中.如果较多最好按照cmdCode分别放置存入hashMap中

	/**
	 * 处理消息的核心方法
	 * */
	@Override
	public void handleMsg(MsgEntity msgEntity) {
		List<MsgEntity> commandList = new ArrayList<MsgEntity>();// 用于存放处理后同时发送的几条消息
		try {
			CmdHandler.handleMsg(msgEntity, commandList);// 处理消息
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (commandList != null && commandList.size() > 0) {
			for (MsgEntity tempMessage : commandList) {
				System.out.println("发送数据指令" + tempMessage.getCmdCode());
				tempMessage.getChannel().writeAndFlush(tempMessage);// 发送消息
			}
		}
		commandList.clear();
		commandList = null;
	}
}
