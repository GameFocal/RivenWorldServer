package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.PlayerService;

import java.sql.SQLException;

@Command(name = "save", sources = "cli,chat")
public class SaveCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

            System.out.println("Saving World...");
            DedicatedServer.instance.getWorld().save();

            System.out.println("Saving Players...");
            for (HiveNetConnection model : DedicatedServer.get(PlayerService.class).players.values()) {
                DataService.exec(() -> {
                    try {
                        DataService.players.createOrUpdate(model.getPlayer());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                });
            }
        }
    }
}
