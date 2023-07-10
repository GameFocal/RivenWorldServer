package com.gamefocal.rivenworld.commands.chat.dev;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.NetworkService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.SaveService;
import com.gamefocal.rivenworld.threads.WorldStateThread;

@Command(name = "stats",sources = "chat")
public class ServerStatsCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            netConnection.sendChatMessage("---- Server Stats ----");
            netConnection.sendChatMessage("Players: " + DedicatedServer.get(PlayerService.class).players.size());
            netConnection.sendChatMessage("Net Connections: " + DedicatedServer.get(NetworkService.class).getServer().getConnections().size());
            netConnection.sendChatMessage("Entities: " + DedicatedServer.instance.getWorld().entityChunkIndex.size());
            netConnection.sendChatMessage("TPS: " + WorldStateThread.tps);
            netConnection.sendChatMessage("Save Queue: " + SaveService.saveQueue.size());
            netConnection.sendChatMessage("---------");
        }
    }
}
