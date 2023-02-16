package com.gamefocal.island.commands.net.player;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.foliage.FoliageIntractable;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.FoliageService;

@Command(name = "fhit", sources = "tcp")
public class NetHitFoliage extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        DataService.exec(() -> {
            try {
                // A player has hit a foliage actor
                String name = message.args[0];
                String locStr = message.args[1];
                Integer hitIndex = Integer.valueOf(message.args[2]);
                Location hitLocation = Location.fromString(message.args[3]);

                System.out.println(message);

                Location loc = Location.fromString(locStr);

                String hash = FoliageService.getHash(name, locStr);

                GameFoliageModel f = DataService.gameFoliage.queryForId(hash);
                if (f == null) {

                    f = new GameFoliageModel();
                    f.uuid = hash;
                    f.modelName = name;
                    f.foliageIndex = hitIndex;
                    f.foliageState = FoliageState.GROWN;
                    f.health = DedicatedServer.get(FoliageService.class).getStartingHealth(name);
                    f.growth = 100;
                    f.location = Location.fromString(locStr);

                    DataService.gameFoliage.createOrUpdate(f);

                    System.out.println("New Foliage Detected...");
                }

                FoliageIntractable foliageIntractable = new FoliageIntractable(f);
                if (netConnection.getPlayer().equipmentSlots.getWeapon() != null) {
                    netConnection.getPlayer().equipmentSlots.getWeapon().getItem().onInteract(foliageIntractable, netConnection, InteractAction.HIT.setLocation(hitLocation));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
