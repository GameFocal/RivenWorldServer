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
import com.gamefocal.rivenworld.game.recipes.clothing.chest.*;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.HeavyIronBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.MediumIronBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SimpleIronBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.feet.SteelBoots_R;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.HeavyIronLegs_R;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.MediumIronLegs_R;
import com.gamefocal.rivenworld.game.recipes.clothing.legs.SimpleIronLegs_R;
import com.gamefocal.rivenworld.game.ui.inventory.RivenCraftingUI;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.LinkedList;

public class AnvilStation extends PlaceableEntity<AnvilStation> implements EntityStorageInterface, TickEntity, CraftingStation {

    protected Inventory inventory = new Inventory(InventoryType.WORKBENCH, "Anvil", "Workbench", 6, 1);

    protected transient LinkedList<HiveNetConnection> inUseBy = new LinkedList<>();

    public AnvilStation() {
        this.type = "Anvil";
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

                // Iron
                new SimpleIronShirt_R(),
                new MediumIronShirt_R(),
                new ChainmailIronShirt_R(),

                // Steel
                new CrusaiderShirt_R(),
                new HeavySteelPlateShirt_R(),
                new SteelPlateShirt_R(),

                /*
                 * Feet
                 * */
                new SimpleIronBoots_R(),
                new MediumIronBoots_R(),
                new HeavyIronBoots_R(),

                new SteelBoots_R(),

                new SimpleIronLegs_R(),
                new MediumIronLegs_R(),
                new HeavyIronLegs_R());
    }
}
