package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.entites.net.*;

@Command(name = "loc", sources = "chat")
public class LocationCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            netConnection.sendChatMessage(ChatColor.BLUE + "Current Location: " + netConnection.getPlayer().location);
            System.out.println("[Loc-cmd]: " + netConnection.getPlayer().location);
        }
    }
}
