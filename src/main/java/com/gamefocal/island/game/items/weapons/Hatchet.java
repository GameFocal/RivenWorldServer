package com.gamefocal.island.game.items.weapons;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.foliage.FoliageIntractable;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.TaskService;

import java.sql.SQLException;

public abstract class Hatchet extends ToolInventoryItem {

    public Hatchet() {
        this.isEquipable = true;
    }

    @Override
    public String slug() {
        return "Stone_Hatchet";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if (FoliageIntractable.class.isAssignableFrom(intractable.getClass())) {
            // Interact with foliage

            FoliageIntractable foliageIntractable = (FoliageIntractable) intractable;

            if (action == InteractAction.HIT) {
                // Hit the foliage

                GameFoliageModel foliageModel = foliageIntractable.getFoliageModel();

                float hitAmt = this.hit();
                foliageModel.health -= hitAmt;

                TaskService.scheduledDelayTask(() -> {
                    connection.showFloatingTxt("Hit Tree", action.getInteractLocation());
                }, 20L, false);

                if (foliageModel.health < 0) {
                    // Cut down the tree
                    foliageModel.foliageState = FoliageState.CUT;
                    foliageModel.growth = 0.00f;
                    try {
                        DataService.gameFoliage.update(foliageModel);

                        // TODO: Spawn wood to the player

                        // TODO: Trigger Inventory Popup for wood being added

                        foliageModel.syncToPlayer(connection);

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    // Give some amount of wood

                }

            }

        }
    }

    public abstract float hit();
}
