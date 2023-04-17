package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.resources.Stump;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;
import com.gamefocal.rivenworld.game.ray.hit.FoliageHitResult;
import com.gamefocal.rivenworld.service.FoliageService;

@Command(name = "tree", sources = "chat")
public class TreeCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            String sub = message.args[0];

            HitResult lookingAt = netConnection.getLookingAt();
            if (lookingAt != null) {
                if (EntityHitResult.class.isAssignableFrom(lookingAt.getClass())) {

                    /*
                     * Looking at Net Entity
                     * */
                    if (sub.equalsIgnoreCase("grow")) {
                        GameEntity e = ((EntityHitResult) lookingAt).get();
                        if (Stump.class.isAssignableFrom(e.getClass())) {
                            DedicatedServer.get(FoliageService.class).regrowTreeFromStump(e, true);
                        }
                    }

                } else if (FoliageHitResult.class.isAssignableFrom(lookingAt.getClass())) {
                    /*
                     * Looking at tree
                     * */

                    FoliageHitResult foliageHitResult = (FoliageHitResult) lookingAt;

                    if (sub.equalsIgnoreCase("name")) {
                        netConnection.sendChatMessage("Tree Model " + foliageHitResult.getName());
                        System.out.println("Tree Model: " + foliageHitResult.getName());
                    } else if (sub.equalsIgnoreCase("del")) {
                        DedicatedServer.get(FoliageService.class).remove(foliageHitResult);
                    } else if (sub.equalsIgnoreCase("grow")) {
                        DedicatedServer.get(FoliageService.class).growTree(foliageHitResult);
                    }

                }
            }
        }
    }
}
