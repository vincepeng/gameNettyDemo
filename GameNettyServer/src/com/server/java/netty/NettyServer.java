package com.server.java.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyServer implements Runnable {
	private int port;

	public NettyServer(int port) {
		super();
		this.port = port;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void run() {
		try {// netty支持3种reactor,netty推荐主从(boss和worker)
			EventLoopGroup bossGroup = new NioEventLoopGroup(4);// acceptor,负责tcp接入,接入认证,创立socketChannel等;
			EventLoopGroup workerGroup = new NioEventLoopGroup();// netty默认设置:Runtime.getRuntime().availableProcessors()
																	// 负责io的读写和编码

			try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(bossGroup, workerGroup).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).channel(NioServerSocketChannel.class)
						.handler(new LoggingHandler(LogLevel.INFO)).childHandler(new NettyServerInitializer());

				ChannelFuture f = b.bind(port).sync();

				// Wait until the server socket is closed.
				// In this example, this does not happen, but you can do that to
				// gracefully
				// shut down your server.
				f.channel().closeFuture().sync();
			} finally {
				workerGroup.shutdownGracefully();
				bossGroup.shutdownGracefully();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
