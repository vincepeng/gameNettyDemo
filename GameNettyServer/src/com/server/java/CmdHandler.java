package com.server.java;

import java.util.List;

import com.server.java.entity.MsgEntity;

public abstract class CmdHandler {
	public abstract void handleMsg(MsgEntity msgEntity, List<MsgEntity> commandList);
}
