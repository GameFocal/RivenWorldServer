package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.entites.crops.CropEntity;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.EntityHitResult;

@Command(sources = "chat", name = "crop")
public class SetCropStageCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            HitResult hitResult = netConnection.getLookingAt();
            if (hitResult != null && EntityHitResult.class.isAssignableFrom(hitResult.getClass())) {

                GameEntity e = ((EntityHitResult) hitResult).get();

                if (CropEntity.class.isAssignableFrom(e.getClass())) {

                    // Is a crop plot
                    CropEntity c = (CropEntity) e;

                    if (c.getCropType() != null) {

                        if (message.args[0].equalsIgnoreCase("r")) {
                            c.setAutomatedCropStage(true);
                            c.resetGrowthStage();
                            return;
                        }

                        c.setGrowthStage(Integer.parseInt(message.args[0]));
                    }

                }

            }

        }
    }
}
