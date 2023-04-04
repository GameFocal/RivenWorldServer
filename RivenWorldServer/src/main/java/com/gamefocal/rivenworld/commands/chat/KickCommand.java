package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.DataService;
import org.apache.commons.lang3.ArrayUtils;

@Command(name = "kick", sources = "chat,cli")
public class KickCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        boolean can = true;
        if (source == CommandSource.CHAT) {
            can = netConnection.isAdmin();
        }

        if (can) {

            String toKick = message.args[0];
            String msg = String.join(" ", ArrayUtils.subarray(message.args, 1, message.args.length - 1));

            HiveNetConnection connection = DedicatedServer.getPlayerFromName(toKick);
            if (connection != null) {

                DataService.players.createOrUpdate(connection.getPlayer());

                connection.kick(msg);
            }

        }

    }
}
