package com.gamefocal.island.game.items.weapons;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.entites.resources.Stump;
import com.gamefocal.island.game.foliage.FoliageIntractable;
import com.gamefocal.island.game.foliage.FoliageState;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.interactable.Intractable;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;
import com.gamefocal.island.game.items.resources.wood.WoodLog;
import com.gamefocal.island.game.sounds.GameSounds;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.models.GameFoliageModel;
import com.gamefocal.island.service.DataService;
import com.gamefocal.island.service.FoliageService;
import com.gamefocal.island.service.TaskService;

import java.sql.SQLException;

public abstract class Hatchet extends ToolInventoryItem {

    public Hatchet() {
        this.isEquipable = true;
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {
        if (FoliageIntractable.class.isAssignableFrom(intractable.getClass())) {
            // Interact with foliage

            FoliageIntractable foliageIntractable = (FoliageIntractable) intractable;

            if (action == InteractAction.HIT) {
                // Hit the foliage

                GameFoliageModel foliageModel = foliageIntractable.getFoliageModel();

                if (foliageModel.foliageState == FoliageState.GROWN) {

                    float hitAmt = this.hit();
                    foliageModel.health -= hitAmt;

                    if (foliageModel.health <= 0) {
                        // Cut down the tree
                        try {
                            DataService.gameFoliage.update(foliageModel);

                            InventoryStack stack = new InventoryStack(new WoodLog(), (int) (DedicatedServer.get(FoliageService.class).getStartingHealth(foliageModel.modelName) / 2));

                            HiveTaskSequence hiveTaskSequence = new HiveTaskSequence(false);
                            hiveTaskSequence.await(20L);
                            hiveTaskSequence.exec(() -> {
                                connection.showFloatingTxt("-" + ((int) hitAmt), action.getInteractLocation());
                            }).exec((() -> {
                                DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TREE_HIT, action.getInteractLocation(), 5, 1f, 1f);
                            })).exec(() -> {
                                connection.getPlayer().inventory.add(stack);
                                connection.displayItemAdded(stack);
                            }).exec(() -> {
//                                foliageModel.syncToPlayer(connection, true);
                            }).exec(() -> {
                                Stump stump = new Stump();
                                DedicatedServer.instance.getWorld().spawn(stump, foliageModel.location);

                                foliageModel.foliageState = FoliageState.CUT;
                                foliageModel.growth = 0.00f;
                                foliageModel.attachedEntity = stump;

                                try {
                                    DataService.gameFoliage.update(foliageModel);
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                            });

                            TaskService.scheduleTaskSequence(hiveTaskSequence);

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    } else {
                        // Give some amount of wood

                        float amt = (hitAmt / 2);

                        InventoryStack stack = new InventoryStack(new WoodLog(), (int) amt);

                        HiveTaskSequence hiveTaskSequence = new HiveTaskSequence(false);
                        hiveTaskSequence.await(20L);
                        hiveTaskSequence.exec(() -> {
                            DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.TREE_HIT, action.getInteractLocation(), 5, 1f, 1f);
                        });
                        hiveTaskSequence.exec(() -> {
                            connection.showFloatingTxt("-" + ((int) hitAmt), action.getInteractLocation());
                        }).exec(() -> {
                            connection.getPlayer().inventory.add(stack);
                            connection.displayItemAdded(stack);
                            try {
                                DataService.gameFoliage.update(foliageModel);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }
                        });

                        TaskService.scheduleTaskSequence(hiveTaskSequence);
                    }
                }
            }

        }
    }
}
