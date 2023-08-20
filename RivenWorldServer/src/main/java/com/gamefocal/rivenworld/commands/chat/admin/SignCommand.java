package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.entites.prefab.Prefab;
import com.gamefocal.rivenworld.game.entites.signs.ReadableEntity;
import com.gamefocal.rivenworld.game.entites.signs.SignOnPole;
import com.gamefocal.rivenworld.game.entites.signs.TownSign;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.PrefabService;

@Command(name = "sign", sources = "chat")
public class SignCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            /*
             * Sign Command
             * /sign [make] [type, pole, lg]
             * /sign add [string]
             * /sign set [int] [string]
             * /sign clear
             * /sign rm [int]
             * /sign rot [rot]
             * /sign save
             * */

            String sub = message.args[0];
            ReadableEntity lookingAt = null;
            if (netConnection.getLookingAt() != null) {
                HitResult hitResult = netConnection.getLookingAt();
                if (EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {
                    if (ReadableEntity.class.isAssignableFrom(hitResult.get().getClass())) {
                        lookingAt = (ReadableEntity) hitResult.get();
                    }
                }
            }

            if (sub.equalsIgnoreCase("make")) {
                // Make a new sign by type
                String type = message.args[1];

                ReadableEntity entity = null;

                if (type.equalsIgnoreCase("pole")) {
                    entity = new SignOnPole();
                } else if (type.equalsIgnoreCase("lg")) {
                    entity = new TownSign();
                }

                DedicatedServer.instance.getWorld().spawn(entity, netConnection.getLookingAtTerrain());
            } else if (sub.equalsIgnoreCase("add")) {
                // Add a string to what sign we're looking at.
                if (lookingAt != null) {
                    String msg = message.getAsStringAtIndex(1);
                    lookingAt.addLine(msg);
                }
            } else if (sub.equalsIgnoreCase("set")) {
                int index = Integer.parseInt(message.args[1]);
                String msg = message.getAsStringAtIndex(2);

                if (lookingAt != null) {
                    lookingAt.addLineAtIndex(index, msg);
                }
            } else if (sub.equalsIgnoreCase("clear")) {
                if (lookingAt != null) {
                    lookingAt.clearLines();
                }
            } else if (sub.equalsIgnoreCase("rm")) {
                int index = Integer.parseInt(message.args[1]);
                if (lookingAt != null) {
                    lookingAt.clearLineAtIndex(index);
                }
            } else if (sub.equalsIgnoreCase("save")) {
                // Save to a prefab file for export

                Prefab prefab = new Prefab(new Location(0, 0, 0));
                for (ReadableEntity entity : DedicatedServer.instance.getWorld().getEntitesOfType(ReadableEntity.class)) {
                    prefab.addEntity(entity);
                }

                PrefabService.saveEntitesToPrefab(prefab, "world-signs");
            } else if (sub.equalsIgnoreCase("rot")) {
                if (lookingAt != null) {
                    lookingAt.location.setRotation(0, 0, Float.parseFloat(message.args[1]));
                }
            }

        }
    }
}
