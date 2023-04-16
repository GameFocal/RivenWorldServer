package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.UUID;

@Command(name = "g", sources = "chat")
public class GuildChatCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (netConnection.getPlayer().guild != null) {

            boolean isLeader = false;
            if (netConnection.getPlayer().guild.owner.uuid.equalsIgnoreCase(netConnection.getPlayer().uuid)) {
                isLeader = true;
            }

            StringBuilder builder = new StringBuilder();
            builder.append(ChatColor.BLUE);
            builder.append("[G]:").append(message.argAsSingle());

            for (PlayerModel playerModel : netConnection.getPlayer().guild.members) {
                if (playerModel.isOnline()) {
                    playerModel.getActiveConnection().sendChatMessage(builder.toString());
                }
                playerModel.guild = null;
                DataService.players.createOrUpdate(playerModel);
            }

        }

    }
}
