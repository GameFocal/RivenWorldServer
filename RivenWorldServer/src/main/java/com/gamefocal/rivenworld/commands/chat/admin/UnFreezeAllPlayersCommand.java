package com.gamefocal.rivenworld.commands.chat.admin;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.PlayerService;

@Command(name = "unfreezeall", sources = "chat")
public class UnFreezeAllPlayersCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        boolean can = true;
        if (source == CommandSource.CHAT) {
            can = netConnection.isAdmin();
        }

        if (can) {
            for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                if (connection != null) {
                    connection.enableMovment();
                    connection.clearProgressBar();
                }
            }
        }
    }
}
