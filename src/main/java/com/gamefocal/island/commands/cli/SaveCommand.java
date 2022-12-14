package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.models.PlayerModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.PlayerService;

@Command(name = "save", sources = "cli")
public class SaveCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println("Saving World...");
        DedicatedServer.instance.getWorld().save();

        System.out.println("Saving Players...");
        for (HiveNetConnection model : DedicatedServer.get(PlayerService.class).players.values()) {
            DataService.players.createOrUpdate(model.getPlayer());
        }

    }
}
