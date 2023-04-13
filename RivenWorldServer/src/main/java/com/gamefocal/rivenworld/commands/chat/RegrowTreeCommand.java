package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.resources.Stump;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.service.FoliageService;

@Command(name = "treegrow", sources = "tcp")
public class RegrowTreeCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            HitResult lookingAt = netConnection.getLookingAt();
            if (lookingAt != null && EntityHitResult.class.isAssignableFrom(lookingAt.getClass())) {

                GameEntity e = ((EntityHitResult) lookingAt).get();
                if (Stump.class.isAssignableFrom(e.getClass())) {
                    DedicatedServer.get(FoliageService.class).regrowTreeFromStump(e, true);
                } else {
                    System.out.println("Not a stump...");
                }

            } else {
                System.out.println("Not looking at entity");
            }

        }
    }
}
