package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.entites.living.Deer;
import com.gamefocal.rivenworld.game.entites.living.npc.ShopkeeperNPC;

import java.util.HashMap;

@Command(name = "se", sources = "chat")
public class SpawnEntityCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            HashMap<String, LivingEntity> livingEntity = new HashMap<>();
            livingEntity.put("deer", new Deer());
            livingEntity.put("shop1", new ShopkeeperNPC());

            // Ex: /se {entity}
            String entityName = message.args[0];
            if (livingEntity.containsKey(entityName)) {
                DedicatedServer.instance.getWorld().spawn(livingEntity.get(entityName), netConnection.getLookingAtTerrain());
            }
        }
    }
}
