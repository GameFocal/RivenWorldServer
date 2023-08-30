package com.gamefocal.rivenworld.commands.net.world;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.NetWorldSyncPackage;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lowentry.ue4.library.LowEntry;

import java.util.Map;

@Command(name = "syncc", sources = "tcp")
public class SyncChunkVersions extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        /*
         * Sync chunks to this client if they are out of sync.
         * */

        NetWorldSyncPackage syncPackage = new NetWorldSyncPackage();

        WorldChunk c = DedicatedServer.instance.getWorld().getChunk(netConnection.getPlayer().location);
        String currentChunkHash = message.args[0];

        if(c != null) {
            if (!c.chunkHash().equalsIgnoreCase(currentChunkHash)) {

                // Make sure this is within the LOD
                if (netConnection.getLOD(c.getCenter()) <= 1) {
                    // Deload the chunk
                    syncPackage.clearChunk(c);

                    // Sync the chunk data
                    netConnection.syncChunkLOD(c, true, true, false, syncPackage);
                }
            }
        }

        if (syncPackage.hasData()) {
            netConnection.sendWorldStateSyncPackage(syncPackage);
        }
    }
}
