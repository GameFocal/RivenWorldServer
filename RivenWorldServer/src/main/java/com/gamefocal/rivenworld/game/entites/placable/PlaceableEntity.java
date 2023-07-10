package com.gamefocal.rivenworld.game.entites.placable;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.DestructibleEntity;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.generics.EntityStorageInterface;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingJob;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.service.InventoryService;

public abstract class PlaceableEntity<T> extends DestructibleEntity<T> implements CollisionEntity {
    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }
}
