package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.storage.DropBag;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.DataService;

import java.util.UUID;

@Command(name = "cleanbags", sources = "chat,cli")
public class CleanBagCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

            /*
             * Clear All Resource nodes and respawn from file.
             * */
            DataService.exec(() -> {

                System.out.println("Cleaning all drop bags...");

                System.out.println("Despawn from world...");
                for (UUID u : DedicatedServer.instance.getWorld().entityChunkIndex.keySet()) {
                    GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(u);
                    if (e != null) {
                        if (DropBag.class.isAssignableFrom(e.entityData.getClass())) {
                            DedicatedServer.instance.getWorld().despawn(u);
                        }
                    }
                }

                System.out.println("Finished!");
            });

        }
    }
}
