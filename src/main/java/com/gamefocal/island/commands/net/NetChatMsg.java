package com.gamefocal.island.commands.net;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.events.chat.ChatMsgEvent;
import com.gamefocal.island.service.CommandService;
import com.gamefocal.island.service.PlayerService;

@Command(name = "msg", sources = "tcp")
public class NetChatMsg extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (message.args.length > 0) {
            String body = message.args[0];

            ChatMsgEvent msgEvent = new ChatMsgEvent(netConnection, message.args[0]);
            if (msgEvent.isCanceled()) {
                return;
            }

            // See if the message is a command
            if (body.trim().charAt(0) == '/') {
                // Is a command

                String[] parts = body.split(" ");

                StringBuilder builder = new StringBuilder();
                for (String p : parts) {
                    builder.append(p).append("|");
                }

                // Check for a valid command
                DedicatedServer.get(CommandService.class).handleCommand(builder.toString(), CommandSource.CHAT, netConnection);

                System.out.println("CMD PROC");

            } else {
                // Relay this message to everyone.
                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                    connection.sendChatMessage(body);
                }

                System.out.println("MSG Sent");
            }
        }
    }
}
