package com.gamefocal.rivenworld.commands.chat.admin;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.FoliageService;

import java.sql.SQLException;

@Command(name = "resettrees", sources = "chat")
public class ResetTreesCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.isAdmin()) {

            for (GameFoliageModel foliageModel : DedicatedServer.get(FoliageService.class).getFoliage().values()) {
                if (foliageModel.foliageState == FoliageState.GROWN) {
                    DataService.exec(() -> {
                        try {
                            if (foliageModel.attachedEntity != null) {
                                DedicatedServer.instance.getWorld().despawn(foliageModel.attachedEntity.uuid);
                            }

                            DataService.gameFoliage.delete(foliageModel);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    });
                }
            }

        }
    }
}
