package com.server.java.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ch.pipeline().addLast("decoder", new NettyMsgDecoder())// 解码器
				.addLast("encoder", new NettyMsgEncoder())// 编码器
				.addLast("handler", new ServerHanlder());

	}

}
