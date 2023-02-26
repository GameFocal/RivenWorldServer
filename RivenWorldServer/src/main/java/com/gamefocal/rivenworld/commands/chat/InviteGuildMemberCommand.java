package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.DataService;

@Command(name = "gi", sources = "chat")
public class InviteGuildMemberCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        if (netConnection.getPlayer().guild != null && netConnection.getPlayer().guild.owner.uuid.equalsIgnoreCase(netConnection.getPlayer().uuid)) {

            String user = message.args[0];

            HiveNetConnection player = DedicatedServer.getPlayerFromName(user);
            if (player != null) {
                player.getPlayer().invitedToJoinGuild = netConnection.getPlayer().guild;
                DataService.players.update(player.getPlayer());

                netConnection.sendChatMessage(ChatColor.GREEN + "You've invited " + user + " to join your guild.");
                player.sendChatMessage(ChatColor.GREEN + "" + netConnection.getPlayer().displayName + " has invited you to join their guild, Press [g] to open the guild screen to accept.");
            }

        }

    }
}
