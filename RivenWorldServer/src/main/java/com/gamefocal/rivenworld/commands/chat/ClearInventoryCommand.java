package com.gamefocal.rivenworld.commands.chat;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.AiService;

@Command(name = "clear", sources = "chat")
public class ClearInventoryCommand extends HiveCommand {

    public static Location[] points = new Location[2];

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            netConnection.getPlayer().inventory.clearInv();
            netConnection.updatePlayerInventory();
            netConnection.syncEquipmentSlots();
        }
    }
}
