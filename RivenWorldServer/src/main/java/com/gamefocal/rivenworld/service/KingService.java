package com.gamefocal.rivenworld.service;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Sphere;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.game.entites.special.KingWarChest;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameMetaModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentLinkedQueue;

@Singleton
@AutoService(HiveService.class)
public class KingService implements HiveService<KingService> {

    public static PlayerModel isTheKing;

    public static final Location warChestLocation = Location.fromString("64873.09,112786.195,24471.646,0.0,0.0,800.0");

    public static final Location throneLocation = Location.fromString("66274.984,113652.96,24641.021,0.0,0.0,-100.45145");

    public static final Location managmentTable = Location.fromString("64928.652,113465.336,24572.242,0.0,0.0,173.47516");

    public static KingWarChest warChest;

    public static float taxPer30Mins = 5;

    @Override
    public void init() {

    }

    public static BoundingBox throneBound() {
        return ShapeUtil.makeBoundBox(throneLocation.toVector(), 200, 500);
    }

    public static BoundingBox managmentBound() {
        return ShapeUtil.makeBoundBox(managmentTable.toVector(), 200, 500);
    }


}
