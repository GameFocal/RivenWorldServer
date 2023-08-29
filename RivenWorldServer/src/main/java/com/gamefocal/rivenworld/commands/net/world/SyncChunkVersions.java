package com.gamefocal.rivenworld.commands.net.world;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
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

        String rawJson = LowEntry.bytesToStringUtf8(LowEntry.decompressLzf(LowEntry.base64ToBytes(message.args[0])));

        JsonObject object = JsonParser.parseString(rawJson).getAsJsonObject();
        for (Map.Entry<String, JsonElement> m : object.entrySet()) {
            Location chunkCord = Location.fromString(m.getKey());
            int chunkVersion = Integer.parseInt(m.getValue().getAsString());

            if (chunkCord != null) {
                WorldChunk c = DedicatedServer.instance.getWorld().getChunk(chunkCord.getX(), chunkCord.getY());

                if(c.getVersion() != chunkVersion) {
                    // Send the chunk again to the player

                }

            }
        }
    }
}
