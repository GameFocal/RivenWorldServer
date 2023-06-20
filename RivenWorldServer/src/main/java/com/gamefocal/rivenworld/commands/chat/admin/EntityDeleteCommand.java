package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;

@Command(name = "edel", sources = "chat")
public class EntityDeleteCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {
            HitResult hr = netConnection.getLookingAt();
            if (hr != null) {
                if (EntityHitResult.class.isAssignableFrom(hr.getClass())) {
                    DedicatedServer.instance.getWorld().despawn(((EntityHitResult) hr).get().uuid);
                }
            }
        }
    }
}
