package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;

@Command(name = "eloc", sources = "chat")
public class EntityDataCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            HitResult hr = netConnection.getLookingAt();
            if (hr != null) {

                if (EntityHitResult.class.isAssignableFrom(hr.getClass())) {

                    // Looking at a entity
                    EntityHitResult hitResult = (EntityHitResult) hr;

                    GameEntity e = hitResult.get();

                    System.out.println("=== ENTITY DATA DUMP ===");
                    System.out.println("UUID: " + e.uuid);
                    System.out.println("LOC: " + e.location.toString());
                    System.out.println("TYPEOF: " + e.type);
                    System.out.println("=== END ENTITY DATA ===");

                    netConnection.sendChatMessage(ChatColor.GREEN + "NetEntity (" + e.uuid.toString() + ") at " + e.location.toString() + " of type " + e.type);
                }

            }
        }
    }
}
