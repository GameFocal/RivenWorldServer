package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.entites.generics.EntityStorageInterface;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.entites.placable.PlaceableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.inventory.InventoryType;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.recipes.Blocks.*;
import com.gamefocal.rivenworld.game.recipes.Placeables.*;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;

public class WorkBenchPlaceable extends PlaceableEntity<WorkBenchPlaceable> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Workbench", "Workbench", 6, 6);

    public WorkBenchPlaceable() {
        this.type = "workbenchPlaceable";
        this.inventory.setAttachedEntity(this.uuid);
//        this.inventory.setCraftingQueue(new CraftingQueue(6));
        this.inventory.getCraftingQueue().addAllowedRecipes(
                // Clay
                new ClayBlockRecipe(),

                // Plaster
                new PlasterBlockRecipe(),

                // Wood
                new WoodBlockRecipe(),
                new WoodHalfBlockRecipe(),
                new WoodCornerBlockRecipe(),
                new WoodBattlementBlockRecipe(),
                new WoodBattlementCornerBlockRecipe(),
                new WoodStairsBlockRecipe(),
                new WoodRampBlockRecipe(),

                // Thatch
                new ThatchBlockRecipe(),
                new ThatchHalfBlockRecipe(),
                new ThatchCornerBlockRecipe(),
                new ThatchStairsBlockRecipe(),
                new ThatchRampBlockRecipe(),

                // StoneBrick
                new StoneBrickBlockRecipe(),
                new StoneBrickHalfBlockRecipe(),
                new StoneBrickCornerBlockRecipe(),
                new StoneBrickBattlementBlockRecipe(),
                new StoneBrickBattlementCornerBlockRecipe(),
                new StoneBrickStairsBlockRecipe(),
                new StoneBrickRampBlockRecipe(),

                // Stone
                new StoneBlockRecipe(),
                new StoneHalfBlockRecipe(),
                new StoneCornerBlockRecipe(),
                new StoneBattlementBlockRecipe(),
                new StoneBattlementCornerBlockRecipe(),
                new StoneStairsBlockRecipe(),
                new StoneRampBlockRecipe(),

                // Storage
                new ChestPlaceableRecipe(),

                // Doors
                new DoorPlaceableRecipe(),
                new DoorPlaceable2Recipe(),
                new DoorPlaceable3Recipe(),

                // Decor
                new BedPlaceableRecipe(),
                new ChairPlaceableRecipe(),
                new TablePlaceableRecipe(),

                // Lights
                new StandOilLampPlaceableRecipe()
        );
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        this.inventory.getCraftingQueue().tick(null);
    }

    @Override
    public void onInventoryUpdated() {

    }

    @Override
    public void onInventoryOpen() {

    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        if (action == InteractAction.USE) {
            RivenCraftingUI ui = new RivenCraftingUI();
            ui.open(connection, this);
        }
    }

    @Override
    public void onInventoryClosed() {

    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Use";
    }

    @Override
    public Inventory dest() {
        return this.inventory;
    }

    @Override
    public Inventory fuel() {
        return null;
    }

    @Override
    public CraftingQueue queue() {
        return this.inventory.getCraftingQueue();
    }

    @Override
    public boolean isOn() {
        return true;
    }

    @Override
    public boolean hasFuel() {
        return false;
    }

    @Override
    public void toggleOnOff(HiveNetConnection connection) {

    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}
