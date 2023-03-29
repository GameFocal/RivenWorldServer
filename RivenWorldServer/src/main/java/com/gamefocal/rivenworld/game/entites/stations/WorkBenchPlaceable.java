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
import com.gamefocal.rivenworld.game.recipes.Placeables.decoration.BedPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.decoration.ChairPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.decoration.TablePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.doors.DoorPlaceable2Recipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.doors.DoorPlaceable3Recipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.doors.DoorPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.fence.*;
import com.gamefocal.rivenworld.game.recipes.PlankRecipe;
import com.gamefocal.rivenworld.game.recipes.Weapons.BuildingHammerRecipe;
import com.gamefocal.rivenworld.game.recipes.Weapons.WoodenSwordRecipe;
import com.gamefocal.rivenworld.game.recipes.WoodBucketRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.LinkedList;

public class WorkBenchPlaceable extends PlaceableEntity<WorkBenchPlaceable> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Workbench", "Workbench", 6, 6);

    protected transient LinkedList<HiveNetConnection> inUseBy = new LinkedList<>();

    public WorkBenchPlaceable() {
        this.type = "workbenchPlaceable";
        this.inventory.setAttachedEntity(this.uuid);
//        this.inventory.setCraftingQueue(new CraftingQueue(6));
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {
        if (this.inUseBy.size() > 0) {
            if (this.inventory.getCraftingQueue().tick(this.inUseBy.getFirst())) {
                this.inventory.updateUIs();
            }
        } else {
            if (this.inventory.getCraftingQueue().tick(null)) {
            }
        }
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
            if (this.inUseBy.size() == 0) {
                RivenCraftingUI ui = new RivenCraftingUI();
                ui.open(connection, this);
            }
        }
    }

    @Override
    public void onInventoryClosed() {

    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        if (this.inUseBy.size() > 0) {
            return "In use by someone";
        } else {
            return "[e] Use";
        }
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

    @Override
    public void onUse(HiveNetConnection connection) {
        if (this.inUseBy == null) {
            this.inUseBy = new LinkedList<>();
        }

        this.inUseBy.add(connection);
    }

    @Override
    public void onLeave(HiveNetConnection connection) {
        this.inUseBy.remove(connection);
    }

    @Override
    public void getRecipes() {
        this.inventory.getCraftingQueue().addAllowedRecipes(
                // Materials
                new PlankRecipe(),

                // Stone Tools
                new WoodenSwordRecipe(),
                new BuildingHammerRecipe(),
                new WoodBucketRecipe(),
//                new StoneHatchetRecipe(),
//                new StonePickaxeRecipe(),

                // Land Claim
                new LandClaimPlaceableRecipe(),

                // Storage
                new ChestPlaceableRecipe(),

                // Doors
                new DoorPlaceableRecipe(),
                new DoorPlaceable2Recipe(),
                new DoorPlaceable3Recipe(),

                //Fence
                new FencePlaceable1Recipe(),
                new FencePlaceable2Recipe(),
                new FencePlaceable3Recipe(),
                new FencePlaceable4Recipe(),
                new FencePlaceable5Recipe(),
                new FencePlaceable6Recipe(),
                new DefenciveFencePlaceableRecipe(),
                new DefenciveSpikedFencePlaceableRecipe(),
                new FenceDoorPlaceableRecipe(),
                new SmallSpikedFencePlaceableRecipe(),
                new SpikedFencePlaceableRecipe(),
                new TallDefenciveSpikedFencePlaceableRecipe(),
                new TallFencePlaceableRecipe(),

                // Decor
                new BedPlaceableRecipe(),
                new ChairPlaceableRecipe(),
                new TablePlaceableRecipe(),

                // Lights
                new StandOilLampPlaceableRecipe(),

                // Other Stations
                new MasonBenchRecipe(),
                new FurnacePlaceableRecipe(),
                new ForgePlaceableRecipe(),

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

                // Clay
                new ClayBlockRecipe()
        );
    }
}
