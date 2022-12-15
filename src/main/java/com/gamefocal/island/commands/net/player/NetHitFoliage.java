package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.FoliageService;

@Command(name = "fhit", sources = "tcp")
public class NetHitFoliage extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // A player has hit a foliage actor
        String name = message.args[0];
        String locStr = message.args[1];

        System.out.println(message);

        Location loc = Location.fromString(locStr);

        String hash = FoliageService.getHash(name, Integer.parseInt(message.args[2].trim()));

        GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
        if (f != null) {
            System.out.println("Foliage Hit: " + hash + ", " + f.modelName + ", " + f.health + ", " + f.foliageState);
//            netConnection.sendTcp("fdel|" + f.hash);
        } else {
            System.out.println("Unable to find Foliage by hash " + hash);
        }
    }
}
