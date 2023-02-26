package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.Stump;
import com.gamefocal.rivenworld.game.foliage.FoliageIntractable;
import com.gamefocal.rivenworld.game.foliage.FoliageState;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.resources.wood.WoodLog;
import com.gamefocal.rivenworld.game.player.Animation;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.models.GameFoliageModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.FoliageService;
import com.gamefocal.rivenworld.service.TaskService;

import java.sql.SQLException;

public abstract class Pickaxe extends ToolInventoryItem {

    public Pickaxe() {
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
                                connection.playAnimation(Animation.SWING_AXE);
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
