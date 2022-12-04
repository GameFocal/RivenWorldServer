package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.FoliageService;

@Command(name = "fhit", sources = "tcp")
public class HitFoliageCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        // A player has hit a foliage actor
        String name = message.args[0];
        String locStr = message.args[1];

        Location loc = Location.fromString(locStr);

        String hash = FoliageService.getHash(name, loc);

        GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
        if (f != null) {
            System.out.println("Foliage Hit: " + hash + ", " + f.foliageType + ", " + f.health + ", " + f.foliageState);
        } else {
            System.out.println("Unable to find Foliage by hash " + hash);
        }
    }
}
