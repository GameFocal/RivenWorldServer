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
import com.gamefocal.rivenworld.game.recipes.blocks.*;
import com.gamefocal.rivenworld.game.recipes.placables.*;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.*;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable2Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable3Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable5Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.fence.*;
import com.gamefocal.rivenworld.game.recipes.PlankRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.towers.LgWatchTower_R;
import com.gamefocal.rivenworld.game.recipes.placables.towers.SmWatchTower_R;
import com.gamefocal.rivenworld.game.recipes.weapons.BasicBowRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.BuildingHammerRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodenSwordRecipe;
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

                // Bow & Arrow
                new BasicBowRecipe(),
                new Target_Recipe(),

                // Land Claim
                new LandClaimPlaceableRecipe(),

                // Storage
                new ChestPlaceableRecipe(),

                // Doors
                new DoorPlaceableRecipe(),
                new DoorPlaceable2Recipe(),
                new DoorPlaceable3Recipe(),
                new DoorPlaceable5Recipe(),

                //Gates
                new GatePlaceableRecipe(),

                //Jail
                new jail_1_Recipe(),
                new jail_3_Recipe(),

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
                new FenceWoodDoorPlaceableRecipe(),
                new FenceLogDoorPlaceableRecipe(),
                new SmallSpikedFencePlaceableRecipe(),
                new SpikedFencePlaceableRecipe(),
                new TallDefenciveSpikedFencePlaceableRecipe(),
                new TallFencePlaceableRecipe(),

                // Towers
                new SmWatchTower_R(),
                new LgWatchTower_R(),

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
                new SpinningWheel_R(),

                // Wood
                new WoodBlockRecipe(),
                new WoodHalfBlockRecipe(),
                new WoodCornerBlockRecipe(),
                new WoodBattlementBlockRecipe(),
                new WoodBattlementCornerBlockRecipe(),
                new WoodStairsBlockRecipe(),
                new WoodRampBlockRecipe(),
                new LogBlockRecipe(),

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
