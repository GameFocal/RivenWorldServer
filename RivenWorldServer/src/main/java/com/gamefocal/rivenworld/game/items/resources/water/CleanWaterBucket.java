package com.gamefocal.rivenworld.game.items.resources.water;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.InventoryItem;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.ray.HitResult;

public class CleanWaterBucket extends InventoryItem implements UsableInventoryItem {

    public CleanWaterBucket() {
        this.icon = InventoryDataRow.Wooden_Bucket;
        this.mesh = InventoryDataRow.Wooden_Bucket;
        this.name = "Bucket with Clean Water";
        this.desc = "A bucket filled with Clean Water that you can use for drinking or cooking";
        this.tint = Color.CYAN;
        this.spawnNames.add("water");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        return null;
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        return false;
    }
}
