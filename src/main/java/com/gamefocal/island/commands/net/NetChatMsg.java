package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.chat.ChatMsgEvent;
import com.gamefocal.island.service.PlayerService;

@Command(name = "msg", sources = "tcp")
public class NetChatMsg extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message);

        ChatMsgEvent event = new ChatMsgEvent(netConnection, String.join(",", message.args)).call();
        if (!event.isCanceled()) {
            // TODO: Default Logic to emit the chat message to players
            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                connection.sendTcp("chat|" + event.getFormattedMsg());
            }
        }
    }
}
