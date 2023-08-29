package com.gamefocal.rivenworld.commands.chat.dev;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.world.WorldMetaData;

@Command(name = "cell", sources = "chat")
public class CellDebugCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            WorldCell cell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(netConnection.getPlayer().location.cpy());
            if (cell != null) {
                WorldMetaData meta = DedicatedServer.instance.getWorld().getRawHeightmap().getMetaDataFromXY(cell.getX(), cell.getY());

                netConnection.sendChatMessage("--- Cell Information ---");
                netConnection.sendChatMessage("Land Type: " + meta.getLandscapeType());
                netConnection.sendChatMessage("Forest: " + meta.getForest());
                netConnection.sendChatMessage("Biome: " + meta.getBiome());
                netConnection.sendChatMessage("Height: " + meta.getHeight());
                netConnection.sendChatMessage("--- End Cell Information ---");
            }
        }
    }
}
