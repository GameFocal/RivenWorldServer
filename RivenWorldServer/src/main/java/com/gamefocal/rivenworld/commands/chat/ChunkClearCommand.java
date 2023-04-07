package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;

@Command(name = "cclear", sources = "chat")
public class ChunkClearCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(netConnection.getPlayer().location);
            if (chunk != null) {
                for (GameEntityModel m : chunk.getEntites().values()) {
                    DedicatedServer.instance.getWorld().despawn(m.uuid);
                }
            }
        }
    }
}
