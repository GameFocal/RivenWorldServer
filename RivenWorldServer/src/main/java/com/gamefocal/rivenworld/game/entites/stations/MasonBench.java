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
import com.gamefocal.rivenworld.game.recipes.StoneBrickRecipe;
import com.gamefocal.rivenworld.game.recipes.blocks.Plaster.*;
import com.gamefocal.rivenworld.game.recipes.blocks.Stone.*;
import com.gamefocal.rivenworld.game.recipes.blocks.StoneBrick.*;
import com.gamefocal.rivenworld.game.recipes.placables.WaterWellRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.fence.rock_WallRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.StoneHatchetRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.StonePickaxeRecipe;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.ShapeUtil;

import java.util.LinkedList;

public class MasonBench extends PlaceableEntity<MasonBench> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Workbench", "Workbench", 6, 6);

    protected transient HiveNetConnection inUseBy = null;

    public MasonBench() {
        this.type = "MasonBench";
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
                // Stone Tools
                new StoneHatchetRecipe(),
                new StonePickaxeRecipe(),

                // Well
                new WaterWellRecipe(),

                // Plaster
                new PlasterBlockRecipe(),
                new PlasterHalfBlockRecipe(),
                new PlasterCornerBlockRecipe(),
                new PlasterBattlementBlockRecipe(),
                new PlasterBattlementCornerBlockRecipe(),
                new PlasterStairsBlockRecipe(),
                new PlasterRampBlockRecipe(),
                new PlasterTileBlockRecipe(),
                new PlasterWallBlockRecipe(),
                new PlasterRoundCornerBlockRecipe(),
                new Plaster1_4CircleBlockRecipe(),

                // StoneBrick+
                new StoneBrickRecipe(),
                new StoneBrickBlockRecipe(),
                new StoneBrickHalfBlockRecipe(),
                new StoneBrickCornerBlockRecipe(),
                new StoneBrickBattlementBlockRecipe(),
                new StoneBrickBattlementCornerBlockRecipe(),
                new StoneBrickStairsBlockRecipe(),
                new StoneBrickRampBlockRecipe(),
                new StoneBrickTileBlockRecipe(),
                new StoneBrickWallBlockRecipe(),
                new StoneBrickRoundCornerBlockRecipe(),
                new StoneBrick1_4CircleBlockRecipe(),

                // Stone
                new StoneBlockRecipe(),
                new StoneHalfBlockRecipe(),
                new StoneCornerBlockRecipe(),
                new StoneBattlementBlockRecipe(),
                new StoneBattlementCornerBlockRecipe(),
                new StoneStairsBlockRecipe(),
                new StoneRampBlockRecipe(),
                new StoneTileBlockRecipe(),
                new StoneWallBlockRecipe(),
                new StoneRoundCornerBlockRecipe(),
                new Stone1_4CircleBlockRecipe(),
                new rock_WallRecipe()
        );
    }
}
