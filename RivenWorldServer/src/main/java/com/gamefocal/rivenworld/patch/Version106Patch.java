package com.gamefocal.rivenworld.patch;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.models.*;
import com.gamefocal.rivenworld.service.DataService;

import java.sql.SQLException;

public class Version106Patch implements ServerVersionPatch {
    @Override
    public float version() {
        return 1.051f;
    }

    @Override
    public void onPatch() {
        /*
         * Move all trees and entites down 3000 units
         * */
        try {

            /*
             * Entites
             * */
            for (GameEntityModel model : DataService.gameEntities.queryForAll()) {
                model.location.addZ(-3000);
                model.entityData.location.addZ(-3000);
                DataService.gameEntities.update(model);
            }

            /*
             * Trees
             * */
            for (GameFoliageModel foliageModel : DataService.gameFoliage.queryForAll()) {
                if (foliageModel.location != null) {
                    foliageModel.location.addZ(-3000);
                    DataService.gameFoliage.update(foliageModel);
                }
            }

            /*
             * Nodes
             * */
            for (GameResourceNode node : DataService.resourceNodes.queryForAll()) {
                if (node.realLocation != null) {
                    node.realLocation.addZ(-3000);
                    DataService.resourceNodes.update(node);
                }
            }

            /*
             * NPC
             * */
            for (GameNpcModel model : DataService.npcModels.queryForAll()) {
                model.location.addZ(-3000);
                DataService.npcModels.update(model);
            }

            /*
             * Beds
             * */
            for (PlayerBedModel bedModel : DataService.playerBedModels.queryForAll()) {
                bedModel.bedLocation.addZ(-3000);
                DataService.playerBedModels.update(bedModel);
            }

            /*
             * Players
             * */
            for (PlayerModel playerModel : DataService.players.queryForAll()) {
                playerModel.location.setZ(0);
                DataService.players.update(playerModel);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public boolean shouldPatch() {
        return DedicatedServer.getWorldFileVersion() < version();
    }
}
