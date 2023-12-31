package com.gamefocal.rivenworld.game.entites.stations;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
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
import com.gamefocal.rivenworld.game.recipes.PlankRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Clay.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Dirt.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Sand.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Thatch.*;
import com.gamefocal.rivenworld.game.recipes.placables.*;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.*;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable2Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable3Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceable5Recipe;
import com.gamefocal.rivenworld.game.recipes.placables.doors.DoorPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.BuildingHammerRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodHoeRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodenSwordRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

public class WorkBenchPlaceable extends PlaceableEntity<WorkBenchPlaceable> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Workbench", "Workbench", 6, 6);

    protected transient HiveNetConnection inUseBy = null;

    public WorkBenchPlaceable() {
        this.type = "workbenchPlaceable";
        this.inventory.setAttachedEntity(this.uuid);
//        this.inventory.setCraftingQueue(new CraftingQueue(6));
    }

    @Override
    public void onHash(StringBuilder builder) {
        builder.append(this.inventory.toJson());
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 50);
    }

    @Override
    public void onTick() {
        if (this.inUseBy != null) {
            if (this.inventory.getCraftingQueue().tick(this.inUseBy)) {
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
            if (this.inUseBy == null) {
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
        if (this.inUseBy != null) {
            if (!DedicatedServer.playerIsOnline(this.inUseBy.getUuid())) {
                this.inUseBy = null;
            }
        }

        if (this.inUseBy != null) {
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
        this.inUseBy = connection;
    }

    @Override
    public void onLeave(HiveNetConnection connection) {
        this.inUseBy = null;
    }

    @Override
    public void getRecipes() {
        this.inventory.getCraftingQueue().addAllowedRecipes(
                // Materials
                new PlankRecipe(),

                // Stone Tools
                new WoodenSwordRecipe(),
                new BuildingHammerRecipe(),
                new WoodHoeRecipe(),
//                new WoodBucketRecipe(),
//                new StoneHatchetRecipe(),
//                new StonePickaxeRecipe(),

                // Bow & Arrow
//                new BasicBowRecipe(),
                new Target_Recipe(),
                new Dummy_Recipe(),

                // Land Claim
                new LandClaimPlaceableRecipe(),

                // Storage
                new SimpleChestPlaceableRecipe(),
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

//                //Fence
//                new FencePlaceable1Recipe(),
//                new FencePlaceable2Recipe(),
//                new FencePlaceable3Recipe(),
//                new FencePlaceable4Recipe(),
//                new FencePlaceable5Recipe(),
//                new FencePlaceable6Recipe(),
////                new DefenciveFencePlaceableRecipe(),
//                new DefenciveSpikedFencePlaceableRecipe(),
//                new FenceDoorPlaceableRecipe(),
//                new FenceWoodDoorPlaceableRecipe(),
//                new FenceLogDoorPlaceableRecipe(),
//                new SmallSpikedFencePlaceableRecipe(),
//                new SpikedFencePlaceableRecipe(),
//                new TallDefenciveSpikedFencePlaceableRecipe(),
//                new TallFencePlaceableRecipe(),

//                // Towers
//                new SmWatchTower_R(),
//                new LgWatchTower_R(),

                // Decor
                new BedPlaceableRecipe(),
//                new ChairPlaceableRecipe(),
//                new TablePlaceableRecipe(),

                // Lights
//                new StandOilLampPlaceableRecipe(),

                // Other Stations
                new MasonBenchRecipe(),
                new WoodBenchRecipe(),
                new TanningRackPlaceableRecipe(),
                new FletcherBenchPlaceableRecipe(),
                new FurnacePlaceableRecipe(),
                new ForgePlaceableRecipe(),
                new SpinningWheel_R(),

//                // Wood
//                new WoodBlockRecipe(),
//                new WoodHalfBlockRecipe(),
//                new WoodCornerBlockRecipe(),
//                new WoodBattlementBlockRecipe(),
//                new WoodBattlementCornerBlockRecipe(),
//                new WoodStairsBlockRecipe(),
//                new WoodRampBlockRecipe(),
//                new LogBlockRecipe(),

                // Thatch
                new ThatchBlockRecipe(),
                new ThatchHalfBlockRecipe(),
                new ThatchCornerBlockRecipe(),
                new ThatchBattlementBlockRecipe(),
                new ThatchBattlementCornerBlockRecipe(),
                new ThatchStairsBlockRecipe(),
                new ThatchRampBlockRecipe(),
                new ThatchTileBlockRecipe(),
                new ThatchWallBlockRecipe(),
                new ThatchRoundCornerBlockRecipe(),
                new Thatch1_4CircleBlockRecipe(),

                // Clay
                new ClayBlockRecipe(),
                new ClayHalfBlockRecipe(),
                new ClayCornerBlockRecipe(),
                new ClayBattlementBlockRecipe(),
                new ClayBattlementCornerBlockRecipe(),
                new ClayStairsBlockRecipe(),
                new ClayRampBlockRecipe(),
                new ClayTileBlockRecipe(),
                new ClayWallBlockRecipe(),
                new ClayRoundCornerBlockRecipe(),
                new Clay1_4CircleBlockRecipe(),

                //Dirt
                new DirtHalfBlockRecipe(),
                new DirtCornerBlockRecipe(),
                new DirtBattlementBlockRecipe(),
                new DirtBattlementCornerBlockRecipe(),
                new DirtStairsBlockRecipe(),
                new DirtRampBlockRecipe(),
                new DirtTileBlockRecipe(),
                new DirtWallBlockRecipe(),
                new DirtRoundCornerBlockRecipe(),
                new Dirt1_4CircleBlockRecipe(),

                //Sand
                new SandHalfBlockRecipe(),
                new SandCornerBlockRecipe(),
                new SandBattlementBlockRecipe(),
                new SandBattlementCornerBlockRecipe(),
                new SandStairsBlockRecipe(),
                new SandRampBlockRecipe(),
                new SandTileBlockRecipe(),
                new SandWallBlockRecipe(),
                new SandRoundCornerBlockRecipe(),
                new Sand1_4CircleBlockRecipe()
        );
    }
}
