package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.models.GameGuildModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.DataService;

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
            builder.append(netConnection.getPlayer().displayName).append(":").append(message.argAsSingle());

            GameGuildModel gameGuildModel = netConnection.getPlayer().guild;
            DataService.guilds.refresh(gameGuildModel);

            for (PlayerModel playerModel : gameGuildModel.members) {
                if (playerModel.isOnline()) {
                    playerModel.getActiveConnection().sendChatMessage(builder.toString());
                }
            }

        }

    }
}
