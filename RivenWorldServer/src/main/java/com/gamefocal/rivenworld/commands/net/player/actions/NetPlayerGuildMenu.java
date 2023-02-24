package com.gamefocal.rivenworld.commands.net.player.actions;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.ui.guild.GuildUI;
import com.gamefocal.rivenworld.models.GameGuildModel;

@Command(name = "guit", sources = "tcp")
public class NetPlayerGuildMenu extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println(message.toString());

        GameUI open = netConnection.getOpenUI();
        if (open == null) {
            // We open a new UI
            // Check if this player is in a guild
            GameGuildModel model = netConnection.getPlayer().guild;

            GuildUI guildUI = new GuildUI();
            guildUI.open(netConnection, model);
        } else if (GuildUI.class.isAssignableFrom(open.getClass())) {
            netConnection.getOpenUI().close(netConnection);
        }

    }
}
