package com.gamefocal.rivenworld.game.items.weapons;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.inventory.CraftingRecipe;
import com.gamefocal.rivenworld.game.inventory.InventoryCraftingInterface;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.enums.InventoryDataRow;
import com.gamefocal.rivenworld.game.items.generics.ToolInventoryItem;
import com.gamefocal.rivenworld.game.items.generics.UsableInventoryItem;
import com.gamefocal.rivenworld.game.ray.HitResult;
import com.gamefocal.rivenworld.game.recipes.weapons.BuildingHammerRecipe;

public class BuildHammer extends ToolInventoryItem implements InventoryCraftingInterface, UsableInventoryItem {

    public BuildHammer() {
        this.icon = InventoryDataRow.BuildingHammer;
        this.mesh = InventoryDataRow.BuildingHammer;
        this.placable.DestructionMode = true;
        this.name = "Build Hammer";
        this.desc = "Used to destroy and repair buildings";
        this.spawnNames.add("hammer");
    }

    @Override
    public void generateUpperRightHelpText() {
        this.upperRightText.add("[RMB] Pickup Placeable");
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }

    @Override
    public CraftingRecipe canCraft(HiveNetConnection connection) {
        return new BuildingHammerRecipe();
    }

    @Override
    public String inHandTip(HiveNetConnection connection, HitResult hitResult) {
        return "[Right click] Destroy";
    }

    @Override
    public boolean onUse(HiveNetConnection connection, HitResult hitResult, InteractAction action, InventoryStack inHand) {
        return false;
    }
}
