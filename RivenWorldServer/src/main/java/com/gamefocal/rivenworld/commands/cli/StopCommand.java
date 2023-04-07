package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.SaveService;
import org.apache.commons.lang3.ArrayUtils;

@Command(name = "stop", sources = "chat,cli")
public class StopCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        boolean can = true;
        if (source == CommandSource.CHAT) {
            can = netConnection.isAdmin();
        }

        if (can) {
            String msg = "Kicked from server";
            if (message.args.length > 0) {
                msg = String.join(" ", message.args);
            }

            DedicatedServer.sendChatMessageToAll(ChatColor.RED + "Server Stopping by Admin Command...");

            SaveService.saveGame();

            String finalMsg = msg;
            DataService.exec(() -> {
                DedicatedServer.kickAllPlayers(finalMsg);
            });

            DataService.exec(() -> {
                System.exit(0);
            });
        }
    }
}
