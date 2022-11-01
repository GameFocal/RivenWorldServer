package com.gamefocal.island.entites.command;

import com.gamefocal.island.entites.net.HiveCommandMessage;
import com.gamefocal.island.entites.net.HiveReplyMessage;

public enum CommandFactory {

    ACK(true),
    NAK(false);

    protected HiveReplyMessage message;
    protected boolean success;

    CommandFactory(boolean success) {
        this.success = success;
        this.message = new HiveReplyMessage();
    }

    CommandFactory(boolean success, HiveCommandMessage commandMessage) {
        this.success = success;
        this.message = new HiveReplyMessage();
        this.message.id = commandMessage.id;
        this.message.to = commandMessage.to;
        this.message.cmd = "reply";
    }

    public CommandFactory set(String key, Object v) {
        this.message.data.put(key, v);
        return this;
    }

    public CommandFactory msg(String msg) {
        this.message.data.put("msg", msg);
        return this;
    }

    public CommandFactory attach(HiveCommandMessage commandMessage) {
        this.message.id = commandMessage.id;
        this.message.to = commandMessage.to;
        this.message.cmd = "reply";
        return this;
    }

    public HiveReplyMessage get() {
        return this.message;
    }

}
