package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.recipes.Placeables.CampFirePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.LandClaimPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.Placeables.WorkBenchPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.PlankRecipe;
import com.gamefocal.rivenworld.game.recipes.Weapons.StoneHatchetRecipe;
import com.gamefocal.rivenworld.game.recipes.Weapons.WoodenClubRecipe;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonObject;

public class RivenInventoryUI extends GameUI<Inventory> implements CraftingUI {

    public RivenInventoryUI() {
        this.focus = false;
    }

    @Override
    public String name() {
        return "riven-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {
        connection.updatePlayerInventory();

        JsonObject o = new JsonObject();

        obj.getCraftingQueue().addAllowedRecipes(
                new WoodenClubRecipe(),
                new StoneHatchetRecipe(),
                new WorkBenchPlaceableRecipe(),
                new CampFirePlaceableRecipe()
        );

        /*
         * Load personal crafting recipes
         * */
        if (obj.getCraftingQueue() != null) {
            // Has a crafting queue
            o.add("crafting", obj.getCraftingQueue().toJson(obj));
        } else {
            System.err.println("No Crafting Queue");
        }

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        object.attachToUI(this);
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {
        object.detachFromUI(this);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("mv")) {
            String fromSlot = data[0];
            String toSlot = data[1];
            this.getAttached().transferToSlot(Integer.parseInt(fromSlot), Integer.parseInt(toSlot));
            this.update(connection);
            connection.updatePlayerInventory();
        } else if (tag.equalsIgnoreCase("drop")) {
            String fromSlot = data[0];
            this.getAttached().dropCompleteSlot(connection, Integer.parseInt(fromSlot));
            this.update(connection);
            connection.updatePlayerInventory();
        } else if (tag.equalsIgnoreCase("split")) {
            String slot = data[0];
            String toSlot = data[1];
            this.getAttached().split(Integer.parseInt(data[0]), Integer.parseInt(toSlot));
            this.update(connection);
            connection.updatePlayerInventory();
        } else if (tag.equalsIgnoreCase("eq")) {

            if (this.getAttached().equipFromSlot(connection, Integer.parseInt(data[0]), EquipmentSlot.valueOf(data[1]))) {
                this.update(connection);
            }

        } else if (tag.equalsIgnoreCase("ueq")) {
            System.out.println("UNEQ");
        }
    }

    @Override
    public CraftingQueue queue() {
        return this.getAttached().getCraftingQueue();
    }

    @Override
    public Inventory getSource() {
        return this.getAttached();
    }

    @Override
    public Inventory getDest() {
        return this.getAttached();
    }

    @Override
    public Location getLocation() {
        return this.getOwner().getPlayer().location;
    }
}
