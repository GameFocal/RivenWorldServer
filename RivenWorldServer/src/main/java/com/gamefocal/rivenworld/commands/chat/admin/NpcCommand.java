package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.WorldChunk;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameNpcModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.NpcService;
import com.gamefocal.rivenworld.service.TaskService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;

@Command(name = "npc", sources = "chat")
public class NpcCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            String cmd = message.args[0];
            if (cmd.equalsIgnoreCase("spawn")) {
                // Spawn a new NPC based on the current player location

                String type = message.args[1];
                if (NpcService.npcTypes.containsKey(type)) {

                    Class<? extends NPC> c = NpcService.npcTypes.get(type);

                    GameNpcModel npcModel = DedicatedServer.get(NpcService.class).registerNpc(c, netConnection.getPlayer().location);

                    TaskService.scheduledDelayTask(() -> {
                        DedicatedServer.get(NpcService.class).spawnNpc(npcModel);
                    }, 20L, false);
                }

            } else if (cmd.equalsIgnoreCase("save")) {

                JsonObject o = new JsonObject();
                List<GameNpcModel> npcModels = DataService.npcModels.queryForAll();
                for (GameNpcModel model : npcModels) {
                    if(!o.has(model.npcType)) {
                        o.add(model.npcType,new JsonArray());
                    }

                    o.get(model.npcType).getAsJsonArray().add(model.location.toString());
                }

                Files.write(Paths.get("npc.json"), o.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

            } else if (cmd.equalsIgnoreCase("del")) {

                HitResult hr = netConnection.getLookingAt();
                if (EntityHitResult.class.isAssignableFrom(hr.getClass())) {

                    if (LivingEntity.class.isAssignableFrom(hr.get().getClass())) {

                        LivingEntity livingEntity = (LivingEntity) hr.get();

                        GameNpcModel npcModel = DataService.npcModels.queryBuilder().where().eq("spawnedId", livingEntity.uuid).queryForFirst();
                        if (npcModel != null) {
                            DedicatedServer.instance.getWorld().despawn(livingEntity.uuid);
                            DataService.npcModels.delete(npcModel);
                        }
                    }
                }
            }
        }
    }
}
