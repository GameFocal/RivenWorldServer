package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.blocks.TestBlock;

import java.util.List;

@Command(name = "nearbydev",sources = "chat")
public class NearbyDevCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if(netConnection.isAdmin()) {

            TestBlock block = new TestBlock();
            block.location = netConnection.getPlayer().location;

            List<GameEntity> nearby = DedicatedServer.instance.getWorld().getCollisionManager().getNearbyEntities(block);

            for (GameEntity e : nearby) {
                netConnection.drawDebugBox(e.getBoundingBox(),1);
            }

        }
    }
}
