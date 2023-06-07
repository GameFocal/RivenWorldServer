package com.gamefocal.rivenworld.game.entites.stations;

import com.badlogic.gdx.math.collision.BoundingBox;
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
import com.gamefocal.rivenworld.game.recipes.blocks.Log.LogBlockRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Wood.*;
import com.gamefocal.rivenworld.game.recipes.placables.RainGatheringRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.ChairPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.decoration.TablePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.fence.*;
import com.gamefocal.rivenworld.game.recipes.placables.towers.LgWatchTower_R;
import com.gamefocal.rivenworld.game.recipes.placables.towers.SmWatchTower_R;
import com.gamefocal.rivenworld.game.recipes.weapons.StickRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

import java.util.LinkedList;

public class WoodBench extends PlaceableEntity<WoodBench> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Lumber Bench", "Workbench", 6, 6);

    protected transient LinkedList<HiveNetConnection> inUseBy = new LinkedList<>();

    public WoodBench() {
        this.type = "lumber-bench";
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
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 50, 50);
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
                new PlankRecipe(),

                // Stations
                new RainGatheringRecipe(),

                new StickRecipe(),

                // Wood
                new WoodBlockRecipe(),
                new WoodHalfBlockRecipe(),
                new WoodCornerBlockRecipe(),
                new WoodBattlementBlockRecipe(),
                new WoodBattlementCornerBlockRecipe(),
                new WoodStairsBlockRecipe(),
                new WoodRampBlockRecipe(),
                new LogBlockRecipe(),

                //Fence
                new FencePlaceable1Recipe(),
                new FencePlaceable2Recipe(),
                new FencePlaceable3Recipe(),
                new FencePlaceable4Recipe(),
                new FencePlaceable5Recipe(),
                new FencePlaceable6Recipe(),
                new DefensiveFencePlaceableRecipe(),
                new DefensiveSpikedFencePlaceableRecipe(),
                new FenceDoorPlaceableRecipe(),
                new FenceWoodDoorPlaceableRecipe(),
                new FenceLogDoorPlaceableRecipe(),
                new SmallSpikedFencePlaceableRecipe(),
                new SpikedFencePlaceableRecipe(),
                new TallDefensiveSpikedFencePlaceableRecipe(),
                new TallFencePlaceableRecipe(),

                // Towers
                new SmWatchTower_R(),
                new LgWatchTower_R(),
                new ChairPlaceableRecipe(),
                new TablePlaceableRecipe()
        );
    }
}
