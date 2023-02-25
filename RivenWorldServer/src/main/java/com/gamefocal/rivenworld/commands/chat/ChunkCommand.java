package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.util.Location;

@Command(name = "chunk", sources = "chat")
public class ChunkCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        Location location = netConnection.getPlayer().location;
        WorldChunk chunk = DedicatedServer.instance.getWorld().getChunk(location);
        if (chunk != null) {

            netConnection.sendChatMessage(ChatColor.ORANGE + "Chunk: " + chunk.getChunkCords().toString());
            System.out.println(chunk.getChunkCords().toString());

        }
    }
}
