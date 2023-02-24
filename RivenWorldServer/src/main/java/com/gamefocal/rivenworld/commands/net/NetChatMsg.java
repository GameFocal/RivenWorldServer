package com.gamefocal.rivenworld.commands.net;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.events.chat.ChatMsgEvent;
import com.gamefocal.rivenworld.service.CommandService;
import com.gamefocal.rivenworld.service.PlayerService;

@Command(name = "msg", sources = "tcp")
public class NetChatMsg extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (message.args.length > 0) {

            String body = message.argAsString();

            ChatMsgEvent msgEvent = new ChatMsgEvent(netConnection, message.args[0]);
            if (msgEvent.isCanceled()) {
                return;
            }

            // See if the message is a command
            if (body.trim().charAt(0) == '/') {
                // Is a command

                body = body.replaceFirst("^\\/", "");

                String[] parts = body.split(" ");

                StringBuilder builder = new StringBuilder();
                for (String p : parts) {
                    builder.append(p).append("|");
                }

                System.out.println(builder.toString());

                // Check for a valid command
                DedicatedServer.get(CommandService.class).handleCommand(builder.toString(), CommandSource.CHAT, netConnection);

                System.out.println("CMD PROC");

            } else {
                // Relay this message to everyone.

                StringBuilder m = new StringBuilder();
                m.append(ChatColor.BOLD).append(ChatColor.ORANGE);
                m.append(netConnection.getPlayer().displayName);
                m.append(": ");
                m.append(ChatColor.RESET).append(body);

//                String nb = "" + ChatColor.BOLD + ChatColor.ORANGE + "[" + netConnection.getPlayer().displayName + "]:" + ChatColor.RESET + " " + body;

                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                    connection.sendChatMessage(m.toString());
                }

                System.out.println("MSG Sent");
            }
        }
    }
}
