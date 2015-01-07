package com.client;

public class Client2 extends Client {
	public static void main(String[] args) {
		Client client = new Client2();
		client.run();// 开启客户端netty

		// 发送名字检查
		client.sendNameCheckMsg();
	}

}