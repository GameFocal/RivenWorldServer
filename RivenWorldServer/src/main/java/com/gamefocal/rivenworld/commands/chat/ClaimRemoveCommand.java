package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.service.DataService;

@Command(name = "claimr", sources = "chat")
public class ClaimRemoveCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            Location location = netConnection.getPlayer().location;

            WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(location);

            if (chunk != null) {

                GameChunkModel chunkModel = DataService.chunks.queryBuilder().where().eq("id", chunk.getChunkCords()).queryForFirst();
                if (chunkModel != null) {

                    if (chunkModel.entityModel != null) {
                        DedicatedServer.instance.getWorld().despawn(chunkModel.entityModel.entityData.uuid);
                        chunkModel.claim = null;
                        DataService.chunks.update(chunkModel);
                    }

                }
            }

        }
    }
}