package com.gamefocal.island.commands.chat;

import com.gamefocal.island.entites.net.*;

@Command(name = "loc",sources = "chat")
public class LocationCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        netConnection.sendChatMessage(ChatColor.BLUE + "Current Location: " + netConnection.getPlayer().location);
        System.out.println("[Loc-cmd]: " + netConnection.getPlayer().location);
    }
}
