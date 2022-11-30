package com.gamefocal.island.events;

import com.gamefocal.island.entites.events.Event;
import com.gamefocal.island.entites.net.HiveNetConnection;

public class ChatMsgEvent extends Event<ChatMsgEvent> {

    private HiveNetConnection connection;

    private String msg;

    private String formattedMsg = "";

    public ChatMsgEvent(HiveNetConnection connection, String msg) {
        this.connection = connection;
        this.msg = msg;
    }

    public HiveNetConnection getConnection() {
        return connection;
    }

    public String getMsg() {
        return msg;
    }

    public String getFormattedMsg() {
        return formattedMsg;
    }

    public void setFormattedMsg(String formattedMsg) {
        this.formattedMsg = formattedMsg;
    }
}
