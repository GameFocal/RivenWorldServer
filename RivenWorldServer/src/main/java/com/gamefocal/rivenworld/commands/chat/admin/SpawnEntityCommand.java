package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.AiService;

@Command(name = "se", sources = "chat")
public class SpawnEntityCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            // Ex: /se {entity}
            String entityName = message.args[0];
            if (AiService.types.containsKey(entityName)) {
                DedicatedServer.instance.getWorld().spawn(AiService.types.get(entityName).getClass().newInstance(), netConnection.getLookingAtTerrain());
            }
        }
    }
}
