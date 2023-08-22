package com.gamefocal.rivenworld.game.entites.lights;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.game.entites.generics.FuelEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.items.resources.misc.Oil;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class TorchPlaceable extends FuelEntity<TorchPlaceable> {

    public TorchPlaceable() {
        this.type = "TorchPlaceable";
        this.fuelSources.put(Oil.class, 300f);
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }
}
