package com.gamefocal.rivenworld.game;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.tasks.HiveTaskSequence;
import com.gamefocal.rivenworld.service.TaskService;

public abstract class DestructibleEntity<T> extends GameEntity<T> implements InteractableEntity {

    protected float health = 100f;

    protected float maxHealth = 100f;

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getDamageValueMultiple(InventoryItem inHand) {
        return 1f;
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return null;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.HIT) {
            // Got hit.
            if (inHand != null) {
//                if (inHand.getItem().isEquipable()) {
//
//                    if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
//                        // Is a tool
//
//                        ToolInventoryItem toolInventoryItem = (ToolInventoryItem) inHand.getItem();
//
//                        float hit = toolInventoryItem.hit() * 2;
//
//                        this.health -= hit;
//
//                        System.out.println("HIT: " + this.health);
//
//                        HiveTaskSequence sequence = new HiveTaskSequence(false);
//                        sequence.await(20L);
//                        sequence.exec(() -> {
//                            float percent = health / maxHealth;
//                            connection.showFloatingTxt(Math.round(percent * 100) + "%", action.getInteractLocation());
//                        });
//
//                        if (this.health <= 0) {
//                            // Despawn
//                            sequence.exec(() -> {
//                                DedicatedServer.instance.getWorld().despawn(this.uuid);
//                            });
//                            sequence.exec(() -> {
//                                // Return items to the ground...
//
//                                if (this.getRelatedItem() != null) {
//                                    // Get what this was made from and return 1/2
//                                    connection.getPlayer().inventory.add(this.getRelatedItem());
//                                }
//
//                            });
//                            sequence.await(5L);
//                            sequence.exec(() -> {
//                                connection.updateInventory(connection.getPlayer().inventory);
//                            });
//                        }
//
//                        TaskService.scheduleTaskSequence(sequence);
//                    }
//                }
            }
        }
    }
}
