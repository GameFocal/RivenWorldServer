package com.gamefocal.island.entites.command;


import com.gamefocal.island.entites.net.HiveCommandMessage;
import com.gamefocal.island.entites.net.HiveReplyMessage;

public abstract class HiveCommandPromise implements Runnable {

    protected HiveCommandMessage commandMessage;
    protected HiveReplyMessage replyMessage;

    public HiveCommandPromise(HiveCommandMessage commandMessage) {
        this.commandMessage = commandMessage;
    }

    public void setReplyMessage(HiveReplyMessage replyMessage) {
        this.replyMessage = replyMessage;
    }

    public HiveCommandMessage getCommandMessage() {
        return commandMessage;
    }

    public HiveReplyMessage getReplyMessage() {
        return replyMessage;
    }
}
