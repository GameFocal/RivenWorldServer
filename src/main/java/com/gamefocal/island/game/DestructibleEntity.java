package com.gamefocal.island.game;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.items.generics.ToolInventoryItem;
import com.gamefocal.island.game.tasks.HiveTaskSequence;
import com.gamefocal.island.service.TaskService;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.Map;

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

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.HIT) {
            // Got hit.
            if (inHand != null) {
                if (inHand.getItem().isEquipable()) {

                    if (ToolInventoryItem.class.isAssignableFrom(inHand.getItem().getClass())) {
                        // Is a tool

                        ToolInventoryItem toolInventoryItem = (ToolInventoryItem) inHand.getItem();

                        float hit = toolInventoryItem.hit() * 2;

                        if (this.getModel().owner != null && this.getModel().owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                            // Is the same owner

                            System.out.println("OWNER");

                            Duration duration = new Duration(this.getModel().createdAt, new DateTime());

                            System.out.println("DIFF: " + duration.getStandardMinutes());

                            if (duration.getStandardMinutes() <= 5) {

                                // Remove with one hit.
                                this.health = 0;
                            }

                        }

                        this.health -= hit;

                        System.out.println("HIT: " + this.health);

                        HiveTaskSequence sequence = new HiveTaskSequence(false);
                        sequence.await(20L);
                        sequence.exec(() -> {
                            float percent = health / maxHealth;
                            connection.showFloatingTxt(Math.round(percent*100) + "%", action.getInteractLocation());
                        });

                        if (this.health <= 0) {
                            // Despawn
                            sequence.exec(() -> {
                                DedicatedServer.instance.getWorld().despawn(this.uuid);
                            });
                            sequence.exec(() -> {
                                // Return items to the ground...

                                if (this.getRelatedItem() != null) {
                                    // Get what this was made from and return 1/2
                                    connection.getPlayer().inventory.add(this.getRelatedItem());
                                }

                            });
                            sequence.await(5L);
                            sequence.exec(() -> {
                                connection.updateInventory(connection.getPlayer().inventory);
                            });
                        }

                        TaskService.scheduleTaskSequence(sequence);
                    }
                }
            }
        }
    }
}
