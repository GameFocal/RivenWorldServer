package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.game.entites.resources.nodes.*;
import com.gamefocal.rivenworld.game.generator.basic.MineralLayer;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.DataService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Command(name = "clearnodes", sources = "chat,cli")
public class ClearNodesCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

            /*
             * Clear All Resource nodes and respawn from file.
             * */
            DataService.exec(() -> {

                System.out.println("Resetting Resource Spawns...");

                ArrayList<Class> types = new ArrayList<>();
                types.add(CoalNode.class);
                types.add(CopperNode.class);
                types.add(DirtNode.class);
                types.add(GoldNode.class);
                types.add(GravelNode.class);
                types.add(IronNode.class);
                types.add(OilNode.class);
                types.add(RockNode.class);
                types.add(SandNode.class);

                /*
                 * Clear the resource spawn point
                 * */
                System.out.println("Clearing all nodes...");
                try {
                    List<GameResourceNode> nodes = DataService.resourceNodes.queryForAll();
                    for (GameResourceNode n : nodes) {
                        if (ResourceNodeEntity.class.isAssignableFrom(n.spawnEntity.getClass())) {
                            DataService.resourceNodes.delete(n);
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                /*
                 * Query all types of Resources
                 * */
                System.out.println("Despawn from world...");
                for (UUID u : DedicatedServer.instance.getWorld().entityChunkIndex.keySet()) {
                    GameEntityModel e = DedicatedServer.instance.getWorld().getEntityFromId(u);
                    if (e != null) {
                        if (ResourceNodeEntity.class.isAssignableFrom(e.entityData.getClass())) {
                            DedicatedServer.instance.getWorld().despawn(u);
                        }
                    }
                }

//                /*
//                 * Run the generator
//                 * */
//                System.out.println("Generating nodes...");
//                MineralLayer mineralLayer = new MineralLayer();
//                mineralLayer.generateLayer(DedicatedServer.instance.getWorld());

                System.out.println("Finished!");
            });

        }
    }
}
