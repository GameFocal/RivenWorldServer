package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.entites.resources.nodes.*;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.ResourceService;

import java.util.HashMap;

@Command(name = "sr", sources = "chat")
public class SpawnResourceCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        // Ex: /sr {type} {delay}

        if (netConnection.isAdmin()) {
            // Can spawn the resource

            HashMap<String, Class<? extends ResourceNodeEntity>> types = new HashMap<>();
            types.put("stone", RockNode.class);
            types.put("coal", CoalNode.class);
            types.put("gold", GoldNode.class);
            types.put("iron", IronNode.class);
            types.put("dirt", DirtNode.class);
            types.put("sand", SandNode.class);
            types.put("oil", OilNode.class);

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
