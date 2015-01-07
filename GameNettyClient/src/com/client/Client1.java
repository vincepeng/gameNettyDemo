package com.client;

public class Client1 extends Client {

	public static void main(String[] args) {
		Client client = new Client1();
		client.run();// 开启客户端netty

		// 发送名字检查
		client.sendNameCheckMsg();
	}
}
