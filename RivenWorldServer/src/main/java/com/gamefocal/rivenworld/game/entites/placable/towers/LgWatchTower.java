package com.gamefocal.rivenworld.game.entites.placable.towers;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.items.weapons.Hatchet;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class LgWatchTower extends PlaceableEntity<LgWatchTower> {

    public LgWatchTower() {
        this.type = "logwatch_tower";
        this.health = 1500;
        this.maxHealth = 1500;
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
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(),100,200);
    }

    @Override
    public float getDamageValueMultiple(InventoryItem inHand) {
        if(Hatchet.class.isAssignableFrom(inHand.getClass())) {
            return 1f;
        }

        return .05f;
    }
}
