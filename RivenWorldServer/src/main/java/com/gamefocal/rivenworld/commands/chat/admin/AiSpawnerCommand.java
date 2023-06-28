package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ai.AiSpawn;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.AiService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Command(name = "ais", sources = "chat")
public class AiSpawnerCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            String animal = message.args[0];

            if (animal.equalsIgnoreCase("save")) {
                DedicatedServer.get(AiService.class).exportToFile();
                return;
            }

            if (animal.equalsIgnoreCase("respawn")) {
                for (UUID uuid : DedicatedServer.get(AiService.class).trackedEntites) {
                    GameEntityModel m = DedicatedServer.instance.getWorld().getEntityFromId(uuid);
                    if (m != null) {
                        boolean isTrackedAnimal = false;
                        for (LivingEntity l : AiService.types.values()) {
                            if (l.getClass().isAssignableFrom(m.entityData.getClass())) {
                                isTrackedAnimal = true;
                                break;
                            }
                        }

                        if (isTrackedAnimal) {
                            DedicatedServer.instance.getWorld().despawn(uuid);
                        }
                    }
                }

                return;
            }

            if (!AiService.types.containsKey(animal)) {
                return;
            }

            // Make or get the spawner
            AiSpawn spawn = null;
            if (DedicatedServer.get(AiService.class).spawners.containsKey(animal)) {
                spawn = DedicatedServer.get(AiService.class).spawners.get(animal);
                spawn.getLocations().add(netConnection.getLookingAtTerrain());
            } else {
                int amount = Integer.parseInt(message.args[1]);
                int delayInSeconds = Integer.parseInt(message.args[2]);

                spawn = new AiSpawn(AiService.types.get(animal).getClass(), 0L, amount, delayInSeconds, netConnection.getLookingAtTerrain());
                DedicatedServer.get(AiService.class).spawners.put(animal, spawn);
            }
        }
    }
}
