package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.entites.resources.nodes.*;
import com.gamefocal.rivenworld.game.generator.basic.MineralLayer;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameNpcModel;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.NpcService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Command(name = "resetnpc", sources = "chat,cli")
public class ResetNPCCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

            /*
             * Clear All Resource nodes and respawn from file.
             * */
            DataService.exec(() -> {

                System.out.println("Resetting NPC...");

                /*
                 * Clear the resource spawn point
                 * */
                System.out.println("Clearing all NPCs...");
                try {
                    List<GameNpcModel> nodes = DataService.npcModels.queryForAll();
                    for (GameNpcModel n : nodes) {
                        DataService.npcModels.delete(n);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                System.out.println("Despawn from world...");
                for (UUID u : DedicatedServer.instance.getWorld().entityChunkIndex.keySet()) {
                    GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(u);
                    if (e != null) {
                        if (NPC.class.isAssignableFrom(e.entityData.getClass())) {
                            DedicatedServer.instance.getWorld().despawn(u);
                        }
                    }
                }

                System.out.println("Spawning NPCs.");
                DedicatedServer.get(NpcService.class).load();

                System.out.println("Finished!");
            });

        }
    }
}
