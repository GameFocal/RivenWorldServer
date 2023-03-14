package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.entites.resources.nodes.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.ResourceService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;

@Command(name = "sr", sources = "chat")
public class SpawnResourceCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // Ex: /sr {type} {delay}

        if (netConnection.isAdmin()) {
            // Can spawn the resource

            if (message.args[0].equalsIgnoreCase("export")) {

                HashMap<String, ArrayList<String>> locs = new HashMap<>();

                for (GameResourceNode resourceNode : DataService.resourceNodes.queryForAll()) {
                    if (ResourceNodeEntity.class.isAssignableFrom(resourceNode.spawnEntity.getClass())) {
                        // Is a node :)

                        String k = resourceNode.spawnEntity.getClass().getSimpleName();
                        String locStr = resourceNode.location.toString();

                        if (!locs.containsKey(k)) {
                            locs.put(k, new ArrayList<>());
                        }

                        locs.get(k).add(locStr);
                    }
                }

                JsonObject d = DedicatedServer.gson.toJsonTree(locs, HashMap.class).getAsJsonObject();

                Files.write(Paths.get("resource-nodes.json"), d.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
                return;
            }

            HashMap<String, Class<? extends ResourceNodeEntity>> types = new HashMap<>();
            types.put("stone", RockNode.class);
            types.put("coal", CoalNode.class);
            types.put("gold", GoldNode.class);
            types.put("iron", IronNode.class);
            types.put("dirt", DirtNode.class);
            types.put("sand", SandNode.class);
            types.put("oil", OilNode.class);
            types.put("copper", CopperNode.class);

            if (netConnection.getLookingAtTerrain() != null) {

                Location tHit = netConnection.getLookingAtTerrain();

                String type = message.args[0];
                Integer delay = Integer.valueOf(message.args[1]);

                if (!types.containsKey(type)) {
                    netConnection.sendChatMessage("" + ChatColor.RED + "No node by the name of " + type);
                    return;
                }

                ResourceNodeEntity nodeEntity = types.get(type).newInstance();

                DedicatedServer.get(ResourceService.class).addNode(nodeEntity, tHit, delay);
            }
        }
    }
}
