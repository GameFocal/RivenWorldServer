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
import com.gamefocal.rivenworld.game.recipes.clothing.Fabric_R;
import com.gamefocal.rivenworld.game.recipes.clothing.chest.*;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.HeavyIronBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.MediumIronBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SimpleIronBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SteelBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.head.ClothCap_R;
import com.gamefocal.rivenworld.game.recipes.clothing.head.ClothCloak_R;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.*;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.LinkedList;

public class SpinningWheelStation extends PlaceableEntity<SpinningWheelStation> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Spinning Wheel", "SpinningWheel", 6, 6);

    protected transient LinkedList<HiveNetConnection> inUseBy = new LinkedList<>();

    public SpinningWheelStation() {
        this.type = "SpinningWheel";
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

                /*
                 * Shirts/ Tops
                 * */
                new Fabric_R(),

                /*
                 * Head
                 * */
                new ClothCloak_R(),
                new ClothCap_R(),

                // Cloth
                new SimpleClothShirt_R(),
                new FancyClothShirt_R(),

                /*
                 * Legs
                 * */
                new SimpleClothLegs_R(),
                new FancyClothLegs_R()
        );
    }
}
