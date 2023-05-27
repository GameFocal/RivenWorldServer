package com.gamefocal.rivenworld.game.entites.placable;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.placables.items.decoration.Target_Item;
import com.gamefocal.rivenworld.game.tasks.HiveTask;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.TaskService;

public class AttackDummyEntity extends DestructibleEntity<AttackDummyEntity> implements TickEntity, CollisionEntity {

    public transient HiveTask cancelTask = null;

    public AttackDummyEntity() {
        this.type = "dummy";
        this.maxHealth = 500;
        this.health = 500;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 300);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
//        for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//            connection.drawDebugBox(Color.RED, this.getBoundingBox(), 1);
//        }
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);

        if (action == InteractAction.HIT) {
            System.out.println("HIT");
        }
    }
}
