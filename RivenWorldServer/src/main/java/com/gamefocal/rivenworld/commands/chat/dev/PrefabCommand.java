package com.gamefocal.rivenworld.commands.chat.dev;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.entites.prefab.Prefab;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.game.world.WorldChunk;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.PrefabService;

import java.util.UUID;

@Command(name = "prefab", sources = "chat")
public class PrefabCommand extends HiveCommand {

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // prefab a
        // prefab b
        // prefab save {name}

        if (message.args.length >= 1) {
            String sub = message.args[0];

            if (sub.equalsIgnoreCase("a")) {
                PrefabService.a = netConnection.getPlayer().location.cpy();
                netConnection.sendChatMessage("Position A Saved.");
            } else if (sub.equalsIgnoreCase("b")) {
                PrefabService.b = netConnection.getPlayer().location.cpy();
                netConnection.sendChatMessage("Position B Saved.");
            } else if (sub.equalsIgnoreCase("save")) {

                if (PrefabService.a == null || PrefabService.b == null) {
                    netConnection.sendChatMessage("You need a A and B point");
                    return;
                }

                String saveName = UUID.randomUUID().toString();
                if (message.args.length == 2) {
                    saveName = message.args[1];
                }

                BoundingBox boundingBox = ShapeUtil.createBoundingBoxFromLocations(PrefabService.a, PrefabService.b);

                // Capture all the entites in this area

                Prefab prefab = new Prefab(PrefabService.a);

//                ArrayList<GameEntity> toSave = new ArrayList<>();
                for (WorldChunk c : DedicatedServer.instance.getWorld().getChunksInBox(boundingBox)) {
                    for (GameEntityModel e : c.getEntites().values()) {
                        if (ShapeUtil.isLocationInsideWithTolerance(boundingBox, e.entityData.location.toVector(), 300)) {
                            prefab.addEntity(e.entityData);
                        }
                    }
                }

                if (prefab.hasData()) {
                    PrefabService.saveEntitesToPrefab(prefab, saveName);
                    netConnection.sendChatMessage("Prefab Saved! (" + prefab.getEntities().size() + ")");
                } else {
                    netConnection.sendChatMessage("No entities found in box");
                }

                PrefabService.a = null;
                PrefabService.b = null;
            } else if (sub.equalsIgnoreCase("load")) {
                String name = message.args[1];
                PrefabService.loadEntitesFromPrefab(name);
            }

        }
    }
}
