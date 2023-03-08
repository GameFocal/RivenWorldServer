package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.CraftingStation;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.service.InventoryService;
import com.google.gson.JsonObject;

public class RivenCraftingUI extends GameUI<CraftingStation> implements CraftingUI {

    public boolean hasFuel = false;
    public boolean isOn = false;
    protected HiveNetConnection connection;

    public RivenCraftingUI(boolean hasFuel) {
        this.hasFuel = hasFuel;
    }

    public RivenCraftingUI() {
        this.hasFuel = false;
    }

    @Override
    public String name() {
        return "crafting";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, CraftingStation obj) {

        connection.updatePlayerInventory();

        JsonObject o = new JsonObject();

        JsonObject dest = this.getAttached().dest().toJson();
        o.add("station", dest);
        o.add("crafting", this.queue().toJson(connection.getPlayer().inventory));

        o.addProperty("hasFuel", obj.hasFuel());

        if (obj.hasFuel()) {
            o.addProperty("isOn", obj.isOn());
            o.add("fuel", obj.fuel().toJson());
        }

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, CraftingStation object) {
        this.connection = connection;
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        DedicatedServer.get(InventoryService.class).trackInventory(object.dest());

        object.dest().attachToUI(this);
//        this.getSource().attachToUI(this);
        connection.getPlayer().inventory.attachToUI(this);

        if (this.hasFuel) {
            DedicatedServer.get(InventoryService.class).trackInventory(object.fuel());
            object.fuel().attachToUI(this);
        }

        object.onUse(connection);
    }

    @Override
    public void onClose(HiveNetConnection connection, CraftingStation object) {
        connection.getPlayer().inventory.detachFromUI(this);
        object.dest().detachFromUI(this);

        if (this.hasFuel) {
            object.fuel().detachFromUI(this);
        }

        object.onLeave(connection);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("OnOff")) {
            this.getAttached().toggleOnOff(connection);
        }
    }

    @Override
    public CraftingQueue queue() {
        return this.getAttached().dest().getCraftingQueue();
    }

    @Override
    public Inventory getSource() {
        return this.connection.getPlayer().inventory;
    }

    @Override
    public Inventory getDest() {
        return this.getAttached().dest();
    }

    @Override
    public Location getLocation() {
        return this.getAttached().getLocation();
    }
}
