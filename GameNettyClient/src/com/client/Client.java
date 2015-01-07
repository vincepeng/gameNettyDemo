package com.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import com.server.java.constants.CmdConstant;
import com.server.java.entity.MsgEntity;
import com.server.proto.demo.DemoProto.NameCheckReq;
import com.server.proto.demo.DemoProto.SayHelloReq;

public abstract class Client {
	private Channel sendchannel = null;
	private final static String host = "localhost";
	private final static int port = 9090;

	public void run() {

		// Configure the client.
		EventLoopGroup group = new NioEventLoopGroup();
		try {

			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ClientChannelInitializer(this));// 这里为了简便直接使用内部类

			// Start the client.
			ChannelFuture f = b.connect(host, port).sync();
			sendchannel = f.channel();
			if (f.isDone()) {
				System.out.println("连接成功");
			}

		} catch (InterruptedException e) {// 链接失败
			e.printStackTrace();
		} finally {
		}

	}

	public void sendNameCheckMsg() {
		String className = Thread.currentThread().getStackTrace()[Thread.currentThread().getStackTrace().length - 1].getClassName();
		String simpleClassName = className.substring(className.lastIndexOf('.') + 1);
		NameCheckReq req = NameCheckReq.newBuilder().setName(simpleClassName).build();// 直接以当前内名字作为发言人姓名
		MsgEntity nameCheckMsg = new MsgEntity();
		nameCheckMsg.setCmdCode(CmdConstant.NAME_CHECK);
		nameCheckMsg.setData(req.toByteArray());
		sendMsg(nameCheckMsg);
	}

	public void sendHello(String content) {
		SayHelloReq req = SayHelloReq.newBuilder().setContent(content).build();
		MsgEntity helloMsg = new MsgEntity();
		helloMsg.setCmdCode(CmdConstant.SAY_HELLO);
		helloMsg.setData(req.toByteArray());
		sendMsg(helloMsg);
	}

	public void sendMsg(MsgEntity msgEntity) {
		sendchannel.writeAndFlush(msgEntity);
		System.out.println("发送数据成功,命令码:\t" + msgEntity.getCmdCode());
	}
}
