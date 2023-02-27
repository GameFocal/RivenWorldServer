package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.PlayerService;

@Command(name = "list", sources = "chat")
public class ListCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        StringBuilder builder = new StringBuilder();

        builder.append(ChatColor.BOLD).append(ChatColor.GREEN).append(DedicatedServer.get(PlayerService.class).players.values()).append(" Players Online:")
                .append(ChatColor.RESET);

        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
            builder.append(connection.getPlayer().displayName).append(" ");
        }

        netConnection.sendChatMessage(builder.toString());
    }
}
