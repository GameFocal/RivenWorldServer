package com.gamefocal.rivenworld.game.entites.stations;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.entites.generics.PassiveResourceStation;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.water.CleanWaterBucket;
import com.gamefocal.rivenworld.game.items.weapons.WoodBucket;
import com.gamefocal.rivenworld.game.ui.inventory.RivenPassiveStationUI;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

import java.util.concurrent.TimeUnit;

public class PassiveWaterWellStation extends PassiveResourceStation<PassiveWaterWellStation> implements InteractableEntity {

    public PassiveWaterWellStation() {
        this.type = "wellforwater";
        this.amt = 0;
        this.producesName = "Clean Water";
        this.producesDesc = "This well will generate clean water for drinking passively";

        this.amtPerTimeSpan = 1;
        this.timeSpanInSeconds = TimeUnit.MINUTES.toSeconds(5);
    }

    @Override
    public void onHash(StringBuilder builder) {
        builder.append(this.amt);
    }

    @Override
    public void onGather(HiveNetConnection connection, long amt) {
        if (amt <= this.amt) {
            amt = this.amt;
        }

        if (amt > this.canCollect(connection)) {
            amt = this.canCollect(connection);
        }

        // Change for water buckets
        for (InventoryStack s : connection.getPlayer().inventory.getOfType(WoodBucket.class)) {
            InventoryItem newItem = new CleanWaterBucket();
            newItem.setDurability(s.getItem().getDurability());
            s.setItem(newItem);
        }

//        InventoryStack stack = new InventoryStack(new CleanWaterBucket(), (int) amt);
//
//        if (connection.getPlayer().inventory.canAdd(stack)) {
//            connection.getPlayer().inventory.add(stack);
//            this.amt -= amt;
//        }
    }

    @Override
    public long canCollect(HiveNetConnection connection) {
        return connection.getPlayer().inventory.amtOfType(WoodBucket.class);
    }

    @Override
    public String helpText(HiveNetConnection connection) {
        return this.amt + " of clean water. Hold empty bucket in-hand to collect";
    }

    @Override
    public String onFocus(HiveNetConnection connection) {

        if(connection.getInHand() != null && WoodBucket.class.isAssignableFrom(connection.getInHand().getItem().getClass())) {
            return "[e] Fill With Water";
        }

//        if (this.inUse == null) {
//            return "[e] Use";
//        }
//
        return "Hold empty bucket to collect";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {

            if(this.amt > 0) {
                if(connection.getInHand() != null && WoodBucket.class.isAssignableFrom(connection.getInHand().getItem().getClass())) {
                    // Has a bucket
                    CleanWaterBucket bucket = new CleanWaterBucket();
                    bucket.setDurability(connection.getInHand().getItem().getDurability());

                    connection.getInHand().setItem(bucket);
                    connection.getInHand().setAmount(1);

                    this.amt--;

                    connection.updatePlayerInventory();
                    connection.syncEquipmentSlots();
                }
            }
        }
    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 100);
    }
}
