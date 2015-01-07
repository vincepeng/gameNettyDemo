package com.server.java.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import com.server.java.entity.MsgEntity;
import com.server.java.queue.CommonQueue;
import com.server.java.queue.LoginQueue;

@Sharable
public class ServerHanlder extends SimpleChannelInboundHandler<MsgEntity> {

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, MsgEntity msg) throws Exception {

		if (msg == null) {
			return;
		}
		msg.setChannel(ctx.channel());

		// int playerid = ServerCache.get(ctx.channel());

		int csCommondCode = msg.getCmdCode();
		if (csCommondCode < 100) {// 100以内暂时不用

		} else if (csCommondCode >= 100 && csCommondCode < 200) { // 100-200用于注册
			LoginQueue.getInstance().put(msg);

		} else {// 消息可能较多,可以分几个队列,这里先放一个
			CommonQueue.getInstance().put(msg);
		}

	}
}
