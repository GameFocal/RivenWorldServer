package com.gamefocal.rivenworld.commands.net.ai;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.service.AiService;

import java.util.LinkedList;
import java.util.UUID;

@Command(name = "aipr", sources = "tcp")
public class NetAiPathResult extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        UUID eid = UUID.fromString(message.args[0]);
        if (DedicatedServer.get(AiService.class).pathFindingRequests.containsKey(eid)) {

            DedicatedServer.get(AiService.class).pathFindingRequests.get(eid).reply(netConnection, message.args);

            if (DedicatedServer.get(AiService.class).pathFindingRequests.get(eid).isComplete()) {

                LivingEntity e = (LivingEntity) DedicatedServer.instance.getWorld().getEntityFromId(eid).entityData;

                // Send data to the entity
                LinkedList<Location> p = DedicatedServer.get(AiService.class).pathFindingRequests.get(eid).getBestPath();

                GameEntityModel m = DedicatedServer.instance.getWorld().getEntityFromId(eid);
                if (m != null) {
                    LivingEntity livingEntity = (LivingEntity) m.entityData;
                    if (livingEntity.stateMachine != null) {
                        livingEntity.stateMachine.netSync(p);
                    }
                }
            }
        }
    }
}
