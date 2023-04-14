package com.gamefocal.rivenworld.game.entites.placable.fence;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.generics.DoorEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class FenceWoodDoorPlaceable extends DoorEntity<FenceWoodDoorPlaceable> implements CollisionEntity {

    public FenceWoodDoorPlaceable() {
        this.type = "Fence_woodDoor";
        this.configureAlwaysLoaded();
        this.health = 200;
        this.maxHealth = 200;
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if(Hatchet.class.isAssignableFrom(inHand.getClass())) {
            return 1f;
        }

        return .05f;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(),50,100);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }
}
