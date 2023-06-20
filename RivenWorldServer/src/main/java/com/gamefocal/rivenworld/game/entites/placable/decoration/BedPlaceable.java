package com.gamefocal.rivenworld.game.entites.placable.decoration;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.UIIcon;
import com.gamefocal.rivenworld.game.ui.radialmenu.RadialMenuOption;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

import java.util.ArrayList;
import java.util.List;

public class BedPlaceable extends PlaceableEntity<BedPlaceable> {

    public BedPlaceable() {
        this.type = "bedPlaceable";
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
    public String onFocus(HiveNetConnection connection) {
        return "[q] Manage Bed";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.ALT) {
            List<RadialMenuOption> options = new ArrayList<>();

            BedPlaceable respawnBed = connection.getRespawnBed();
            if (respawnBed == null || respawnBed.uuid != this.uuid) {
                options.add(new RadialMenuOption("Set as Respawn", "link", UIIcon.LOCK));
            } else {
                options.add(new RadialMenuOption("Clear as Respawn", "unlink", UIIcon.UNLOCK));
            }

            connection.openRadialMenu(action1 -> {
                System.out.println(action1);
                if (action1.equalsIgnoreCase("link")) {
                    connection.setRespawnBed(this);
                } else if (action1.equalsIgnoreCase("unlink")) {
                    connection.clearRespawnBed();
                }
            }, options);
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 75, 50);
    }
}
