package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.special.HandTorchInGround;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.ray.hit.TerrainHitResult;
import com.gamefocal.rivenworld.game.recipes.weapons.TorchRecipe;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;

public class Torch extends ToolInventoryItem implements InventoryCraftingInterface, UsableInventoryItem {

    public Torch() {
        this.icon = InventoryDataRow.Torch;
        this.mesh = InventoryDataRow.Torch;
        this.name = "Torch";
        this.desc = "Used to provide light around your character";
        this.spawnNames.add("torch");
        this.initDurability(200);
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 15;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new TorchRecipe();
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        if(hitResult != null){
            if (TerrainHitResult.class.isAssignableFrom(hitResult.getClass())){
                return "[e] To Place on Ground";
            }
        }
        return null;
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {

        Location spawnAt = connection.getLookingAtTerrain();
        if (connection.getPlayer().location.dist(spawnAt) <= 100 * 4) {
            // Is within Range
            GameEntityModel model = DedicatedServer.instance.getWorld().spawn(new HandTorchInGround(), spawnAt, connection);
            if (model != null) {
                connection.getPlayer().equipmentSlots.inHand.clear();
                connection.getPlayer().equipmentSlots.inHand = null;
                connection.syncEquipmentSlots();
                connection.updatePlayerInventory();
            }
        }

        return true;
    }
}
