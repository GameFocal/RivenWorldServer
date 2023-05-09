package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.RayService;

@Command(name = "loc", sources = "chat")
public class LocationCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            netConnection.sendChatMessage(ChatColor.BLUE + "Current Location: " + netConnection.getPlayer().location);
            System.out.println("[Loc-cmd]: " + netConnection.getPlayer().location);

            float height = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(netConnection.getPlayer().location);

            DedicatedServer.get(RayService.class).makeRequest(netConnection.getPlayer().location, 1, request -> {
                System.out.println("HM: " + height + ", UE:" + request.getReturnedHeight());
            });
        }
    }
}
