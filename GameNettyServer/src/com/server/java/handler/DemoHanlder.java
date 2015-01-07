package com.server.java.handler;

import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.server.java.CmdHandler;
import com.server.java.cache.ServerCache;
import com.server.java.constants.CmdConstant;
import com.server.java.entity.MsgEntity;
import com.server.java.entity.PlayerEntity;
import com.server.proto.demo.DemoProto.NameCheckReq;
import com.server.proto.demo.DemoProto.NameCheckResp;
import com.server.proto.demo.DemoProto.SayHelloReq;
import com.server.proto.demo.DemoProto.SayHelloResp;

public class DemoHanlder extends CmdHandler {

	@Override
	public void handleMsg(MsgEntity msgEntity, List<MsgEntity> commandList) {
		switch (msgEntity.getCmdCode()) {// 根据命令码对应找到对应处理方法
		case CmdConstant.NAME_CHECK:
			handleNameCheck(msgEntity, commandList);
			break;
		case CmdConstant.SAY_HELLO:
			handleSayHello(msgEntity, commandList);
			break;
		default:
			System.out.println("找不到对应的命令码");
		}
	}

	private void handleNameCheck(MsgEntity msgEntity, List<MsgEntity> commandList) {
		// com.server.proto.demo.
		NameCheckReq req = null;
		try {// 按照与客户端约定,指定命令码使用指定的解码class解码
				// 这里可以通过反射做成自动解码,参考:http://vincepeng.iteye.com/blog/2171310
			req = NameCheckReq.parseFrom(msgEntity.getData());
		} catch (InvalidProtocolBufferException e) {
			System.out.println("protobuf解码错误");
			e.printStackTrace();
			return;
		}
		String name = req.getName();
		if (name == null || name.isEmpty()) {
			return;
		}

		boolean isExist = ServerCache.CheckName(name);
		if (!isExist) {// 如果没有存在/则模拟注册
			ServerCache.addNewPlayer(name, msgEntity.getChannel());// 由于是单线程操作,无需加锁.参考:实战1的第2条
		}
		NameCheckResp.Builder resp = NameCheckResp.newBuilder();
		resp.setIsExist(isExist);
		msgEntity.setData(resp.build().toByteArray());// 将原来的消息内容替换为回包,命令码无需变化
		commandList.add(msgEntity);// 加入到发送数组
		if (!isExist) {
			SayHelloResp helloResp = SayHelloResp.newBuilder().setContent("欢迎" + name + "的到来").setSpeaker("系统").build();
			MsgEntity helloMsg = new MsgEntity();
			helloMsg.setCmdCode(CmdConstant.SAY_HELLO);
			helloMsg.setData(helloResp.toByteArray());
			ServerCache.sendToAll(helloMsg);// 此操作开销较大,一般不要如此或者分离到其它服务器
		}

	}

	private void handleSayHello(MsgEntity msgEntity, List<MsgEntity> commandList) {
		SayHelloReq req = null;
		try {
			req = SayHelloReq.parseFrom(msgEntity.getData());
		} catch (InvalidProtocolBufferException e) {
			System.err.println("protobuf解码错误");
			e.printStackTrace();
			return;
		}
		// 关键词过滤
		// 发言频率检测
		int playerId = ServerCache.get(msgEntity.getChannel());
		PlayerEntity pe = ServerCache.getPlayerById(playerId);
		if (pe != null) {
			SayHelloResp resp = SayHelloResp.newBuilder().setContent(req.getContent()).setSpeaker(pe.getName()).build();
			msgEntity.setData(resp.toByteArray());
			ServerCache.sendToAll(msgEntity);
		} else {
			System.err.println("玩家不存在");
		}

	}
}
