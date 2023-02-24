package com.gamefocal.rivenworld.commands.net.gathering;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.ForageService;
import com.gamefocal.rivenworld.service.TaskService;

import java.util.ArrayList;
import java.util.List;

@Command(name = "ng", sources = "tcp")
public class NetGather extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        DataService.exec(() -> {
            try {
                String type = message.args[0];
                Location location = Location.fromString(message.args[1]);
                String misc = message.args[2];

                System.out.println(message.toString());

                List<InventoryStack> stacks = new ArrayList<>();

                if (type.equalsIgnoreCase("terrain")) {
                    // Forage from the ground.

                    GameSounds sfx = null;

                    if (misc.equalsIgnoreCase("Rocks")) {
                        sfx = GameSounds.FORAGE_ROCK;
                    } else if (misc.equalsIgnoreCase("Dirt")) {
                        sfx = GameSounds.FORAGE_DIRT;
                    } else if (misc.equalsIgnoreCase("Grass")) {
                        sfx = GameSounds.FORAGE_GRASS;
                    } else if (misc.equalsIgnoreCase("Sand")) {
                        sfx = GameSounds.FORAGE_SAND;
                    }

                    if (sfx != null) {
                        DedicatedServer.instance.getWorld().playSoundAtLocation(sfx, location, 5, 1f, 1f);
                    }

                    stacks = DedicatedServer.get(ForageService.class).forageGround(netConnection, misc, location);
                } else if (type.equalsIgnoreCase("foliage")) {
                    // Forage from a tree

                    String hash = FoliageService.getHash(misc, location.toString());
                    GameFoliageModel foliageModel = DataService.gameFoliage.queryForId(hash);

                    if (foliageModel == null) {
                        foliageModel = new GameFoliageModel();
                        foliageModel.uuid = hash;
                        foliageModel.modelName = misc;
                        foliageModel.foliageIndex = 0;
                        foliageModel.foliageState = FoliageState.GROWN;
                        foliageModel.health = DedicatedServer.get(FoliageService.class).getStartingHealth(misc);
                        foliageModel.growth = 100;
                        foliageModel.location = location;

                        DataService.gameFoliage.createOrUpdate(foliageModel);

                        System.out.println("New Foliage Detected...");
                    }

                    DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.FORAGE_TREE, location, 5, 1f, 1f);

                    stacks = DedicatedServer.get(ForageService.class).forageFoliage(netConnection, location, foliageModel);
                }

                // Send updates to client.
                HiveTaskSequence sequence = new HiveTaskSequence(false);
                sequence.await(20L);

                for (InventoryStack s : stacks) {
                    netConnection.getPlayer().inventory.add(s);
                    sequence.exec(() -> {
                        netConnection.displayItemAdded(s);
                    });
                }
                sequence.await(5L);
                sequence.exec(() -> {
                    netConnection.updateInventory(netConnection.getPlayer().inventory);
                });

                TaskService.scheduleTaskSequence(sequence);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}
