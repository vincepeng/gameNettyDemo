package com.server.java.cache;

import gnu.trove.impl.sync.TSynchronizedObjectIntMap;
import gnu.trove.map.TObjectIntMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TObjectIntHashMap;
import io.netty.channel.Channel;

import com.server.java.entity.MsgEntity;
import com.server.java.entity.PlayerEntity;

public class ServerCache {
	/**
	 * 这里采用Trove,更省内存和额外的使用方法,甚至有类似lambda的forEach()方法
	 * */
	public final static TObjectIntMap<Channel> CHANNEL_PLAYER = new TSynchronizedObjectIntMap<Channel>(new TObjectIntHashMap<Channel>());
	public final static TIntObjectHashMap<PlayerEntity> PLAYERS_MAP = new TIntObjectHashMap<PlayerEntity>();
	public final static TObjectIntHashMap<String> NAME_MAPS = new TObjectIntHashMap<String>();

	public static int playerId = 1;

	/**
	 * 由于都是单线程队列方式,无需加锁
	 * */
	public static void addNewPlayer(String name, Channel ch) {
		PlayerEntity pe = new PlayerEntity();
		pe.setName(name);
		pe.setPlayerId(++playerId);
		pe.setCh(ch);
		CHANNEL_PLAYER.put(ch, playerId);
		PLAYERS_MAP.put(pe.getPlayerId(), pe);
		NAME_MAPS.put(name, 1);
	}

	public static int get(Channel channel) {
		return CHANNEL_PLAYER.get(channel);
	}

	public static boolean CheckName(String name) {
		return NAME_MAPS.contains(name);
	}

	public static PlayerEntity getPlayerById(int playerId) {
		return PLAYERS_MAP.get(playerId);
	}

	/**
	 * 给所有玩家发送消息
	 */
	public static void sendToAll(MsgEntity msgEntity) {
		Object[] players = PLAYERS_MAP.values();
		PlayerEntity pe = null;
		for (Object object : players) {
			pe = (PlayerEntity) object;
			if (pe.getCh().isWritable()) {
				pe.getCh().writeAndFlush(msgEntity);
			}
		}
	}
}
