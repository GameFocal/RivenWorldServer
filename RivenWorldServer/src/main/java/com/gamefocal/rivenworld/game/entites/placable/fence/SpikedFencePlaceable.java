package com.gamefocal.rivenworld.game.entites.placable.fence;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.CollisionEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class SpikedFencePlaceable extends PlaceableEntity<SpikedFencePlaceable> implements CollisionEntity {

    public SpikedFencePlaceable() {
        this.type = "SpikedFencePlaceable";
        this.health = 800;
        this.maxHealth = 800;
        this.configureAlwaysLoaded();
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
        return ShapeUtil.makeBoundBox(this.location.toVector(),100,100);
    }

    @Override
    public void takeDamage(float amt) {
        this.health -= amt;
    }
}
